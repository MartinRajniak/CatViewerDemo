package eu.rajniak.cat

import eu.rajniak.cat.data.Cat
import eu.rajniak.cat.data.Category
import eu.rajniak.cat.data.MimeType
import eu.rajniak.cat.data.MimeTypesSource
import kotlinx.coroutines.flow.first
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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

    private lateinit var catsApi: FakeCatsApi
    private lateinit var mimeTypesSource: FakeMimeTypesSource
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

        viewModel = CatsViewModel(
            catsStore = CatsStoreImpl(
                catsApi = catsApi,
                mimeTypesSource = mimeTypesSource
            )
        )
    }

    @Test
    fun testCategorySelectionChange() = runBlockingTest {
        assertTrue { viewModel.categories.first()[0].enabled }

        viewModel.onCategoryChecked(CATEGORY_HAT.id, false)

        assertFalse { viewModel.categories.first()[0].enabled }
    }

    @Test
    fun testMimeTypeSelectionChange() = runBlockingTest {
        assertTrue { viewModel.mimeTypes.first()[0].enabled }

        viewModel.onMimeTypeChecked(MIME_TYPE_GIF.id, false)

        assertFalse { viewModel.mimeTypes.first()[0].enabled }
    }

    @Test
    fun testCategoryCatFilter() = runBlockingTest {
        assertTrue(viewModel.cats.first().size == 2)

        viewModel.onCategoryChecked(CATEGORY_HAT.id, false)

        assertTrue(viewModel.cats.first().size == 1)
    }

    @Test
    fun testMimeTypeCatFilter() = runBlockingTest {
        assertTrue(viewModel.cats.first().size == 2)

        viewModel.onMimeTypeChecked(MIME_TYPE_GIF.id, false)

        assertTrue(viewModel.cats.first().size == 1)
    }

    @Test
    fun testPagination() = runBlockingTest {
        assertTrue(viewModel.cats.first().size == 2)

        viewModel.onScrolledToTheEnd()

        assertTrue(viewModel.cats.first().size == 3)
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