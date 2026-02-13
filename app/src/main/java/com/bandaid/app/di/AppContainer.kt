package com.bandaid.app.di

import com.bandaid.app.data.local.InMemoryMedicineRepository
import com.bandaid.app.data.local.InMemoryDoseLogRepository
import com.bandaid.app.data.local.InMemoryDoseScheduleRepository
import com.bandaid.app.data.local.InMemoryCalendarEntryRepository
import com.bandaid.app.data.local.InMemoryMedicineMetaStore
import com.bandaid.app.data.local.InMemoryUserProfileStore
import com.bandaid.app.data.local.InMemoryUserRepository
import com.bandaid.app.domain.CalendarEntryGenerator
import com.bandaid.app.domain.repository.CalendarEntryRepository
import com.bandaid.app.domain.repository.DoseLogRepository
import com.bandaid.app.domain.repository.DoseScheduleRepository
import com.bandaid.app.domain.repository.MedicineRepository
import com.bandaid.app.domain.repository.UserRepository
import com.bandaid.app.ui.calendar.CalendarViewModelFactory
import com.bandaid.app.ui.main.MainViewModelFactory
import com.bandaid.app.ui.medicine.MedicineDetailViewModelFactory

/*
 * Responsabilidad:
 * - Centraliza el wiring de dependencias de la app.
 * - Expone repositorios in-memory, stores y factories de ViewModel.
 * - Evita instanciacion manual en Activities.
 *
 * Capa: DI/Wiring.
 * Alcance: v0.1.x local-only.
 */
class AppContainer {
    val medicineRepository: MedicineRepository = InMemoryMedicineRepository()
    val doseLogRepository: DoseLogRepository = InMemoryDoseLogRepository()
    val doseScheduleRepository: DoseScheduleRepository = InMemoryDoseScheduleRepository()
    val calendarEntryRepository: CalendarEntryRepository = InMemoryCalendarEntryRepository()
    val userRepository: UserRepository = InMemoryUserRepository()
    val userProfileStore: InMemoryUserProfileStore = InMemoryUserProfileStore()
    val medicineMetaStore: InMemoryMedicineMetaStore = InMemoryMedicineMetaStore()

    val calendarEntryGenerator: CalendarEntryGenerator =
        CalendarEntryGenerator(calendarEntryRepository)

    val mainViewModelFactory: MainViewModelFactory =
        MainViewModelFactory(medicineRepository, calendarEntryRepository)

    val calendarViewModelFactory: CalendarViewModelFactory =
        CalendarViewModelFactory(calendarEntryRepository, doseLogRepository, medicineRepository)

    val medicineDetailViewModelFactory: MedicineDetailViewModelFactory =
        MedicineDetailViewModelFactory(
            medicineRepository,
            doseLogRepository,
            calendarEntryRepository,
            medicineMetaStore
        )
}
