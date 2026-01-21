package com.bandaid.app.data.local

import com.bandaid.app.domain.model.User
import com.bandaid.app.domain.repository.UserRepository

class InMemoryUserRepository : UserRepository {
    private val items = LinkedHashMap<String, User>()

    override fun getAll(): List<User> = items.values.toList()

    override fun getById(id: String): User? = items[id]

    override fun upsert(user: User) {
        items[user.id] = user
    }

    override fun deleteById(id: String): Boolean = items.remove(id) != null
}
