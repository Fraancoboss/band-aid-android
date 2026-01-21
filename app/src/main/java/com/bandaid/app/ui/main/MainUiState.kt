package com.bandaid.app.ui.main

sealed class MainUiState {
    object Loading : MainUiState()
    object Empty : MainUiState()
    data class Content(val medicineCount: Int) : MainUiState()
}
