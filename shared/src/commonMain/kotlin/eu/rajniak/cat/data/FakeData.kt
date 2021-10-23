package eu.rajniak.cat.data

import kotlin.jvm.JvmStatic

object FakeData {

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

    val cats = listOf(
        Cat(
            id = "1",
            url = "https://cdn2.thecatapi.com/images/YEw66vQ9A.jpg",
            categories = listOf(
                CATEGORY_HATS
            )
        ),
        Cat(
            id = "2",
            url = "https://cdn2.thecatapi.com/images/497.gif",
            categories = listOf(
                CATEGORY_HATS, CATEGORY_SPACE
            )
        ),
        Cat(
            id = "3",
            url = "https://cdn2.thecatapi.com/images/YEw66vQ9A.jpg",
            categories = listOf(
                CATEGORY_SPACE
            )
        ),
        Cat(
            id = "4",
            url = "https://cdn2.thecatapi.com/images/497.gif",
            categories = listOf(
                CATEGORY_HATS, CATEGORY_SPACE
            )
        ),
        Cat(
            id = "5",
            url = "https://cdn2.thecatapi.com/images/YEw66vQ9A.jpg",
            categories = listOf(
                CATEGORY_HATS
            )
        ),
    )

    val categories = listOf(
        CATEGORY_HATS,
        Category(
            id = 2,
            name = "space"
        )
    )
}
