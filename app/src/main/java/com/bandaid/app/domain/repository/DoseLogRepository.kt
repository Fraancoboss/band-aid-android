/*
 * Responsibility:
 * - Contract for accessing DoseLog data.
 * - Defines minimal CRUD for v0.1.
 * - Does NOT enforce CalendarEntry association.
 * Layer: domain (repository contract).
 * Scope: stable for v0.1.
 *
 * Responsabilidad:
 * - Contrato para acceder a datos de DoseLog.
 * - Define CRUD minimo para v0.1.
 * - NO aplica la asociacion con CalendarEntry.
 * Capa: domain (contrato de repositorio).
 * Alcance: estable para v0.1.
 */
package com.bandaid.app.domain.repository

import com.bandaid.app.domain.model.DoseLog

interface DoseLogRepository {
    fun getAll(): List<DoseLog>
    fun getById(id: String): DoseLog?
    fun upsert(log: DoseLog)
    fun deleteById(id: String): Boolean
}
