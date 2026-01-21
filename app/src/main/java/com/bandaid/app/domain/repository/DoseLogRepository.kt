package com.bandaid.app.domain.repository

import com.bandaid.app.domain.model.DoseLog

interface DoseLogRepository {
    fun getAll(): List<DoseLog>
    fun getById(id: String): DoseLog?
    fun upsert(log: DoseLog)
    fun deleteById(id: String): Boolean
}
