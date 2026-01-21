package com.bandaid.app.data.local

import com.bandaid.app.domain.model.DoseLog
import com.bandaid.app.domain.repository.DoseLogRepository

class InMemoryDoseLogRepository : DoseLogRepository {
    private val items = LinkedHashMap<String, DoseLog>()

    override fun getAll(): List<DoseLog> = items.values.toList()

    override fun getById(id: String): DoseLog? = items[id]

    override fun upsert(log: DoseLog) {
        items[log.id] = log
    }

    override fun deleteById(id: String): Boolean = items.remove(id) != null
}
