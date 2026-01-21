package com.bandaid.app.domain.repository

import com.bandaid.app.domain.model.Medicine

interface MedicineRepository {
    fun getAll(): List<Medicine>
    fun getById(id: String): Medicine?
    fun upsert(medicine: Medicine)
    fun deleteById(id: String): Boolean
}
