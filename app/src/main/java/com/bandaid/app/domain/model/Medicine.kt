package com.bandaid.app.domain.model

data class Medicine(
    val id: String,
    val name: String,
    val dosage: String,
    val instructions: String,
    val isActive: Boolean
)
