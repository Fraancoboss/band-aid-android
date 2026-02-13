package com.bandaid.app.ui.medicine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bandaid.app.data.local.InMemoryMedicineMetaStore
import com.bandaid.app.domain.repository.CalendarEntryRepository
import com.bandaid.app.domain.repository.DoseLogRepository
import com.bandaid.app.domain.repository.MedicineRepository

class MedicineDetailViewModelFactory(
    private val medicineRepository: MedicineRepository,
    private val doseLogRepository: DoseLogRepository,
    private val calendarEntryRepository: CalendarEntryRepository,
    private val medicineMetaStore: InMemoryMedicineMetaStore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedicineDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedicineDetailViewModel(
                medicineRepository,
                doseLogRepository,
                calendarEntryRepository,
                medicineMetaStore
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
