/*
 * Responsibility:
 * - Immutable domain entity representing a recurring dose plan.
 * - Holds schedule data only; no generation logic.
 * - Does NOT create CalendarEntry records.
 * - Uses java.time types (minSdk 26) to avoid external time libraries.
 * Layer: domain (model).
 * Scope: stable for v0.1.
 *
 * Responsabilidad:
 * - Entidad de dominio inmutable que representa un plan de dosis recurrente.
 * - Contiene solo datos de horario; sin logica de generacion.
 * - NO crea registros de CalendarEntry.
 * - Usa tipos java.time (minSdk 26) para evitar librerias externas de tiempo.
 * Capa: domain (modelo).
 * Alcance: estable para v0.1.
 */
package com.bandaid.app.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class DoseSchedule(
    val id: String,
    val medicineId: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val timesOfDay: List<LocalTime>,
    val frequencyDays: Int
)
