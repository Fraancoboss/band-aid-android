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
        val items = medicineRepository.getAll().filter { it.isActive }
        _uiState.value = if (items.isEmpty()) {
            MainUiState.Empty
        } else {
            MainUiState.Content(items)
        }
    }

    fun addDemoMedicine(name: String, dosage: String, instructions: String) {
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
