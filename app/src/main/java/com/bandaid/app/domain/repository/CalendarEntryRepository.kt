package com.bandaid.app.domain.repository

import com.bandaid.app.domain.model.CalendarEntry

interface CalendarEntryRepository {
    fun getAll(): List<CalendarEntry>
    fun getById(id: String): CalendarEntry?
    fun upsert(entry: CalendarEntry)
    fun deleteById(id: String): Boolean
}
