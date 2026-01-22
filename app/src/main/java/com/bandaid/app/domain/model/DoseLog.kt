/*
 * Responsibility:
 * - Immutable domain entity representing a recorded dose.
 * - Holds timestamps and status only; no validation.
 * - Does NOT guarantee association to CalendarEntry.
 * - Uses java.time types (minSdk 26) to avoid external time libraries.
 * Layer: domain (model).
 * Scope: stable for v0.1.
 *
 * Responsabilidad:
 * - Entidad de dominio inmutable que representa una toma registrada.
 * - Contiene timestamps y estado; sin validacion.
 * - NO garantiza asociacion con CalendarEntry.
 * - Usa tipos java.time (minSdk 26) para evitar librerias externas de tiempo.
 * Capa: domain (modelo).
 * Alcance: estable para v0.1.
 */
package com.bandaid.app.domain.model

import java.time.LocalDateTime

data class DoseLog(
    val id: String,
    val medicineId: String,
    val scheduledTime: LocalDateTime?,
    val takenAt: LocalDateTime,
    val status: String,
    val notes: String?
)
