/*
 * Responsibility:
 * - In-memory store for User entities.
 * - Provides minimal CRUD without persistence or threading.
 * - Does NOT enforce single-user constraints.
 * Layer: data (local, in-memory).
 * Scope: demo-only for v0.1.
 *
 * Responsabilidad:
 * - Almacen in-memory para entidades User.
 * - Provee CRUD minimo sin persistencia ni hilos.
 * - NO impone restricciones de un solo usuario.
 * Capa: data (local, in-memory).
 * Alcance: demo para v0.1.
 */
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
