/*
 * Responsibility:
 * - Passive, read-only calendar list of CalendarEntry items.
 * - Reads repositories directly via AppContainer without ViewModel.
 * - Does NOT create entries, schedule work, or allow edits.
 * Layer: UI (Activity).
 * Scope: demo-only for v0.1.
 *
 * Responsabilidad:
 * - Lista pasiva y de solo lectura de CalendarEntry.
 * - Lee repositorios via AppContainer sin ViewModel.
 * - NO crea entradas, agenda trabajo ni permite edicion.
 * Capa: UI (Activity).
 * Alcance: demo para v0.1.
 */
package com.bandaid.app.ui.calendar

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bandaid.app.BandAidApplication
import com.bandaid.app.R
import com.bandaid.app.databinding.ActivityCalendarBinding
import com.bandaid.app.databinding.ItemCalendarEntryBinding
import com.bandaid.app.domain.model.CalendarEntry
import com.bandaid.app.domain.model.DoseLog
import com.bandaid.app.domain.repository.CalendarEntryRepository
import com.bandaid.app.domain.repository.DoseLogRepository
import com.bandaid.app.domain.repository.MedicineRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarBinding

    private val calendarEntryRepository: CalendarEntryRepository
        get() = (application as BandAidApplication).appContainer.calendarEntryRepository

    private val doseLogRepository: DoseLogRepository
        get() = (application as BandAidApplication).appContainer.doseLogRepository

    private val medicineRepository: MedicineRepository
        get() = (application as BandAidApplication).appContainer.medicineRepository

    private val dateTimeFormatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm",
        Locale.getDefault()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // WHY THIS DECISION:
        // v0.1 calendar is intentionally passive and read-only.
        //
        // POR QUE ESTA DECISION:
        // El calendario de v0.1 es intencionalmente pasivo y de solo lectura.
        renderCalendarEntries()
    }

    private fun renderCalendarEntries() {
        val entries = calendarEntryRepository.getAll()
            .sortedBy { it.expectedAt }
        val doseLogs = doseLogRepository.getAll()

        binding.layoutCalendarEntries.removeAllViews()
        // WHY THIS DECISION:
        // v0.1 does not generate CalendarEntry automatically, so the list can be empty.
        //
        // POR QUE ESTA DECISION:
        // v0.1 no genera CalendarEntry automaticamente, por eso la lista puede estar vacia.
        if (entries.isEmpty()) {
            binding.textCalendarEmpty.visibility = View.VISIBLE
            binding.layoutCalendarEntries.visibility = View.GONE
            return
        }

        binding.textCalendarEmpty.visibility = View.GONE
        binding.layoutCalendarEntries.visibility = View.VISIBLE

        entries.forEach { entry ->
            val itemBinding = ItemCalendarEntryBinding.inflate(
                layoutInflater,
                binding.layoutCalendarEntries,
                false
            )
            bindEntry(itemBinding, entry, doseLogs)
            binding.layoutCalendarEntries.addView(itemBinding.root)
        }
    }

    private fun bindEntry(
        itemBinding: ItemCalendarEntryBinding,
        entry: CalendarEntry,
        doseLogs: List<DoseLog>
    ) {
        val medicineName = medicineRepository.getById(entry.medicineId)?.name
            ?: getString(R.string.medicine_unknown)
        val formattedTime = entry.expectedAt.format(dateTimeFormatter)
        // WHY THIS DECISION:
        // v0.1 associates CalendarEntry to DoseLog using medicineId + scheduledTime only.
        // Limitation: manual logs may have scheduledTime null and appear as pending here.
        //
        // POR QUE ESTA DECISION:
        // v0.1 asocia CalendarEntry con DoseLog usando solo medicineId + scheduledTime.
        // Limite: tomas manuales pueden tener scheduledTime null y verse como pendientes.
        val isTaken = doseLogs.any {
            it.medicineId == entry.medicineId &&
                it.scheduledTime == entry.expectedAt
        }
        val statusText = if (isTaken) {
            getString(R.string.calendar_status_taken)
        } else {
            getString(R.string.calendar_status_pending)
        }

        itemBinding.textMedicine.text = getString(
            R.string.calendar_medicine_format,
            medicineName
        )
        itemBinding.textExpectedAt.text = getString(
            R.string.calendar_expected_at_format,
            formattedTime
        )
        itemBinding.textStatus.text = getString(
            R.string.calendar_status_format,
            statusText
        )
    }
}
