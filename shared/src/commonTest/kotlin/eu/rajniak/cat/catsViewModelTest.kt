package eu.rajniak.cat

import kotlinx.coroutines.flow.first
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

// TODO: once we use don't need FakeData anymore,
//  replace static list with list created in test,
//  so it is visible why items are filtered
class CatsViewModelTest {

    private lateinit var viewModel: CatsViewModel

    @BeforeTest
    fun setUp() {
        viewModel = CatsViewModel()
    }

    @Test
    fun testCategorySelectionChange() = runBlockingTest {
        val categoryId = 1
        assertTrue { viewModel.categories.first()[0].enabled }

        viewModel.onCategoryChecked(categoryId, false)

        assertFalse { viewModel.categories.first()[0].enabled }
    }

    @Test
    fun testMimeTypeSelectionChange() = runBlockingTest {
        val mimeTypeId = 1
        assertTrue { viewModel.mimeTypes.first()[0].enabled }

        viewModel.onMimeTypeChecked(mimeTypeId, false)

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
