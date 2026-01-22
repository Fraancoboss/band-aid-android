/*
 * Responsibility:
 * - Provides main screen UI state from in-memory repositories.
 * - Exposes minimal read/add actions for demo data.
 * - Does NOT perform validation, persistence, or scheduling.
 * Layer: UI (ViewModel).
 * Scope: demo-only for v0.1.
 *
 * Responsabilidad:
 * - Provee estado de UI desde repositorios in-memory.
 * - Expone acciones minimas de lectura/alta para datos demo.
 * - NO realiza validacion, persistencia ni scheduling.
 * Capa: UI (ViewModel).
 * Alcance: demo para v0.1.
 */
package com.bandaid.app.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bandaid.app.domain.model.CalendarEntry
import com.bandaid.app.domain.model.Medicine
import com.bandaid.app.domain.repository.CalendarEntryRepository
import com.bandaid.app.domain.repository.MedicineRepository
import java.time.LocalDateTime
import java.util.UUID

class MainViewModel(
    private val medicineRepository: MedicineRepository,
    private val calendarEntryRepository: CalendarEntryRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<MainUiState>(MainUiState.Loading)
    val uiState: LiveData<MainUiState> = _uiState

    init {
        load()
    }

    fun load() {
        // WHY THIS DECISION:
        // v0.1 shows only active medicines to keep the main list focused.
        //
        // POR QUE ESTA DECISION:
        // v0.1 muestra solo medicinas activas para mantener la lista principal simple.
        val items = medicineRepository.getAll().filter { it.isActive }
        _uiState.value = if (items.isEmpty()) {
            MainUiState.Empty
        } else {
            MainUiState.Content(items)
        }
    }

    fun addDemoMedicine(name: String, dosage: String, instructions: String) {
        // WHY THIS DECISION:
        // Demo data uses generated IDs to avoid persistence requirements in v0.1.
        //
        // POR QUE ESTA DECISION:
        // Los datos demo usan IDs generados para evitar persistencia en v0.1.
        val demo = Medicine(
            id = UUID.randomUUID().toString(),
            name = name,
            dosage = dosage,
            instructions = instructions,
            isActive = true
        )
        medicineRepository.upsert(demo)
        // WHY THIS DECISION:
        // CalendarEntry demo data is generated here to keep Activity UI simple.
        // Dates are explicit and deterministic for a local-only v0.1.x flow.
        //
        // POR QUE ESTA DECISION:
        // Los CalendarEntry demo se generan aqui para mantener la UI simple.
        // Las fechas son explicitas y deterministas para v0.1.x local.
        val now = LocalDateTime.now()
        val demoEntries = listOf(1L, 2L, 3L).map { offsetDays ->
            CalendarEntry(
                id = UUID.randomUUID().toString(),
                medicineId = demo.id,
                // WHY THIS DECISION:
                // DoseSchedule is intentionally not used in v0.1.x; this is a placeholder ID only.
                //
                // POR QUE ESTA DECISION:
                // No se usa DoseSchedule en v0.1.x; este ID es solo un placeholder.
                scheduleId = "demo",
                expectedAt = now.plusDays(offsetDays),
                isCompleted = false
            )
        }
        demoEntries.forEach { calendarEntryRepository.upsert(it) }
        load()
    }
}
