package com.bandaid.app.di

import com.bandaid.app.data.local.InMemoryMedicineRepository
import com.bandaid.app.data.local.InMemoryDoseLogRepository
import com.bandaid.app.domain.repository.DoseLogRepository
import com.bandaid.app.domain.repository.MedicineRepository
import com.bandaid.app.ui.main.MainViewModelFactory

class AppContainer {
    val medicineRepository: MedicineRepository = InMemoryMedicineRepository()
    val doseLogRepository: DoseLogRepository = InMemoryDoseLogRepository()

    val mainViewModelFactory: MainViewModelFactory =
        MainViewModelFactory(medicineRepository)
}
