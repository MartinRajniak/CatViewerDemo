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

    @JvmStatic
    val MIME_TYPE_GIF = MimeType(
        id = 1,
        name = "gif"
    )

    @JvmStatic
    val MIME_TYPE_JPG = MimeType(
        id = 2,
        name = "jpg"
    )

    @JvmStatic
    val MIME_TYPE_PNG = MimeType(
        id = 3,
        name = "png"
    )

    val categories = listOf(
        CATEGORY_HATS,
        CATEGORY_SPACE
    )

    val mimeTypes = listOf(
        MIME_TYPE_GIF,
        MIME_TYPE_JPG,
        MIME_TYPE_PNG
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
                    categories = listOf(CATEGORY_HATS, CATEGORY_SPACE, null)
                        .shuffled()
                        .take(2)
                        .filterNotNull()
                )
            )
        }
        return result
    }
}
