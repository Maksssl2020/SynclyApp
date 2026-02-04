package pl.skomunikacja.synclyapp.helpers

import pl.skomunikacja.synclyapp.model.FriendRequestData
import pl.skomunikacja.synclyapp.model.FriendUserData
import pl.skomunikacja.synclyapp.service.ApiFriendsService

class ApiFriendsHelper(
    private val api: ApiFriendsService
) {
    suspend fun getUserFriendsList(userId: Long): List<FriendUserData> =
        api.getUserFriendsList(userId)

    suspend fun getUserPendingFriendRequests(userId: Long): List<FriendRequestData> =
        api.getUserPendingFriendRequests(userId)

    suspend fun getUserSentFriendRequests(userId: Long): List<FriendRequestData> =
        api.getUserSentFriendRequests(userId)

    suspend fun acceptFriendRequest(requestId: Long): Boolean =
        api.acceptFriendRequest(requestId).isSuccessful

    suspend fun declineFriendRequest(requestId: Long): Boolean =
        api.declineFriendRequest(requestId).isSuccessful

    suspend fun deleteSentFriendRequest(receiverId: Long, userId: Long): Boolean =
        api.deleteSentFriendRequest(receiverId, userId).isSuccessful
}