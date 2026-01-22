package com.bandaid.app.ui.medicine

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bandaid.app.BandAidApplication
import com.bandaid.app.R
import com.bandaid.app.databinding.ActivityMedicineDetailBinding
import com.bandaid.app.databinding.ItemDoseLogBinding
import com.bandaid.app.domain.model.DoseLog
import com.bandaid.app.domain.repository.DoseLogRepository
import com.bandaid.app.domain.repository.MedicineRepository
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.util.Locale
import java.util.UUID

class MedicineDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicineDetailBinding
    private lateinit var medicineId: String
    private val doseLogFormatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm",
        Locale.getDefault()
    )

    private val medicineRepository: MedicineRepository
        get() = (application as BandAidApplication).appContainer.medicineRepository

    private val doseLogRepository: DoseLogRepository
        get() = (application as BandAidApplication).appContainer.doseLogRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicineDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extraId = intent.getStringExtra(EXTRA_MEDICINE_ID)
        if (extraId.isNullOrBlank()) {
            showNotFound()
            return
        }
        medicineId = extraId

        val medicine = medicineRepository.getById(medicineId)

        if (medicine == null) {
            showNotFound()
            return
        }

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

        renderDoseLogs()

        binding.buttonRegisterDose.setOnClickListener {
            registerDose()
        }
    }

    private fun showNotFound() {
        binding.textName.text = getString(R.string.medicine_not_found)
        binding.textDosage.text = getString(R.string.empty_text)
        binding.textInstructions.text = getString(R.string.empty_text)
        binding.textStatus.text = getString(R.string.empty_text)
        binding.textFeedback.text = getString(R.string.empty_text)
        binding.textDoseLogsEmpty.text = getString(R.string.empty_text)
        binding.textDoseLogsEmpty.visibility = View.GONE
        binding.layoutDoseLogs.removeAllViews()
        binding.layoutDoseLogs.visibility = View.GONE
        binding.buttonRegisterDose.isEnabled = false
    }

    private fun registerDose() {
        val doseLog = DoseLog(
            id = UUID.randomUUID().toString(),
            medicineId = medicineId,
            scheduledTime = null,
            takenAt = LocalDateTime.now(),
            status = "taken",
            notes = null
        )
        doseLogRepository.upsert(doseLog)
        binding.textFeedback.text = getString(R.string.dose_log_registered)
        renderDoseLogs()
    }

    private fun renderDoseLogs() {
        val logs = doseLogRepository.getAll()
            .filter { it.medicineId == medicineId }
            .sortedByDescending { it.takenAt }

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
            val formatted = log.takenAt.format(doseLogFormatter)
            itemBinding.textDoseLog.text = getString(
                R.string.dose_log_item_format,
                formatted
            )
            binding.layoutDoseLogs.addView(itemBinding.root)
        }
    }

    companion object {
        const val EXTRA_MEDICINE_ID = "medicine_id"
    }
}
