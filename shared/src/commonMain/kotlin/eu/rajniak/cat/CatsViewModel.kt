package eu.rajniak.cat

import eu.rajniak.cat.data.Cat
import eu.rajniak.cat.data.Category
import eu.rajniak.cat.data.FakeData
import eu.rajniak.cat.data.MimeType
import eu.rajniak.cat.utils.CommonFlow
import eu.rajniak.cat.utils.SharedViewModel
import eu.rajniak.cat.utils.asCommonFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class CatsViewModel : SharedViewModel() {
    private val _categories = MutableStateFlow(FakeData.categories)
    val categories: CommonFlow<List<Category>> = _categories.asCommonFlow()

    // TODO: persist to survive restart
    private val _categorySelection = MutableStateFlow(emptyMap<Int, Boolean>())
    val categorySelection: CommonFlow<Map<Int, Boolean>> = _categorySelection.asCommonFlow()

    private val _mimeTypes = MutableStateFlow(FakeData.mimeTypes)
    val mimeTypes: CommonFlow<List<MimeType>> = _mimeTypes.asCommonFlow()

    // TODO: persist to survive restart
    private val _mimeTypeSelection = MutableStateFlow(emptyMap<Int, Boolean>())
    val mimeTypeSelection: CommonFlow<Map<Int, Boolean>> = _mimeTypeSelection.asCommonFlow()

    private val _cats = MutableStateFlow(FakeData.cats)
    val cats: CommonFlow<List<Cat>> =
        combine(
            _cats,
            mimeTypes,
            categorySelection,
            mimeTypeSelection
        ) { cats, mimeTypes, categorySelection, mimeTypeSelection ->
            cats.filter { cat ->
                cat.categories.forEach { category ->
                    if (categorySelection[category.id] == false) {
                        return@filter false
                    }
                }
                mimeTypes.forEach { mimeType ->
                    if (cat.url.endsWith(mimeType.name)) {
                        return@filter mimeTypeSelection[mimeType.id] ?: true
                    }
                }
                true
            }
        }
            .asCommonFlow()

    fun onCategoryChecked(categoryId: Int, checked: Boolean) {
        val oldSelection = _categorySelection.value
        _categorySelection.value = oldSelection.plus(categoryId to checked)
    }

    fun onMimeTypeChecked(mimeTypeId: Int, checked: Boolean) {
        val oldSelection = _mimeTypeSelection.value
        _mimeTypeSelection.value = oldSelection.plus(mimeTypeId to checked)
    }
}
