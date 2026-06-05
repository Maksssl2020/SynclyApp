package pl.skomunikacja.synclyapp.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import pl.skomunikacja.synclyapp.model.post.Post
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiPostsService {

    @GET("api/v1/posts/android-app/user/{userId}")
    suspend fun getUserPostsByUserId(
        @Path("userId") userId: Long
    ): List<Post>

    @GET("api/v1/posts/search")
    suspend fun searchPosts(
        @Query("query") query: String
    ): List<Post>

    @Headers("Accept: application/json")
    @POST("api/v1/shared-posts/android-app/share")
    suspend fun sharePost(
        @Query("userId") userId: Long,
        @Query("postId") postId: Long
    ): Response<Unit>

    @Headers("Accept: application/json")
    @DELETE("api/v1/shared-posts/android-app/unshare")
    suspend fun unsharePost(
        @Query("userId") userId: Long,
        @Query("postId") postId: Long
    ): Response<Unit>

    @GET("api/v1/posts/android-app/user/dashboard/{userId}")
    suspend fun getForYouFeed(
        @Path("userId") userId: Long,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): List<Post>

    @GET("api/v1/posts/android-app/user/{userId}/feed/following")
    suspend fun getFollowingFeed(
        @Path("userId") userId: Long,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): List<Post>

    @Multipart
    @POST("api/v1/posts/android-app/create/{userId}")
    suspend fun createPost(
        @Path("userId") userId: Long,
        @Part("data") data: RequestBody,
        @Part files: List<MultipartBody.Part>? = null
    ): Response<Unit>
}