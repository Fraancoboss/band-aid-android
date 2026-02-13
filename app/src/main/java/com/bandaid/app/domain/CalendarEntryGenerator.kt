package com.bandaid.app.domain

import com.bandaid.app.domain.model.CalendarEntry
import com.bandaid.app.domain.model.DoseSchedule
import com.bandaid.app.domain.repository.CalendarEntryRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

/*
 * Responsabilidad:
 * - Convierte una pauta (DoseSchedule) en entradas concretas de calendario.
 * - Genera ocurrencias por fecha/hora segun frecuencia y rango.
 * - Tambien elimina entradas ligadas a una pauta al reemplazarla.
 *
 * Capa: dominio (servicio de aplicacion).
 * Alcance: logica local de generacion para v0.1.x.
 */
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
