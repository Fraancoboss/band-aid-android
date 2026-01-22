/*
 * Responsibility:
 * - Displays read-only medicine details and allows manual dose logging.
 * - Reads/writes in-memory repositories via AppContainer.
 * - Does NOT edit medicines, schedule reminders, or persist data.
 * Layer: UI (Activity).
 * Scope: demo-only for v0.1.
 *
 * Responsabilidad:
 * - Muestra detalle de medicina en solo lectura y permite registrar una toma manual.
 * - Lee/escribe repositorios in-memory via AppContainer.
 * - NO edita medicinas, agenda recordatorios ni persiste datos.
 * Capa: UI (Activity).
 * Alcance: demo para v0.1.
 */
package com.bandaid.app.ui.medicine

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bandaid.app.BandAidApplication
import com.bandaid.app.R
import com.bandaid.app.databinding.ActivityMedicineDetailBinding
import com.bandaid.app.databinding.ItemDoseLogBinding
import com.bandaid.app.domain.model.DoseLog
import com.bandaid.app.domain.repository.CalendarEntryRepository
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

    private val calendarEntryRepository: CalendarEntryRepository
        get() = (application as BandAidApplication).appContainer.calendarEntryRepository

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

        // WHY THIS DECISION:
        // Direct repository access avoids introducing a ViewModel for a simple v0.1 screen.
        //
        // POR QUE ESTA DECISION:
        // Acceso directo evita un ViewModel en una pantalla simple de v0.1.
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
        // WHY THIS DECISION:
        // Manual ID/time generation keeps the flow local-only without persistence.
        //
        // POR QUE ESTA DECISION:
        // La generacion manual de ID/tiempo mantiene el flujo local sin persistencia.
        val takenAt = LocalDateTime.now()
        val scheduledTime = resolveScheduledTime(takenAt)
        val doseLog = DoseLog(
            id = UUID.randomUUID().toString(),
            medicineId = medicineId,
            scheduledTime = scheduledTime,
            takenAt = takenAt,
            status = "taken",
            notes = null
        )
        doseLogRepository.upsert(doseLog)
        binding.textFeedback.text = getString(R.string.dose_log_registered)
        renderDoseLogs()
    }

    private fun resolveScheduledTime(takenAt: LocalDateTime): LocalDateTime? {
        // WHY THIS DECISION:
        // Select the closest CalendarEntry at or before the manual takenAt time.
        //
        // POR QUE ESTA DECISION:
        // Se selecciona el CalendarEntry mas cercano en el tiempo, igual o anterior a takenAt.
        val entries = calendarEntryRepository.getAll()
            .filter { it.medicineId == medicineId }
            .filter { it.expectedAt <= takenAt }

        // WHY THIS DECISION:
        // If no matching entry exists, keep scheduledTime null to avoid creating entries.
        //
        // POR QUE ESTA DECISION:
        // Si no hay coincidencia, se mantiene scheduledTime null y no se crean entradas.
        if (entries.isEmpty()) {
            return null
        }

        return entries.maxBy { it.expectedAt }.expectedAt
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
        // WHY THIS DECISION:
        // A simple LinearLayout list avoids RecyclerView overhead for a small v0.1 list.
        //
        // POR QUE ESTA DECISION:
        // Una lista simple evita RecyclerView en una lista pequena de v0.1.
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
