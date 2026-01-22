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
import com.bandaid.app.domain.model.Medicine
import com.bandaid.app.domain.repository.MedicineRepository
import java.util.UUID

class MainViewModel(
    private val medicineRepository: MedicineRepository
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
        load()
    }
}
