/*
 * Responsibility:
 * - In-memory store for CalendarEntry entities.
 * - Provides minimal CRUD without persistence or threading.
 * - Does NOT derive entries from schedules.
 * Layer: data (local, in-memory).
 * Scope: demo-only for v0.1.
 *
 * Responsabilidad:
 * - Almacen in-memory para entidades CalendarEntry.
 * - Provee CRUD minimo sin persistencia ni hilos.
 * - NO deriva entradas desde schedules.
 * Capa: data (local, in-memory).
 * Alcance: demo para v0.1.
 */
package com.bandaid.app.data.local

import com.bandaid.app.domain.model.CalendarEntry
import com.bandaid.app.domain.repository.CalendarEntryRepository

class InMemoryCalendarEntryRepository : CalendarEntryRepository {
    private val items = LinkedHashMap<String, CalendarEntry>()

    override fun getAll(): List<CalendarEntry> = items.values.toList()

    override fun getById(id: String): CalendarEntry? = items[id]

    override fun upsert(entry: CalendarEntry) {
        items[entry.id] = entry
    }

    override fun deleteById(id: String): Boolean = items.remove(id) != null
}
