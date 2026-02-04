package pl.skomunikacja.synclyapp.service

import pl.skomunikacja.synclyapp.model.PostCollection
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiPostCollectionsService {

    @GET("api/v1/post-collections/all-by-user/{userId}")
    suspend fun getUserAllPostCollections(
        @Path("userId") userId: Long
    ): List<PostCollection>

    @GET("api/v1/post-collections/android-app/{postCollectionId}")
    suspend fun getPostCollectionById(
        @Path("postCollectionId") postCollectionId: Long
    ): PostCollection

    @POST("api/v1/post-collections/save-post/{postCollectionId}")
    suspend fun savePostToCollection(
        @Path("postCollectionId") postCollectionId: Long,
        @Query("postId") postId: Long
    ): Response<Unit>

    @DELETE("api/v1/post-collections/unsave-post/by-post-collection/{postCollectionId}/{postId}")
    suspend fun unsavePostFromCollection(
        @Path("postCollectionId") postCollectionId: Long,
        @Path("postId") postId: Long
    ): Response<Unit>
}