package com.bandaid.app.domain.model

import java.time.LocalDateTime

data class CalendarEntry(
    val id: String,
    val medicineId: String,
    val scheduleId: String,
    val expectedAt: LocalDateTime,
    val isCompleted: Boolean
)
