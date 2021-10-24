package eu.rajniak.cat

import eu.rajniak.cat.data.FakeData
import kotlinx.coroutines.flow.first
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CatsViewModelTest {

    private lateinit var viewModel: CatsViewModel

    @BeforeTest
    fun setUp() {
        viewModel = CatsViewModel()
    }

    @Test
    fun testCategorySelectionChange() = runBlockingTest {
        val categoryId = 42
        assertFalse { viewModel.disabledCategories.first().contains(categoryId) }

        viewModel.onCategoryChecked(categoryId, false)

        assertTrue { viewModel.disabledCategories.first().contains(categoryId) }
    }

    @Test
    fun testMimeTypeSelectionChange() = runBlockingTest {
        val mimeTypeId = 42
        assertFalse { viewModel.disabledMimeTypes.first().contains(mimeTypeId) }

        viewModel.onMimeTypeChecked(mimeTypeId, false)

        assertTrue { viewModel.disabledMimeTypes.first().contains(mimeTypeId) }
    }

    // TODO: once we use don't need FakeData anymore,
    //  replace static list with list created in test,
    //  so it is visible why items are filtered
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
