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
