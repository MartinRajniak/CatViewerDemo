package eu.rajniak.cat

import eu.rajniak.cat.data.Cat
import eu.rajniak.cat.data.CategoryModel
import eu.rajniak.cat.data.MimeTypeModel
import eu.rajniak.cat.utils.AtomicReference
import eu.rajniak.cat.utils.CommonFlow
import eu.rajniak.cat.utils.SharedViewModel
import eu.rajniak.cat.utils.asCommonFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsStore: CatsStore = CatViewerServiceLocator.catsStore,
    private val dispatcher: CoroutineDispatcher = CatViewerServiceLocator.defaultDispatcher
) : SharedViewModel() {

    val categories: CommonFlow<List<CategoryModel>> = catsStore.categories.asCommonFlow()

    val mimeTypes: CommonFlow<List<MimeTypeModel>> = catsStore.mimeTypes.asCommonFlow()

    private val _cats = catsStore.cats
    val cats: CommonFlow<List<Cat>> =
        combine(
            _cats,
            categories,
            mimeTypes,
        ) { cats, categories, mimeTypes ->
            cats.filter { cat ->
                cat.categories.forEach { category ->
                    if (categories.firstOrNull { it.id == category.id }?.enabled == false) {
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

    // TODO: using AtomicReference
    //  because of issues with freezing state on iOS.
    //  Try figure out what is wrong (should be one thread access).
    private var loadingJob: AtomicReference<Job?> = AtomicReference(null)

    init {
        sharedScope.launch(context = dispatcher) {
            catsStore.start()
        }
    }

    fun onCategoryChecked(categoryId: Int, checked: Boolean) {
        sharedScope.launch(context = dispatcher) {
            catsStore.changeCategoryState(categoryId, checked)
        }
    }

    fun onMimeTypeChecked(mimeTypeId: Int, checked: Boolean) {
        sharedScope.launch(context = dispatcher) {
            catsStore.changeMimeTypeState(mimeTypeId, checked)
        }
    }

    // TODO: use multiplatform paging library instead (https://github.com/kuuuurt/multiplatform-paging)
    fun onScrolledToTheEnd() {
        if (loadingJob.get()?.isActive == true) {
            return
        }
        val job = sharedScope.launch(context = dispatcher) {
            catsStore.fetchMoreData()
        }
        loadingJob.set(job)
    }
}
