/*
 * Responsibility:
 * - Manual dependency container for v0.1 wiring.
 * - Provides in-memory repositories and ViewModel factories.
 * - Does NOT perform persistence, background work, or business logic.
 * Layer: wiring (DI-lite).
 * Scope: temporary for v0.1.
 *
 * Responsabilidad:
 * - Contenedor manual de dependencias para el wiring de v0.1.
 * - Provee repositorios in-memory y factories de ViewModel.
 * - NO realiza persistencia, trabajo en segundo plano ni logica de negocio.
 * Capa: wiring (DI-lite).
 * Alcance: temporal para v0.1.
 */
package com.bandaid.app.di

import com.bandaid.app.data.local.InMemoryMedicineRepository
import com.bandaid.app.data.local.InMemoryDoseLogRepository
import com.bandaid.app.data.local.InMemoryCalendarEntryRepository
import com.bandaid.app.domain.repository.CalendarEntryRepository
import com.bandaid.app.domain.repository.DoseLogRepository
import com.bandaid.app.domain.repository.MedicineRepository
import com.bandaid.app.ui.main.MainViewModelFactory

class AppContainer {
    // WHY THIS DECISION:
    // In-memory repositories avoid persistence and keep the app local-only for v0.1.
    //
    // POR QUE ESTA DECISION:
    // Los repositorios in-memory evitan persistencia y mantienen la app local en v0.1.
    val medicineRepository: MedicineRepository = InMemoryMedicineRepository()
    val doseLogRepository: DoseLogRepository = InMemoryDoseLogRepository()
    val calendarEntryRepository: CalendarEntryRepository = InMemoryCalendarEntryRepository()

    // WHY THIS DECISION:
    // ViewModel factory is created here to avoid creating repositories inside Activities.
    //
    // POR QUE ESTA DECISION:
    // El factory de ViewModel se crea aqui para evitar instancias en Activities.
    val mainViewModelFactory: MainViewModelFactory =
        MainViewModelFactory(medicineRepository)
}
