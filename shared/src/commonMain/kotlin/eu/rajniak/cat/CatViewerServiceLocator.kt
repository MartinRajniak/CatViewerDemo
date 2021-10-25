package eu.rajniak.cat

object CatViewerServiceLocator {

    val catsStore by lazy {
        CatsStoreImpl()
    }
}
