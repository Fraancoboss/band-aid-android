package com.bandaid.app.domain

import com.bandaid.app.domain.model.CalendarEntry
import com.bandaid.app.domain.model.DoseSchedule
import com.bandaid.app.domain.repository.CalendarEntryRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class CalendarEntryGenerator(
    private val calendarEntryRepository: CalendarEntryRepository
) {

    fun generateFromSchedule(medicineId: String, schedule: DoseSchedule) {
        val endDate = schedule.endDate ?: schedule.startDate.plusDays(30)
        var currentDate = schedule.startDate

        while (!currentDate.isAfter(endDate)) {
            for (time in schedule.timesOfDay) {
                val entry = CalendarEntry(
                    id = UUID.randomUUID().toString(),
                    medicineId = medicineId,
                    scheduleId = schedule.id,
                    expectedAt = LocalDateTime.of(currentDate, time),
                    isCompleted = false
                )
                calendarEntryRepository.upsert(entry)
            }
            currentDate = currentDate.plusDays(schedule.frequencyDays.toLong())
        }
    }

    fun removeEntriesForSchedule(scheduleId: String) {
        val toRemove = calendarEntryRepository.getAll()
            .filter { it.scheduleId == scheduleId }
        toRemove.forEach { calendarEntryRepository.deleteById(it.id) }
    }
}
