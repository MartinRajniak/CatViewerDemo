package eu.rajniak.cat.data

import com.benasher44.uuid.uuid4
import kotlinx.serialization.Serializable

@Serializable
data class Cat(
    val id: String = uuid4().toString(),
    val url: String,
    val categories: List<Category> = listOf()
)
