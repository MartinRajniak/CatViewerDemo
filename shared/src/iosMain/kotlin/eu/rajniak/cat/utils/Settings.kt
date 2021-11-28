package eu.rajniak.cat.utils

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import platform.Foundation.NSUserDefaults

actual fun settings(): FlowSettings =
    AppleSettings(NSUserDefaults.standardUserDefaults, true).toFlowSettings()
