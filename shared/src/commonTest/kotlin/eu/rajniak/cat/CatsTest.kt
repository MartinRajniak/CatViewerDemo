package eu.rajniak.cat

import eu.rajniak.cat.data.Cat
import eu.rajniak.cat.data.Category
import eu.rajniak.cat.data.MimeType
import eu.rajniak.cat.data.MimeTypesSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

// TODO: switch to AssertJ and JUnit for testing
//  this doesn't provide useful info
@ExperimentalCoroutinesApi
class CatsTest {

    companion object {
        private val CATEGORY_HAT = Category(
            id = 1,
            name = "Hat"
        )

        private val MIME_TYPE_GIF = MimeType(
            id = 1,
            name = "gif"
        )
    }

    private lateinit var dispatcher: TestDispatcher

    private lateinit var catsApi: FakeCatsApi
    private lateinit var mimeTypesSource: FakeMimeTypesSource
    private lateinit var settingsStorage: SettingsStorage
    private lateinit var viewModel: CatsViewModel

    @BeforeTest
    fun setUp() {
        catsApi = FakeCatsApi()
        catsApi.categories += CATEGORY_HAT
        catsApi.cats[1] = listOf(
            Cat(
                url = "Dummy.gif",
                categories = listOf(CATEGORY_HAT)
            ),
            Cat(
                url = "Dummy.png"
            )
        )
        catsApi.cats[2] = listOf(
            Cat(
                url = "Dummy.jpg"
            )
        )

        mimeTypesSource = FakeMimeTypesSource()
        mimeTypesSource.mimeTypes += MIME_TYPE_GIF

        settingsStorage = FakeSettingsStorage()

        dispatcher = StandardTestDispatcher()

        viewModel = CatsViewModel(
            catsStore = CatsStoreImpl(
                catsApi = catsApi,
                mimeTypesSource = mimeTypesSource,
                settingsStorage = settingsStorage
            ),
            dispatcher = dispatcher
        )
    }

    @Test
    fun testCategorySelectionChange() = runTest(dispatcher) {
        assertTrue { viewModel.categories.first().first { it.id == CATEGORY_HAT.id }.enabled }

        viewModel.onCategoryChecked(CATEGORY_HAT.id, false)

        assertFalse { viewModel.categories.first().first { it.id == CATEGORY_HAT.id }.enabled }
    }

    @Test
    fun testMimeTypeSelectionChange() = runTest(dispatcher) {
        assertTrue { viewModel.mimeTypes.first().first { it.id == MIME_TYPE_GIF.id }.enabled }

        viewModel.onMimeTypeChecked(MIME_TYPE_GIF.id, false)

        assertFalse { viewModel.mimeTypes.first().first { it.id == MIME_TYPE_GIF.id }.enabled }
    }

    @Test
    fun testCategoryCatFilter() = runTest(dispatcher) {
        assertEquals(2, viewModel.cats.first().size)

        viewModel.onCategoryChecked(CATEGORY_HAT.id, false)

        assertEquals(1, viewModel.cats.first().size)
    }

    @Test
    fun testMimeTypeCatFilter() = runTest(dispatcher) {
        assertEquals(2, viewModel.cats.first().size)

        viewModel.onMimeTypeChecked(MIME_TYPE_GIF.id, false)

        assertEquals(1, viewModel.cats.first().size)
    }

    @Test
    fun testPagination() = runTest(dispatcher) {
        assertEquals(2, viewModel.cats.first().size)

        viewModel.onScrolledToTheEnd()

        assertEquals(3, viewModel.cats.first().size)
    }
}

class FakeCatsApi : CatsApi {

    // key: page, value: Cat
    val cats = mutableMapOf<Int, List<Cat>>()
    val categories = mutableListOf<Category>()

    override suspend fun fetchCats(page: Int) = cats[page]!!

    override suspend fun fetchCategories() = categories
}

class FakeMimeTypesSource : MimeTypesSource {

    override val mimeTypes = mutableListOf<MimeType>()
}

// TODO: if we fake settings instead, we can use real storage with this logic in it
//  not needing to rewrite it here
class FakeSettingsStorage : SettingsStorage {

    override val disabledCategories = MutableStateFlow(setOf<Int>())
    override val disabledMimeTypes = MutableStateFlow(setOf<Int>())

    override suspend fun changeCategoryState(categoryId: Int, enabled: Boolean) {
        val oldValue = disabledCategories.value
        disabledCategories.value = if (enabled) {
            oldValue.minus(categoryId)
        } else {
            oldValue.plus(categoryId)
        }
    }

    override suspend fun changeMimeTypeState(mimeTypeId: Int, enabled: Boolean) {
        val oldValue = disabledMimeTypes.value
        disabledMimeTypes.value = if (enabled) {
            oldValue.minus(mimeTypeId)
        } else {
            oldValue.plus(mimeTypeId)
        }
    }
}
