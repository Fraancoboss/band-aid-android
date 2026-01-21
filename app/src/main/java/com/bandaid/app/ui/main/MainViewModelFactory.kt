package com.bandaid.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bandaid.app.domain.repository.MedicineRepository

class MainViewModelFactory(
    private val medicineRepository: MedicineRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(medicineRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
