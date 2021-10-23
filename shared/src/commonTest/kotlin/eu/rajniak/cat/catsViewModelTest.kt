package eu.rajniak.cat

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
        assertFalse { viewModel.categorySelection.first()[categoryId] == true }

        viewModel.onCategoryChecked(categoryId, true)

        assertTrue { viewModel.categorySelection.first()[categoryId] == true }
    }

    @Test
    fun testMimeTypeSelectionChange() = runBlockingTest {
        val mimeTypeId = 42
        assertFalse { viewModel.mimeTypeSelection.first()[mimeTypeId] == true }

        viewModel.onMimeTypeChecked(mimeTypeId, true)

        assertTrue { viewModel.mimeTypeSelection.first()[mimeTypeId] == true }
    }
}
