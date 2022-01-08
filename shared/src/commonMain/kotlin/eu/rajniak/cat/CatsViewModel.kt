package eu.rajniak.cat

import co.touchlab.kermit.Logger
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
            try {
                catsStore.start()
            } catch (exception: Exception) {
                Logger.withTag(TAG).e("Failed to start.", exception)
                // TODO: do not continue and show whole screen error
            }
        }
    }

    fun onCategoryChecked(categoryId: Int, checked: Boolean) {
        sharedScope.launch(context = dispatcher) {
            try {
                catsStore.changeCategoryState(categoryId, checked)
            } catch (exception: Exception) {
                Logger.withTag(TAG).e("Failed to change category selection.", exception)
                // TODO: show temporary error message (UI should also show original value)
            }
        }
    }

    fun onMimeTypeChecked(mimeTypeId: Int, checked: Boolean) {
        sharedScope.launch(context = dispatcher) {
            try {
                catsStore.changeMimeTypeState(mimeTypeId, checked)
            } catch (exception: Exception) {
                Logger.withTag(TAG).e("Failed to change mime type selection.", exception)
                // TODO: show temporary error message (UI should also show original value)
            }
        }
    }

    // TODO: use multiplatform paging library instead (https://github.com/kuuuurt/multiplatform-paging)
    fun onScrolledToTheEnd() {
        Logger.withTag("CatsViewModel").i("onScrolledToTheEnd")
        if (loadingJob.get()?.isActive == true) {
            return
        }
        val job = sharedScope.launch(context = dispatcher) {
            try {
                catsStore.fetchMoreData()
            } catch (exception: Exception) {
                Logger.withTag(TAG).e("Failed to fetch more data.", exception)
                // TODO: how to unblock? either try later or let user manually refresh
            }
        }
        loadingJob.set(job)
    }

    companion object {

        private const val TAG = "CatsViewModel"
    }
}
