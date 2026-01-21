package com.bandaid.app.data.local

import com.bandaid.app.domain.model.DoseSchedule
import com.bandaid.app.domain.repository.DoseScheduleRepository

class InMemoryDoseScheduleRepository : DoseScheduleRepository {
    private val items = LinkedHashMap<String, DoseSchedule>()

    override fun getAll(): List<DoseSchedule> = items.values.toList()

    override fun getById(id: String): DoseSchedule? = items[id]

    override fun upsert(schedule: DoseSchedule) {
        items[schedule.id] = schedule
    }

    override fun deleteById(id: String): Boolean = items.remove(id) != null
}
