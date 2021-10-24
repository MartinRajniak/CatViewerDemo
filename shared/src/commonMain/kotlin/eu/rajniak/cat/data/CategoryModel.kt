package eu.rajniak.cat.data

data class CategoryModel(
    val id: Int,
    val name: String,
    // TODO: var so that it can be used in iOS as a State
    //  see if we can avoid changing model directly
    var enabled: Boolean
)
