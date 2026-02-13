/*
 * Responsibility:
 * - Application entry point that owns a single AppContainer instance.
 * - Exposes dependencies to UI without global singletons.
 * - Does NOT hold domain data or run background work.
 * Layer: wiring (app lifecycle).
 * Scope: stable for v0.1.
 *
 * Responsabilidad:
 * - Punto de entrada de la aplicacion que mantiene una unica instancia de AppContainer.
 * - Expone dependencias a la UI sin singletons globales.
 * - NO guarda datos de usuario ni ejecuta trabajo en segundo plano.
 * Capa: wiring (ciclo de vida de la app).
 * Alcance: estable para v0.1.
 */
package com.bandaid.app

import android.app.Application
import com.bandaid.app.di.AppContainer

class BandAidApplication : Application() {
    // WHY THIS DECISION:
    // Lazy init avoids DI frameworks and guarantees a single container per app process.
    //
    // POR QUE ESTA DECISION:
    // La inicialización diferida evita frameworks DI y garantiza un solo container por proceso.
    val appContainer: AppContainer by lazy { AppContainer() }
}
