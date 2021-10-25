package eu.rajniak.cat.data

import kotlin.jvm.JvmStatic

interface MimeTypesSource {

    val mimeTypes: List<MimeType>
}

object MimeTypesSourceImpl : MimeTypesSource {

    @JvmStatic
    private val MIME_TYPE_GIF = MimeType(
        id = 1,
        name = "gif"
    )

    @JvmStatic
    private val MIME_TYPE_JPG = MimeType(
        id = 2,
        name = "jpg"
    )

    @JvmStatic
    private val MIME_TYPE_PNG = MimeType(
        id = 3,
        name = "png"
    )

    override val mimeTypes = listOf(
        MIME_TYPE_GIF,
        MIME_TYPE_JPG,
        MIME_TYPE_PNG
    )
}
