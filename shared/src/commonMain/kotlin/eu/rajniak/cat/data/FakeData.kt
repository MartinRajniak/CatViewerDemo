package eu.rajniak.cat.data

import kotlin.jvm.JvmStatic

object FakeData {

    private const val IMAGE_URL_JPG = "https://cdn2.thecatapi.com/images/YEw66vQ9A.jpg"
    private const val IMAGE_URL_GIF = "https://cdn2.thecatapi.com/images/497.gif"

    @JvmStatic
    val CATEGORY_HATS = Category(
        id = 1,
        name = "hats"
    )

    @JvmStatic
    val CATEGORY_SPACE = Category(
        id = 2,
        name = "space"
    )

    private val imageUrls = listOf(
        IMAGE_URL_JPG,
        IMAGE_URL_GIF
    )

    val cats = generateCats(50)

    fun generateCats(size: Int): List<Cat> {
        val result = mutableListOf<Cat>()
        repeat(size) {
            result.add(
                Cat(
                    url = imageUrls.random(),
                    categories = listOf(CATEGORY_HATS, CATEGORY_SPACE, null, null)
                        .shuffled()
                        .take(2)
                        .filterNotNull()
                )
            )
        }
        return result
    }
}
