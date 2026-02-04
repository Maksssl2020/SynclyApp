package pl.skomunikacja.synclyapp.service

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiLikesService {

    @Headers("Accept: application/json")
    @POST("api/v1/likes/android-app/like/post")
    suspend fun likePost(
        @Query("userId") userId: Long,
        @Query("postId") postId: Long
    ): Response<Unit>

    @Headers("Accept: application/json")
    @DELETE("api/v1/likes/android-app/unlike/post")
    suspend fun unlikePost(
        @Query("userId") userId: Long,
        @Query("postId") postId: Long
    ): Response<Unit>

    @Headers("Accept: application/json")
    @POST("api/v1/likes/android-app/like/comment")
    suspend fun likeComment(
        @Query("userId") userId: Long,
        @Query("commentId") commentId: Long
    ): Response<Unit>

    @Headers("Accept: application/json")
    @DELETE("api/v1/likes/android-app/unlike/comment")
    suspend fun unlikeComment(
        @Query("userId") userId: Long,
        @Query("commentId") commentId: Long
    ): Response<Unit>

    @GET("api/v1/likes/user/liked-profiles")
    suspend fun getUserLikedProfilesIds(
        @Query("userId") userId: Long
    ): List<Long>
}