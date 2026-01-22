/*
 * Responsibility:
 * - UI state model for the main screen.
 * - Represents loading, empty, and content states.
 * - Does NOT contain behavior or business rules.
 * Layer: UI (state model).
 * Scope: stable for v0.1.
 *
 * Responsabilidad:
 * - Modelo de estado de UI para la pantalla principal.
 * - Representa estados de carga, vacio y contenido.
 * - NO contiene comportamiento ni reglas de negocio.
 * Capa: UI (modelo de estado).
 * Alcance: estable para v0.1.
 */
package com.bandaid.app.ui.main

import com.bandaid.app.domain.model.Medicine

sealed class MainUiState {
    object Loading : MainUiState()
    object Empty : MainUiState()
    data class Content(val medicines: List<Medicine>) : MainUiState()
}
