/*
 * Responsibility:
 * - In-memory store for DoseSchedule entities.
 * - Provides minimal CRUD without persistence or threading.
 * - Does NOT generate calendar entries.
 * Layer: data (local, in-memory).
 * Scope: demo-only for v0.1.
 *
 * Responsabilidad:
 * - Almacen in-memory para entidades DoseSchedule.
 * - Provee CRUD minimo sin persistencia ni hilos.
 * - NO genera entradas de calendario.
 * Capa: data (local, in-memory).
 * Alcance: demo para v0.1.
 */
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
