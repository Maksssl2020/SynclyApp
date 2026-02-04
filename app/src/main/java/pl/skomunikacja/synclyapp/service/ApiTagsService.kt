package pl.skomunikacja.synclyapp.service

import pl.skomunikacja.synclyapp.model.Tag
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiTagsService {

    @GET("api/v1/tags/search")
    suspend fun searchTags(
        @Query("query") query: String
    ): List<Tag>

    @GET("api/v1/tags/android-app")
    suspend fun getAllTags(): List<Tag>

}