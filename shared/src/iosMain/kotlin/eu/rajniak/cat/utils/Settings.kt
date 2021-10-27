package eu.rajniak.cat.utils

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import platform.Foundation.NSUserDefaults
import kotlin.native.concurrent.SharedImmutable

// TODO: how about function to create settings instead of value (to avoid annotation)
@SharedImmutable
actual val settings: FlowSettings =
    AppleSettings(NSUserDefaults.standardUserDefaults).toFlowSettings()
