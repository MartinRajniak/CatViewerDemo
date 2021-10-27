package eu.rajniak.cat

import eu.rajniak.cat.data.Cat
import eu.rajniak.cat.data.Category
import eu.rajniak.cat.data.CategoryModel
import eu.rajniak.cat.data.MimeType
import eu.rajniak.cat.data.MimeTypeModel
import eu.rajniak.cat.data.MimeTypesSource
import eu.rajniak.cat.data.MimeTypesSourceImpl
import eu.rajniak.cat.utils.AtomicInt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

interface CatsStore {
    val cats: Flow<List<Cat>>
    val categories: Flow<List<CategoryModel>>
    val mimeTypes: Flow<List<MimeTypeModel>>

    suspend fun start()
    suspend fun fetchMoreData()
    suspend fun changeCategoryState(categoryId: Int, enabled: Boolean)
    suspend fun changeMimeTypeState(mimeTypeId: Int, enabled: Boolean)
}

class CatsStoreImpl(
    private val catsApi: CatsApi = CatsApiImpl(),
    private val mimeTypesSource: MimeTypesSource = MimeTypesSourceImpl,
    private val settingsStorage: SettingsStorage = SettingsStorageImpl()
) : CatsStore {

    private val _disabledCategories: Flow<Set<Int>> = settingsStorage.disabledCategories

    private val _categories = MutableStateFlow(listOf<Category>())
    override val categories =
        _categories.combine(_disabledCategories) { categories, disabledCategories ->
            categories.map { category ->
                CategoryModel(
                    id = category.id,
                    name = category.name,
                    enabled = !disabledCategories.contains(category.id)
                )
            }
        }

    private val _disabledMimeTypes: Flow<Set<Int>> = settingsStorage.disabledMimeTypes

    // It would be nice if CatsAPI provided available MimeTypes as well,
    // so keeping it here in case they do :)
    private val _mimeTypes = MutableStateFlow(listOf<MimeType>())
    override val mimeTypes =
        _mimeTypes.combine(_disabledMimeTypes) { mimeTypes, disabledMimeTypes ->
            mimeTypes.map { mimeType ->
                MimeTypeModel(
                    id = mimeType.id,
                    name = mimeType.name,
                    enabled = !disabledMimeTypes.contains(mimeType.id)
                )
            }
        }

    override val cats = MutableStateFlow(listOf<Cat>())

    private var page = AtomicInt(1)

    override suspend fun start() {
        _mimeTypes.value = mimeTypesSource.mimeTypes
        _categories.value = catsApi.fetchCategories()
        cats.value = catsApi.fetchCats(page.get())
    }

    override suspend fun fetchMoreData() {
        val oldList = cats.value
        val newList = catsApi.fetchCats(page.addAndGet(1))
        cats.value = listOf(oldList, newList).flatten()
    }

    override suspend fun changeCategoryState(categoryId: Int, enabled: Boolean) {
        settingsStorage.changeCategoryState(categoryId, enabled)
    }

    override suspend fun changeMimeTypeState(mimeTypeId: Int, enabled: Boolean) {
        settingsStorage.changeMimeTypeState(mimeTypeId, enabled)
    }
}
