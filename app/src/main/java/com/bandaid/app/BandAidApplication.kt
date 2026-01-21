package com.bandaid.app

import android.app.Application
import com.bandaid.app.di.AppContainer

class BandAidApplication : Application() {
    val appContainer: AppContainer by lazy { AppContainer() }
}
