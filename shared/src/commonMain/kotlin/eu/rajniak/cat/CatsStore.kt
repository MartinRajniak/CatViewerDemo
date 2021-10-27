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
    fun changeCategoryState(categoryId: Int, enabled: Boolean)
    fun changeMimeTypeState(mimeTypeId: Int, enabled: Boolean)
}

class CatsStoreImpl(
    private val catsApi: CatsApi = CatsApiImpl(),
    private val mimeTypesSource: MimeTypesSource = MimeTypesSourceImpl
) : CatsStore {

    // TODO: persist to survive restart
    private val _disabledCategories = MutableStateFlow(emptySet<Int>())

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

    // TODO: persist to survive restart
    private val _disabledMimeTypes = MutableStateFlow(emptySet<Int>())

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

    override fun changeCategoryState(categoryId: Int, enabled: Boolean) {
        val oldSelection = _disabledCategories.value
        _disabledCategories.value = if (enabled) {
            oldSelection.minus(categoryId)
        } else {
            oldSelection.plus(categoryId)
        }
    }

    override fun changeMimeTypeState(mimeTypeId: Int, enabled: Boolean) {
        val oldSelection = _disabledMimeTypes.value
        _disabledMimeTypes.value = if (enabled) {
            oldSelection.minus(mimeTypeId)
        } else {
            oldSelection.plus(mimeTypeId)
        }
    }
}
