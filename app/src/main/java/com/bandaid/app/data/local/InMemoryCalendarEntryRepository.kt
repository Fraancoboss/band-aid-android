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
