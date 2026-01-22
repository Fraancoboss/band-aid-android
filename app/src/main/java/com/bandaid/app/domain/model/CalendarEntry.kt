/*
 * Responsibility:
 * - Immutable domain entity representing an expected dose occurrence.
 * - Holds expected time and reference IDs only.
 * - Completion is derived in UI by comparing against DoseLog in v0.1.
 * - Uses java.time types (minSdk 26) to avoid external time libraries.
 * Layer: domain (model).
 * Scope: stable for v0.1.
 *
 * Responsabilidad:
 * - Entidad de dominio inmutable que representa una ocurrencia esperada.
 * - Contiene fecha/hora esperada e IDs de referencia.
 * - La completitud se deriva en UI comparando con DoseLog en v0.1.
 * - Usa tipos java.time (minSdk 26) para evitar librerias externas de tiempo.
 * Capa: domain (modelo).
 * Alcance: estable para v0.1.
 */
package com.bandaid.app.domain.model

import java.time.LocalDateTime

data class CalendarEntry(
    val id: String,
    val medicineId: String,
    val scheduleId: String,
    val expectedAt: LocalDateTime,
    val isCompleted: Boolean
)
