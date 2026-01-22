/*
 * Responsibility:
 * - In-memory store for Medicine entities.
 * - Provides minimal CRUD without persistence or threading.
 * - Does NOT validate data or enforce business rules.
 * Layer: data (local, in-memory).
 * Scope: demo-only for v0.1.
 *
 * Responsabilidad:
 * - Almacen in-memory para entidades Medicine.
 * - Provee CRUD minimo sin persistencia ni hilos.
 * - NO valida datos ni aplica reglas de negocio.
 * Capa: data (local, in-memory).
 * Alcance: demo para v0.1.
 */
package com.bandaid.app.data.local

import com.bandaid.app.domain.model.Medicine
import com.bandaid.app.domain.repository.MedicineRepository

class InMemoryMedicineRepository : MedicineRepository {
    private val items = LinkedHashMap<String, Medicine>()

    override fun getAll(): List<Medicine> = items.values.toList()

    override fun getById(id: String): Medicine? = items[id]

    override fun upsert(medicine: Medicine) {
        items[medicine.id] = medicine
    }

    override fun deleteById(id: String): Boolean = items.remove(id) != null
}
