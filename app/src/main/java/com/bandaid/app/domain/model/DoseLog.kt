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
