package eu.rajniak.cat.android

import android.app.Application
import co.touchlab.kermit.ExperimentalKermitApi
import co.touchlab.kermit.Logger
import co.touchlab.kermit.crashlytics.CrashlyticsLogWriter
import co.touchlab.kermit.platformLogWriter

class CatViewerApp : Application() {

    @OptIn(ExperimentalKermitApi::class)
    override fun onCreate() {
        super.onCreate()
        // Setup crash crash reporting service and static log writer on app creation
        Logger.setLogWriters(platformLogWriter(), CrashlyticsLogWriter())
    }
}
