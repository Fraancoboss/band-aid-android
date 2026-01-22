/*
 * Responsibility:
 * - In-memory store for user profile setup data.
 * - Provides a single profile snapshot for v0.1.x.
 * - Does NOT persist or validate data.
 * Layer: data (local, in-memory).
 * Scope: demo-only for v0.1.x.
 *
 * Responsabilidad:
 * - Almacen in-memory para datos de configuracion de usuario.
 * - Provee un solo perfil para v0.1.x.
 * - NO persiste ni valida datos.
 * Capa: data (local, in-memory).
 * Alcance: demo para v0.1.x.
 */
package com.bandaid.app.data.local

data class UserProfile(
    val age: Int,
    val weight: Float,
    val height: Float,
    val sex: String,
    val diseases: String?
)

class InMemoryUserProfileStore {
    private var profile: UserProfile? = null

    fun getProfile(): UserProfile? = profile

    fun saveProfile(profile: UserProfile) {
        this.profile = profile
    }
}
