/*
 * Responsibility:
 * - In-memory store for DoseLog entities.
 * - Provides minimal CRUD without persistence or threading.
 * - Does NOT deduplicate or enforce associations.
 * Layer: data (local, in-memory).
 * Scope: demo-only for v0.1.
 *
 * Responsabilidad:
 * - Almacen in-memory para entidades DoseLog.
 * - Provee CRUD minimo sin persistencia ni hilos.
 * - NO deduplica ni aplica asociaciones.
 * Capa: data (local, in-memory).
 * Alcance: demo para v0.1.
 */
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
