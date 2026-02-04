package pl.skomunikacja.synclyapp.service

import pl.skomunikacja.synclyapp.model.UserProfileData
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiFollowsService {
    @GET("api/v1/follows/users/{userId}")
    suspend fun getFollowedUsersByUserId(
        @Path("userId") userId: Long
    ): List<UserProfileData>

    @POST("api/v1/follows/android-app/follow/user/{userProfileId}")
    suspend fun followUser(
        @Path("userProfileId") userProfileId: Long,
        @Query("userId") userId: Long
    ): UserProfileData?

    @DELETE("api/v1/follows/android-app/unfollow/user/{userProfileId}")
    suspend fun unfollowUser(
        @Path("userProfileId") userProfileId: Long,
        @Query("userId") userId: Long
    ): UserProfileData?
}