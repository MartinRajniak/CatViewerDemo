package eu.rajniak.cat.utils

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.startup.Initializer
import com.russhwolf.settings.coroutines.FlowSettings

private var appContext: Context? = null

private val Context.dataStore by preferencesDataStore(
    name = "settings"
)

actual fun settings(): FlowSettings =
    DataStoreSettings(
        appContext?.dataStore ?: throw IllegalStateException("Context not initialized")
    )

internal class SettingsInitializer : Initializer<Context> {
    override fun create(context: Context): Context =
        context.applicationContext.also { appContext = it }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
