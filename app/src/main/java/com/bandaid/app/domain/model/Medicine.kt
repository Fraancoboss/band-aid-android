/*
 * Responsibility:
 * - Immutable domain entity representing a medicine.
 * - Holds data only; no validation or behavior.
 * - Does NOT carry persistence or UI concerns.
 * Layer: domain (model).
 * Scope: stable for v0.1.
 *
 * Responsabilidad:
 * - Entidad de dominio inmutable que representa un medicamento.
 * - Contiene solo datos; sin validacion ni comportamiento.
 * - NO incluye persistencia ni detalles de UI.
 * Capa: domain (modelo).
 * Alcance: estable para v0.1.
 */
package com.bandaid.app.domain.model

data class Medicine(
    val id: String,
    val name: String,
    val dosage: String,
    val instructions: String,
    val isActive: Boolean
)
