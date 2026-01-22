/*
 * Responsibility:
 * - Immutable domain entity representing the user profile.
 * - Holds data only; no validation or behavior.
 * - Does NOT include authentication or accounts.
 * Layer: domain (model).
 * Scope: stable for v0.1.
 *
 * Responsabilidad:
 * - Entidad de dominio inmutable que representa el perfil de usuario.
 * - Contiene solo datos; sin validacion ni comportamiento.
 * - NO incluye autenticacion ni cuentas.
 * Capa: domain (modelo).
 * Alcance: estable para v0.1.
 */
package com.bandaid.app.domain.model

data class User(
    val id: String,
    val name: String,
    val timezone: String
)
