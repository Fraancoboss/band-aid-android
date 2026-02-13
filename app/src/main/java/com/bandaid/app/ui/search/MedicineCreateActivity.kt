/*
 * Responsibility:
 * - Creates/edits medicines and optional schedules for local demo repositories.
 * - Validates required fields and emits in-memory schedule entries.
 * - Does NOT persist data across app restarts.
 * Layer: UI (Activity).
 * Scope: stable for v0.1.x.
 */
package com.bandaid.app.ui.search

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bandaid.app.BandAidApplication
import com.bandaid.app.R
import com.bandaid.app.databinding.ActivityMedicineCreateBinding
import com.bandaid.app.domain.model.DoseSchedule
import com.bandaid.app.domain.model.Medicine
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

class MedicineCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicineCreateBinding
    private val selectedTimes = linkedSetOf<LocalTime>()
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    private var editingMedicineId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicineCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { finish() }

        editingMedicineId = intent.getStringExtra(EXTRA_MEDICINE_ID)
        loadPrefill()

        binding.buttonAddTime.setOnClickListener { openTimePicker() }
        binding.buttonSave.setOnClickListener { saveMedicine() }
    }

    private fun loadPrefill() {
        val container = (application as BandAidApplication).appContainer
        val editingId = editingMedicineId

        if (!editingId.isNullOrBlank()) {
            binding.toolbar.setTitle(R.string.toolbar_title_edit)
            val medicine = container.medicineRepository.getById(editingId)
            if (medicine != null) {
                binding.inputName.setText(medicine.name)
                binding.inputDosage.setText(medicine.dosage)
                binding.inputInstructions.setText(medicine.instructions)
                binding.inputDuration.setText(
                    container.medicineMetaStore.getDuration(editingId).orEmpty()
                )
            }

            val schedule = container.doseScheduleRepository.getAll()
                .firstOrNull { it.medicineId == editingId }
            if (schedule != null) {
                binding.inputFrequency.setText(schedule.frequencyDays.toString())
                selectedTimes.clear()
                selectedTimes.addAll(schedule.timesOfDay.sorted())
                renderTimeChips()
            }
            return
        }

        val prefillName = intent.getStringExtra(EXTRA_NAME).orEmpty()
        val prefillDosage = intent.getStringExtra(EXTRA_DOSAGE).orEmpty()
        val prefillInstructions = intent.getStringExtra(EXTRA_INSTRUCTIONS).orEmpty()
        val prefillDuration = intent.getStringExtra(EXTRA_DURATION).orEmpty()
        val hasPrefill = prefillName.isNotBlank() || prefillDosage.isNotBlank() ||
            prefillInstructions.isNotBlank() || prefillDuration.isNotBlank()
        binding.textPrefillNote.visibility = if (hasPrefill) android.view.View.VISIBLE else android.view.View.GONE
        binding.inputName.setText(prefillName)
        binding.inputDosage.setText(prefillDosage)
        binding.inputInstructions.setText(prefillInstructions)
        binding.inputDuration.setText(prefillDuration)
    }

    private fun openTimePicker() {
        val now = LocalTime.now()
        TimePickerDialog(
            this,
            { _, hour, minute ->
                selectedTimes.add(LocalTime.of(hour, minute))
                renderTimeChips()
            },
            now.hour,
            now.minute,
            true
        ).show()
    }

    private fun renderTimeChips() {
        binding.chipGroupTimes.removeAllViews()
        selectedTimes.sorted().forEach { time ->
            val chip = Chip(this).apply {
                text = time.format(timeFormatter)
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    selectedTimes.remove(time)
                    renderTimeChips()
                }
            }
            binding.chipGroupTimes.addView(chip)
        }
    }

    private fun saveMedicine() {
        binding.layoutName.error = null
        binding.layoutDosage.error = null
        binding.layoutInstructions.error = null
        binding.layoutFrequency.error = null

        val name = binding.inputName.text?.toString()?.trim().orEmpty()
        val dosage = binding.inputDosage.text?.toString()?.trim().orEmpty()
        val instructions = binding.inputInstructions.text?.toString()?.trim().orEmpty()
        val duration = binding.inputDuration.text?.toString()?.trim().orEmpty()
        val frequencyText = binding.inputFrequency.text?.toString()?.trim().orEmpty()

        var hasError = false
        if (name.isBlank()) {
            binding.layoutName.error = getString(R.string.error_field_required)
            hasError = true
        }
        if (dosage.isBlank()) {
            binding.layoutDosage.error = getString(R.string.error_field_required)
            hasError = true
        }
        if (instructions.isBlank()) {
            binding.layoutInstructions.error = getString(R.string.error_field_required)
            hasError = true
        }

        val frequencyDays = if (frequencyText.isBlank()) {
            null
        } else {
            frequencyText.toIntOrNull()
        }
        if (frequencyText.isNotBlank() && (frequencyDays == null || frequencyDays <= 0)) {
            binding.layoutFrequency.error = getString(R.string.error_field_required)
            hasError = true
        }
        if (frequencyDays == null && selectedTimes.isNotEmpty()) {
            binding.layoutFrequency.error = getString(R.string.error_field_required)
            hasError = true
        }
        if (frequencyDays != null && selectedTimes.isEmpty()) {
            Snackbar.make(
                binding.root,
                R.string.error_field_required,
                Snackbar.LENGTH_SHORT
            ).show()
            hasError = true
        }
        if (hasError) return

        val container = (application as BandAidApplication).appContainer
        val medicineId = editingMedicineId ?: UUID.randomUUID().toString()
        val existing = container.medicineRepository.getById(medicineId)
        val medicine = Medicine(
            id = medicineId,
            name = name,
            dosage = dosage,
            instructions = instructions,
            isActive = existing?.isActive ?: true
        )
        container.medicineRepository.upsert(medicine)
        container.medicineMetaStore.setDuration(medicineId, duration)
        replaceSchedule(
            medicineId = medicineId,
            frequencyDays = frequencyDays,
            durationText = duration
        )

        val message = if (editingMedicineId == null) {
            R.string.snackbar_medicine_created
        } else {
            R.string.snackbar_medicine_updated
        }
        setResult(
            RESULT_OK,
            Intent().putExtra(EXTRA_RESULT_MESSAGE_RES, message)
        )
        finish()
    }

    private fun replaceSchedule(
        medicineId: String,
        frequencyDays: Int?,
        durationText: String
    ) {
        val container = (application as BandAidApplication).appContainer
        val existingSchedules = container.doseScheduleRepository.getAll()
            .filter { it.medicineId == medicineId }
        existingSchedules.forEach { schedule ->
            container.calendarEntryGenerator.removeEntriesForSchedule(schedule.id)
            container.doseScheduleRepository.deleteById(schedule.id)
        }

        if (frequencyDays == null || selectedTimes.isEmpty()) return

        val startDate = LocalDate.now()
        val durationDays = durationText.toIntOrNull()
        val endDate = if (durationDays != null && durationDays > 0) {
            startDate.plusDays((durationDays - 1).toLong())
        } else {
            null
        }
        val schedule = DoseSchedule(
            id = UUID.randomUUID().toString(),
            medicineId = medicineId,
            startDate = startDate,
            endDate = endDate,
            timesOfDay = selectedTimes.sorted(),
            frequencyDays = frequencyDays
        )
        container.doseScheduleRepository.upsert(schedule)
        container.calendarEntryGenerator.generateFromSchedule(medicineId, schedule)
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DOSAGE = "extra_dosage"
        const val EXTRA_INSTRUCTIONS = "extra_instructions"
        const val EXTRA_DURATION = "extra_duration"
        const val EXTRA_MEDICINE_ID = "extra_medicine_id"
        const val EXTRA_RESULT_MESSAGE_RES = "extra_result_message_res"
    }
}
