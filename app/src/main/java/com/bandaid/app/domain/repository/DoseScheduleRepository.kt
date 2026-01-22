/*
 * Responsibility:
 * - Contract for accessing DoseSchedule data.
 * - Defines minimal CRUD for v0.1.
 * - Does NOT generate CalendarEntry records.
 * Layer: domain (repository contract).
 * Scope: stable for v0.1.
 *
 * Responsabilidad:
 * - Contrato para acceder a datos de DoseSchedule.
 * - Define CRUD minimo para v0.1.
 * - NO genera registros de CalendarEntry.
 * Capa: domain (contrato de repositorio).
 * Alcance: estable para v0.1.
 */
package com.bandaid.app.domain.repository

import com.bandaid.app.domain.model.DoseSchedule

interface DoseScheduleRepository {
    fun getAll(): List<DoseSchedule>
    fun getById(id: String): DoseSchedule?
    fun upsert(schedule: DoseSchedule)
    fun deleteById(id: String): Boolean
}
