package eu.rajniak.cat

import eu.rajniak.cat.data.Cat
import eu.rajniak.cat.data.CategoryModel
import eu.rajniak.cat.data.FakeData
import eu.rajniak.cat.data.MimeTypeModel
import eu.rajniak.cat.utils.CommonFlow
import eu.rajniak.cat.utils.SharedViewModel
import eu.rajniak.cat.utils.asCommonFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CatsViewModel : SharedViewModel() {
    // TODO: persist to survive restart
    private val _disabledCategories = MutableStateFlow(emptySet<Int>())

    private val _categories = MutableStateFlow(FakeData.categories)
    val categories: CommonFlow<List<CategoryModel>> =
        _categories.combine(_disabledCategories) { categories, disabledCategories ->
            categories.map { category ->
                CategoryModel(
                    id = category.id,
                    name = category.name,
                    enabled = !disabledCategories.contains(category.id)
                )
            }
        }.asCommonFlow()

    // TODO: persist to survive restart
    private val _disabledMimeTypes = MutableStateFlow(emptySet<Int>())

    private val _mimeTypes = MutableStateFlow(FakeData.mimeTypes)
    val mimeTypes: CommonFlow<List<MimeTypeModel>> =
        _mimeTypes.combine(_disabledMimeTypes) { mimeTypes, disabledMimeTypes ->
            mimeTypes.map { mimeType ->
                MimeTypeModel(
                    id = mimeType.id,
                    name = mimeType.name,
                    enabled = !disabledMimeTypes.contains(mimeType.id)
                )
            }
        }.asCommonFlow()

    private val _cats = MutableStateFlow(FakeData.cats)
    val cats: CommonFlow<List<Cat>> =
        combine(
            _cats,
            categories,
            mimeTypes,
        ) { cats, categories, mimeTypes ->
            cats.filter { cat ->
                cat.categories.forEach { category ->
                    if (categories.first { it.id == category.id }.enabled == false) {
                        return@filter false
                    }
                }
                mimeTypes.forEach { mimeType ->
                    if (cat.url.endsWith(mimeType.name)) {
                        return@filter mimeType.enabled
                    }
                }
                true
            }
        }.asCommonFlow()

    private var loadingJob: Job? = null

    fun onCategoryChecked(categoryId: Int, checked: Boolean) {
        val oldSelection = _disabledCategories.value
        _disabledCategories.value = if (checked) {
            oldSelection.minus(categoryId)
        } else {
            oldSelection.plus(categoryId)
        }
    }

    fun onMimeTypeChecked(mimeTypeId: Int, checked: Boolean) {
        val oldSelection = _disabledMimeTypes.value
        _disabledMimeTypes.value = if (checked) {
            oldSelection.minus(mimeTypeId)
        } else {
            oldSelection.plus(mimeTypeId)
        }
    }

    // TODO: use multiplatform paging library instead (https://github.com/kuuuurt/multiplatform-paging)
    fun onScrolledToTheEnd() {
        if (loadingJob?.isActive == true) {
            return
        }
        loadingJob = sharedScope.launch {
            delay(2000L)

            // fetch more items
            val oldList = _cats.value
            _cats.value = listOf(oldList, FakeData.generateCats(50)).flatten()
        }
    }
}
