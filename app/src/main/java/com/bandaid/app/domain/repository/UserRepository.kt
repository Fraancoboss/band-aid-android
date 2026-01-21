package com.bandaid.app.domain.repository

import com.bandaid.app.domain.model.User

interface UserRepository {
    fun getAll(): List<User>
    fun getById(id: String): User?
    fun upsert(user: User)
    fun deleteById(id: String): Boolean
}
