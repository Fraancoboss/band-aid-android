/*
 * Responsibility:
 * - In-memory store for extra medicine UI metadata (e.g., duration text).
 * - Keeps non-domain fields local and optional in v0.1.x.
 * - Does NOT persist or validate data.
 * Layer: data (local, in-memory).
 * Scope: demo-only for v0.1.x.
 *
 * Responsabilidad:
 * - Almacen in-memory para metadatos UI de medicina (ej: duracion).
 * - Mantiene campos no-dominio locales y opcionales en v0.1.x.
 * - NO persiste ni valida datos.
 * Capa: data (local, in-memory).
 * Alcance: demo para v0.1.x.
 */
package com.bandaid.app.data.local

class InMemoryMedicineMetaStore {
    private val durationByMedicineId = LinkedHashMap<String, String>()

    fun getDuration(medicineId: String): String? = durationByMedicineId[medicineId]

    fun setDuration(medicineId: String, duration: String?) {
        if (duration.isNullOrBlank()) {
            durationByMedicineId.remove(medicineId)
        } else {
            durationByMedicineId[medicineId] = duration
        }
    }
}
