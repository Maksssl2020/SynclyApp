package pl.skomunikacja.synclyapp.service

import pl.skomunikacja.synclyapp.model.FriendRequestData
import pl.skomunikacja.synclyapp.model.FriendUserData
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiFriendsService {
    @GET("api/v1/friends/android-app/all/{userId}")
    suspend fun getUserFriendsList(
        @Path("userId") userId: Long
    ): List<FriendUserData>

    @GET("api/v1/friends/android-app/pending")
    suspend fun getUserPendingFriendRequests(
        @Query("userId") userId: Long
    ): List<FriendRequestData>

    @GET("api/v1/friends/android-app/sent")
    suspend fun getUserSentFriendRequests(
        @Query("userId") userId: Long
    ): List<FriendRequestData>

    @GET("api/v1/friends/android-app/request/status")
    suspend fun getFriendRequestStatus(
        @Query("requesterId") requesterId: Long,
        @Query("receiverId") receiverId: Long
    ): Response<String>

    @POST("api/v1/friends/accept/request")
    suspend fun acceptFriendRequest(
        @Query("requestId") requestId: Long
    ): Response<Unit>

    @POST("api/v1/friends/android-app/send/request")
    suspend fun sendFriendRequest(
        @Query("requesterId") requesterId: Long,
        @Query("receiverId") receiverId: Long
    ): Response<Unit>

    @DELETE("api/v1/friends/decline/request")
    suspend fun declineFriendRequest(
        @Query("requestId") requestId: Long
    ): Response<Unit>

    @DELETE("api/v1/friends/android-app/remove/request/{receiverId}")
    suspend fun deleteSentFriendRequest(
        @Path("receiverId") receiverId: Long,
        @Query("userId") userId: Long
    ): Response<Unit>

    @DELETE(value = "api/v1/friends/remove/friend")
    suspend fun deleteFriend(
        @Path("userId") userId: Long,
        @Query("friendId") friendId: Long
    ): Response<Unit>
}