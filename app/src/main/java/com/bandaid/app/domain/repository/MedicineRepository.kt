/*
 * Responsibility:
 * - Contract for accessing Medicine data.
 * - Defines minimal CRUD for v0.1.
 * - Does NOT dictate persistence or threading.
 * Layer: domain (repository contract).
 * Scope: stable for v0.1.
 *
 * Responsabilidad:
 * - Contrato para acceder a datos de Medicine.
 * - Define CRUD minimo para v0.1.
 * - NO dicta persistencia ni threading.
 * Capa: domain (contrato de repositorio).
 * Alcance: estable para v0.1.
 */
package com.bandaid.app.domain.repository

import com.bandaid.app.domain.model.Medicine

interface MedicineRepository {
    fun getAll(): List<Medicine>
    fun getById(id: String): Medicine?
    fun upsert(medicine: Medicine)
    fun deleteById(id: String): Boolean
}
