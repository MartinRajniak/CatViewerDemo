package eu.rajniak.cat

import eu.rajniak.cat.data.Cat
import eu.rajniak.cat.data.Category
import eu.rajniak.cat.data.MimeType
import eu.rajniak.cat.data.MimeTypesSource
import eu.rajniak.cat.data.MimeTypesSourceImpl
import eu.rajniak.cat.utils.AtomicInt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface CatsStore {
    val cats: StateFlow<List<Cat>>
    val categories: StateFlow<List<Category>>
    val mimeTypes: StateFlow<List<MimeType>>

    suspend fun start()
    suspend fun fetchMoreData()
}

class CatsStoreImpl(
    private val catsApi: CatsApi = CatsApiImpl(),
    mimeTypesSource: MimeTypesSource = MimeTypesSourceImpl
) : CatsStore {

    override val cats = MutableStateFlow(listOf<Cat>())
    override val categories = MutableStateFlow(listOf<Category>())

    // It would be nice if CatsAPI provided available MimeTypes as well,
    // so keeping it here in case they do :)
    override val mimeTypes = MutableStateFlow(mimeTypesSource.mimeTypes)

    private var page = AtomicInt(1)

    override suspend fun start() {
        categories.value = catsApi.fetchCategories()
        cats.value = catsApi.fetchCats(page.get())
    }

    override suspend fun fetchMoreData() {
        val oldList = cats.value
        val newList = catsApi.fetchCats(page.addAndGet(1))
        cats.value = listOf(oldList, newList).flatten()
    }
}
