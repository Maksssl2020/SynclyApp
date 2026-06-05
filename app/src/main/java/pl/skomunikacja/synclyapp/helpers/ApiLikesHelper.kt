package pl.skomunikacja.synclyapp.helpers

import pl.skomunikacja.synclyapp.service.ApiLikesService

class ApiLikesHelper(
    private val api: ApiLikesService
) {

    suspend fun likeComment(userId: Long, commentId: Long): Boolean =
        api.likeComment(userId, commentId).isSuccessful

    suspend fun unlikeComment(userId: Long, commentId: Long): Boolean =
        api.unlikeComment(userId, commentId).isSuccessful

    suspend fun getUserLikedProfilesIds(userId: Long): List<Long> =
        api.getUserLikedProfilesIds(userId)

    suspend fun likePost(userId: Long, postId: Long): Boolean =
        api.likePost(userId, postId).isSuccessful

    suspend fun unlikePost(userId: Long, postId: Long): Boolean =
        api.unlikePost(userId, postId).isSuccessful

    suspend fun likeUserProfile(userId: Long, userProfileId: Long): Boolean =
        api.likeUserProfile(userId, userProfileId).isSuccessful

    suspend fun unlikeUserProfile(userId: Long, userProfileId: Long): Boolean =
        api.unlikeUserProfile(userId, userProfileId).isSuccessful
}