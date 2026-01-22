/*
 * Responsibility:
 * - Contract for accessing CalendarEntry data.
 * - Defines minimal CRUD for v0.1.
 * - Does NOT infer entries from schedules.
 * Layer: domain (repository contract).
 * Scope: stable for v0.1.
 *
 * Responsabilidad:
 * - Contrato para acceder a datos de CalendarEntry.
 * - Define CRUD minimo para v0.1.
 * - NO infiere entradas desde schedules.
 * Capa: domain (contrato de repositorio).
 * Alcance: estable para v0.1.
 */
package com.bandaid.app.domain.repository

import com.bandaid.app.domain.model.CalendarEntry

interface CalendarEntryRepository {
    fun getAll(): List<CalendarEntry>
    fun getById(id: String): CalendarEntry?
    fun upsert(entry: CalendarEntry)
    fun deleteById(id: String): Boolean
}
