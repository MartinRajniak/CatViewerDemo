package eu.rajniak.cat

import com.russhwolf.settings.coroutines.FlowSettings
import eu.rajniak.cat.utils.settings
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface SettingsStorage {

    val disabledCategories: Flow<Set<Int>>
    val disabledMimeTypes: Flow<Set<Int>>

    suspend fun changeCategoryState(categoryId: Int, enabled: Boolean)
    suspend fun changeMimeTypeState(mimeTypeId: Int, enabled: Boolean)
}

class SettingsStorageImpl(
    private val settings: FlowSettings = settings()
) : SettingsStorage {

    companion object {

        private const val SETTINGS_DISABLED_CATEGORIES = "disabled_categories"
        private const val SETTINGS_DISABLED_MIME_TYPES = "disabled_mime_types"
    }

    override val disabledCategories: Flow<Set<Int>> =
        settings.getStringOrNullFlow(SETTINGS_DISABLED_CATEGORIES)
            .map {
                if (it == null) return@map emptySet<Int>()
                Json.decodeFromString(it)
            }

    override val disabledMimeTypes: Flow<Set<Int>> =
        settings.getStringOrNullFlow(SETTINGS_DISABLED_MIME_TYPES)
            .map {
                if (it == null) return@map emptySet<Int>()
                Json.decodeFromString(it)
            }

    init {
        // TODO: just for exercise - remove afterwards
        // Assignment requirement - filter out hats
        GlobalScope.launch {
            if (settings.hasKey(SETTINGS_DISABLED_CATEGORIES)) {
                // Already set
                return@launch
            }
            val hatsCategoryId = 1
            changeCategoryState(hatsCategoryId, false)
        }
    }

    override suspend fun changeCategoryState(categoryId: Int, enabled: Boolean) {
        val disabledCategoriesSerialized = settings.getStringOrNull(SETTINGS_DISABLED_CATEGORIES)
        val disabledCategories: MutableSet<Int> = if (disabledCategoriesSerialized == null) {
            mutableSetOf()
        } else {
            Json.decodeFromString(disabledCategoriesSerialized)
        }

        val result = if (enabled) {
            disabledCategories.minus(categoryId)
        } else {
            disabledCategories.plus(categoryId)
        }
        val resultSerialized = Json.encodeToString(result)

        settings.putString(SETTINGS_DISABLED_CATEGORIES, resultSerialized)
    }

    override suspend fun changeMimeTypeState(mimeTypeId: Int, enabled: Boolean) {
        val disabledMimeTypesSerialized = settings.getStringOrNull(SETTINGS_DISABLED_MIME_TYPES)
        val disabledMimeTypes: MutableSet<Int> = if (disabledMimeTypesSerialized == null) {
            mutableSetOf()
        } else {
            Json.decodeFromString(disabledMimeTypesSerialized)
        }

        val result = if (enabled) {
            disabledMimeTypes.minus(mimeTypeId)
        } else {
            disabledMimeTypes.plus(mimeTypeId)
        }
        val resultSerialized = Json.encodeToString(result)

        settings.putString(SETTINGS_DISABLED_MIME_TYPES, resultSerialized)
    }
}
