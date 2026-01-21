package com.bandaid.app.ui.main

import com.bandaid.app.domain.model.Medicine

sealed class MainUiState {
    object Loading : MainUiState()
    object Empty : MainUiState()
    data class Content(val medicines: List<Medicine>) : MainUiState()
}
