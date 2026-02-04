package pl.skomunikacja.synclyapp.helpers

import pl.skomunikacja.synclyapp.model.UserProfileData
import pl.skomunikacja.synclyapp.service.ApiFollowsService

class ApiFollowsHelper(
    private val api: ApiFollowsService
) {
    suspend fun getFollowedUsersByUserId(userId: Long): List<UserProfileData> =
        api.getFollowedUsersByUserId(userId)

    suspend fun followUser(userProfileId: Long, userId: Long): UserProfileData? =
        api.followUser(userProfileId, userId)

    suspend fun unfollowUser(userProfileId: Long, userId: Long): UserProfileData? =
        api.unfollowUser(userProfileId, userId)
}