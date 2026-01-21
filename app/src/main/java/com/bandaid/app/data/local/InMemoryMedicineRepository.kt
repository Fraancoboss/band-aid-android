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
