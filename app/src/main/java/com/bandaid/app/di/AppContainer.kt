package com.bandaid.app.di

import com.bandaid.app.data.local.InMemoryMedicineRepository
import com.bandaid.app.domain.repository.MedicineRepository
import com.bandaid.app.ui.main.MainViewModelFactory

class AppContainer {
    val medicineRepository: MedicineRepository = InMemoryMedicineRepository()

    val mainViewModelFactory: MainViewModelFactory =
        MainViewModelFactory(medicineRepository)
}
