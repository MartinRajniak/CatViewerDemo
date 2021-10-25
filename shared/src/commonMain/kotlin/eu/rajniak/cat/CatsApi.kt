package eu.rajniak.cat

import eu.rajniak.cat.data.Cat
import eu.rajniak.cat.data.Category
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.observer.ResponseObserver
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.Url
import kotlinx.serialization.json.Json

interface CatsApi {
    suspend fun fetchCats(page: Int): List<Cat>
    suspend fun fetchCategories(): List<Category>
}

class CatsApiImpl : CatsApi {

    private val catsUrl =
        Url("https://api.thecatapi.com/v1/images/search")

    private val categoriesUrl =
        Url("https://api.thecatapi.com/v1/categories")

    private val client = HttpClient() {
        ResponseObserver { response ->
            println("HTTP status: ${response.status.value}")
            println("HTTP content: ${response.content}")
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    // TODO: find if there is a way how to tell that there are no more pages
    override suspend fun fetchCats(page: Int): List<Cat> {
        return client.get {
            url(catsUrl.toString())
            headers {
                append("x-api-key", BuildKonfig.api_key)
            }
            parameter("limit", 50)
            parameter("page", page)
            parameter("order", "ASC")
        }
    }

    override suspend fun fetchCategories(): List<Category> {
        return client.get {
            url(categoriesUrl.toString())
            headers {
                append("x-api-key", BuildKonfig.api_key)
            }
        }
    }
}
