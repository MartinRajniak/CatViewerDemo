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

// TODO: once we use don't need FakeData anymore,
//  replace static list with list created in test,
//  so it is visible why items are filtered
class CatsViewModelTest {

    companion object {
        private const val CATEGORY_ID = 1
        private const val MIME_TYPE_ID = 1
    }

    private lateinit var catsApi: FakeCatsApi
    private lateinit var mimeTypesSource: FakeMimeTypesSource
    private lateinit var viewModel: CatsViewModel

    @BeforeTest
    fun setUp() {
        catsApi = FakeCatsApi()
        catsApi.categories += Category(id = CATEGORY_ID, name = "Dummy")

        mimeTypesSource = FakeMimeTypesSource()
        mimeTypesSource.mimeTypes += MimeType(id = MIME_TYPE_ID, name = "Dummy")

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

        viewModel.onCategoryChecked(CATEGORY_ID, false)

        assertFalse { viewModel.categories.first()[0].enabled }
    }

    @Test
    fun testMimeTypeSelectionChange() = runBlockingTest {
        assertTrue { viewModel.mimeTypes.first()[0].enabled }

        viewModel.onMimeTypeChecked(MIME_TYPE_ID, false)

        assertFalse { viewModel.mimeTypes.first()[0].enabled }
    }

//    @Test
//    fun testCategoryCatFilter() = runBlockingTest {
//        assertTrue(viewModel.cats.first().size == 5)
//
//        viewModel.onCategoryChecked(FakeData.CATEGORY_HATS.id, false)
//
//        assertTrue(viewModel.cats.first().size == 1)
//    }
//
//    @Test
//    fun testMimeTypeCatFilter() = runBlockingTest {
//        assertTrue(viewModel.cats.first().size == 5)
//
//        viewModel.onMimeTypeChecked(FakeData.MIME_TYPE_GIF.id, false)
//
//        assertTrue(viewModel.cats.first().size == 3)
//    }
}

class FakeCatsApi : CatsApi {

    val cats = mutableListOf<Cat>()
    val categories = mutableListOf<Category>()

    override suspend fun fetchCats() = cats

    override suspend fun fetchCategories() = categories
}

class FakeMimeTypesSource : MimeTypesSource {

    override val mimeTypes = mutableListOf<MimeType>()
}
