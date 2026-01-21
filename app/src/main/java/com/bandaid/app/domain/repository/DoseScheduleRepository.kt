package com.bandaid.app.domain.repository

import com.bandaid.app.domain.model.DoseSchedule

interface DoseScheduleRepository {
    fun getAll(): List<DoseSchedule>
    fun getById(id: String): DoseSchedule?
    fun upsert(schedule: DoseSchedule)
    fun deleteById(id: String): Boolean
}
