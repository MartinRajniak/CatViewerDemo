package eu.rajniak.cat

import kotlinx.coroutines.Dispatchers

object CatViewerServiceLocator {

    val catsStore by lazy {
        CatsStoreImpl()
    }

    val defaultDispatcher = Dispatchers.Default
}
