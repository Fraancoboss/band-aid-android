package com.bandaid.app.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bandaid.app.domain.repository.CalendarEntryRepository
import com.bandaid.app.domain.repository.DoseLogRepository
import com.bandaid.app.domain.repository.MedicineRepository

class CalendarViewModelFactory(
    private val calendarEntryRepository: CalendarEntryRepository,
    private val doseLogRepository: DoseLogRepository,
    private val medicineRepository: MedicineRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalendarViewModel(
                calendarEntryRepository,
                doseLogRepository,
                medicineRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
