/*
 * Responsibility:
 * - Contract for accessing User data.
 * - Defines minimal CRUD for v0.1.
 * - Does NOT enforce single-user semantics.
 * Layer: domain (repository contract).
 * Scope: stable for v0.1.
 *
 * Responsabilidad:
 * - Contrato para acceder a datos de User.
 * - Define CRUD minimo para v0.1.
 * - NO impone semantica de un solo usuario.
 * Capa: domain (contrato de repositorio).
 * Alcance: estable para v0.1.
 */
package com.bandaid.app.domain.repository

import com.bandaid.app.domain.model.User

interface UserRepository {
    fun getAll(): List<User>
    fun getById(id: String): User?
    fun upsert(user: User)
    fun deleteById(id: String): Boolean
}
