/*
 * Responsibility:
 * - Displays medicine detail and routes user actions through MedicineDetailViewModel.
 * - Renders dose logs, activation state, and delete/edit actions.
 * - Does NOT implement persistence or background work.
 * Layer: UI (Activity).
 * Scope: stable for v0.1.x.
 */
package com.bandaid.app.ui.medicine

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bandaid.app.BandAidApplication
import com.bandaid.app.R
import com.bandaid.app.databinding.ActivityMedicineDetailBinding
import com.bandaid.app.databinding.ItemDoseLogBinding
import com.bandaid.app.domain.model.DoseLog
import com.bandaid.app.ui.search.MedicineCreateActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MedicineDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicineDetailBinding
    private lateinit var viewModel: MedicineDetailViewModel
    private var medicineId: String? = null
    private var currentIsActive: Boolean = false
    private val editLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val messageRes = result.data?.getIntExtra(
                MedicineCreateActivity.EXTRA_RESULT_MESSAGE_RES,
                0
            ) ?: 0
            if (messageRes != 0) {
                Snackbar.make(binding.root, messageRes, Snackbar.LENGTH_SHORT).show()
            }
            medicineId?.let { viewModel.loadMedicine(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicineDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val container = (application as BandAidApplication).appContainer
        viewModel = ViewModelProvider(
            this,
            container.medicineDetailViewModelFactory
        )[MedicineDetailViewModel::class.java]

        observeViewModel()

        medicineId = intent.getStringExtra(EXTRA_MEDICINE_ID)
        if (medicineId.isNullOrBlank()) {
            showNotFound()
        } else {
            viewModel.loadMedicine(medicineId!!)
        }

        binding.buttonRegisterDose.setOnClickListener {
            viewModel.registerDose()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_medicine_detail, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val toggle = menu.findItem(R.id.action_toggle_active)
        toggle?.setTitle(
            if (currentIsActive) {
                R.string.action_deactivate
            } else {
                R.string.action_activate
            }
        )
        val hasMedicine = !medicineId.isNullOrBlank()
        menu.findItem(R.id.action_edit)?.isVisible = hasMedicine
        menu.findItem(R.id.action_toggle_active)?.isVisible = hasMedicine
        menu.findItem(R.id.action_delete)?.isVisible = hasMedicine
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                openEdit()
                true
            }

            R.id.action_toggle_active -> {
                viewModel.toggleActive()
                true
            }

            R.id.action_delete -> {
                showDeleteConfirmation()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is MedicineDetailUiState.Loading -> {
                    binding.buttonRegisterDose.isEnabled = false
                }

                is MedicineDetailUiState.NotFound -> {
                    showNotFound()
                }

                is MedicineDetailUiState.Content -> {
                    renderContent(state.model)
                }

                is MedicineDetailUiState.Deleted -> {
                    Snackbar.make(
                        binding.root,
                        R.string.snackbar_medicine_deleted,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }

        viewModel.event.observe(this) { wrapper ->
            when (wrapper.getContentIfNotHandled()) {
                is DetailEvent.DoseRegistered -> {
                    Snackbar.make(
                        binding.root,
                        R.string.snackbar_dose_registered,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                is DetailEvent.Deactivated -> {
                    Snackbar.make(
                        binding.root,
                        R.string.snackbar_medicine_deactivated,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    invalidateOptionsMenu()
                }

                is DetailEvent.Activated -> {
                    Snackbar.make(
                        binding.root,
                        R.string.snackbar_medicine_activated,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    invalidateOptionsMenu()
                }

                null -> Unit
            }
        }
    }

    private fun renderContent(model: MedicineDetailUiModel) {
        val medicine = model.medicine
        medicineId = medicine.id
        currentIsActive = medicine.isActive
        binding.buttonRegisterDose.isEnabled = true

        binding.textName.text = medicine.name
        binding.textDosage.text = getString(
            R.string.medicine_dosage_format,
            medicine.dosage
        )
        binding.textInstructions.text = getString(
            R.string.medicine_instructions_format,
            medicine.instructions
        )
        val statusText = if (medicine.isActive) {
            getString(R.string.medicine_status_active)
        } else {
            getString(R.string.medicine_status_inactive)
        }
        binding.textStatus.text = getString(
            R.string.medicine_status_format,
            statusText
        )
        binding.textDuration.text = getString(
            R.string.medicine_duration_format,
            model.duration ?: getString(R.string.medicine_duration_unknown)
        )
        renderDoseLogs(model.doseLogs)
        invalidateOptionsMenu()
    }

    private fun renderDoseLogs(logs: List<DoseLog>) {
        binding.layoutDoseLogs.removeAllViews()
        if (logs.isEmpty()) {
            binding.textDoseLogsEmpty.visibility = View.VISIBLE
            binding.layoutDoseLogs.visibility = View.GONE
            return
        }

        binding.textDoseLogsEmpty.visibility = View.GONE
        binding.layoutDoseLogs.visibility = View.VISIBLE
        logs.forEach { log ->
            val itemBinding = ItemDoseLogBinding.inflate(
                layoutInflater,
                binding.layoutDoseLogs,
                false
            )
            itemBinding.textDoseLog.text = getString(
                R.string.dose_log_item_format,
                viewModel.formatDoseLog(log)
            )
            binding.layoutDoseLogs.addView(itemBinding.root)
        }
    }

    private fun showNotFound() {
        medicineId = null
        currentIsActive = false
        binding.textName.text = getString(R.string.medicine_not_found)
        binding.textDosage.text = getString(R.string.empty_text)
        binding.textInstructions.text = getString(R.string.empty_text)
        binding.textStatus.text = getString(R.string.empty_text)
        binding.textDuration.text = getString(R.string.empty_text)
        binding.textDoseLogsEmpty.visibility = View.GONE
        binding.layoutDoseLogs.removeAllViews()
        binding.layoutDoseLogs.visibility = View.GONE
        binding.buttonRegisterDose.isEnabled = false
        invalidateOptionsMenu()
    }

    private fun showDeleteConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_title)
            .setMessage(R.string.dialog_delete_message)
            .setNegativeButton(R.string.dialog_cancel, null)
            .setPositiveButton(R.string.dialog_confirm) { _, _ ->
                viewModel.deleteMedicine()
            }
            .show()
    }

    private fun openEdit() {
        val id = medicineId ?: return
        val intent = Intent(this, MedicineCreateActivity::class.java)
        intent.putExtra(MedicineCreateActivity.EXTRA_MEDICINE_ID, id)
        editLauncher.launch(intent)
    }

    override fun onResume() {
        super.onResume()
        medicineId?.let { viewModel.loadMedicine(it) }
    }

    companion object {
        const val EXTRA_MEDICINE_ID = "medicine_id"
    }
}
