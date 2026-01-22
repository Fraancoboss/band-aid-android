/*
 * Responsibility:
 * - Simple factory to build MainViewModel with explicit dependencies.
 * - Avoids framework DI while keeping Activity clean.
 * - Does NOT add lifecycle logic beyond ViewModel creation.
 * Layer: UI (wiring).
 * Scope: stable for v0.1.
 *
 * Responsabilidad:
 * - Factory simple para crear MainViewModel con dependencias explicitas.
 * - Evita frameworks DI y mantiene la Activity limpia.
 * - NO agrega logica de ciclo de vida mas alla de crear el ViewModel.
 * Capa: UI (wiring).
 * Alcance: estable para v0.1.
 */
package com.bandaid.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bandaid.app.domain.repository.CalendarEntryRepository
import com.bandaid.app.domain.repository.MedicineRepository

class MainViewModelFactory(
    private val medicineRepository: MedicineRepository,
    private val calendarEntryRepository: CalendarEntryRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                medicineRepository,
                calendarEntryRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
