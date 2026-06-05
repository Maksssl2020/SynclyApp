package pl.skomunikacja.synclyapp.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.skomunikacja.synclyapp.config.RetrofitClient
import pl.skomunikacja.synclyapp.helpers.ApiFollowsHelper
import pl.skomunikacja.synclyapp.helpers.ApiFriendsHelper
import pl.skomunikacja.synclyapp.helpers.ApiLikesHelper
import pl.skomunikacja.synclyapp.helpers.ApiPostsHelper
import pl.skomunikacja.synclyapp.helpers.ApiUsersHelper
import pl.skomunikacja.synclyapp.model.UserProfileData
import pl.skomunikacja.synclyapp.model.post.Post

class UserProfileViewModel : ViewModel() {
    private val apiUsersHelper = ApiUsersHelper(RetrofitClient.apiUsersService)
    private val apiPostsHelper = ApiPostsHelper(RetrofitClient.apiPostsService)
    private val apiFollowsHelper = ApiFollowsHelper(RetrofitClient.apiFollowsService)
    private val apiLikesHelper = ApiLikesHelper(RetrofitClient.apiLikesService)
    private val apiFriendsHelper = ApiFriendsHelper(RetrofitClient.apiFriendsService)

    private val _userProfile = MutableStateFlow<UserProfileData?>(null)
    val userProfile = _userProfile.asStateFlow()

    private val _authenticatedUserFollowedUsers = MutableStateFlow(emptyList<UserProfileData>())
    val authenticatedUserFollowedUsers = _authenticatedUserFollowedUsers.asStateFlow()

    private val _authenticatedUserLikedProfilesIds = MutableStateFlow(emptyList<Long>())
    val authenticatedUserLikedProfilesIds = _authenticatedUserLikedProfilesIds.asStateFlow()

    private val _authenticatedUserFriendStatus = MutableStateFlow("none")
    val authenticatedUserFriendStatus = _authenticatedUserFriendStatus.asStateFlow()

    private val _userProfilePosts = MutableStateFlow<List<Post>>(emptyList())
    val userProfilePosts = _userProfilePosts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadUserProfileData(userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val profileDeferred = async { apiUsersHelper.getUserProfile(userId) }
                val postsDeferred = async { apiPostsHelper.getUserPosts(userId) }

                _userProfile.value = profileDeferred.await()
                _userProfilePosts.value = postsDeferred.await()
            } catch (e: Exception) {
                _error.value = "Failed to load data: ${e.message}"
                Log.e("UserProfileVM", "Error loading data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadAuthenticatedUserData(userId: Long, userProfileOwnerId: Long) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val followedUsersByUserId = apiFollowsHelper.getFollowedUsersByUserId(userId)
                val userLikedProfiles = apiLikesHelper.getUserLikedProfilesIds(userId)
                val friendRequestStatus =
                    apiFriendsHelper.getFriendRequestStatus(userId, userProfileOwnerId)

                _authenticatedUserFollowedUsers.value = followedUsersByUserId
                _authenticatedUserLikedProfilesIds.value = userLikedProfiles
                _authenticatedUserFriendStatus.value = friendRequestStatus ?: "none"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun followUser(userProfileId: Long, userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
               val responseData = apiFollowsHelper.followUser(userProfileId, userId)

               if (responseData != null) {
                   _userProfile.update {
                       it?.copy(
                           followersCount = it.followersCount + 1
                       )
                   }

                   _authenticatedUserFollowedUsers.update { followedUsers ->
                       if (followedUsers.any { it.userProfileId == responseData.userProfileId }) {
                           followedUsers
                       } else {
                           followedUsers + responseData
                       }
                   }
               }
           } catch (ex: Exception) {
               _error.value = "Failed to follow user: ${ex.message}"
               Log.e("UserProfileVM", "Error loading data", ex)
           }finally {
               _isLoading.value = false
           }
        }
    }

    fun unfollowUser(userProfileId: Long, userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val responseData = apiFollowsHelper.unfollowUser(userProfileId, userId)

                if (responseData != null) {
                    _userProfile.update {
                        it?.copy(
                            followersCount =  (it.followersCount - 1).coerceAtLeast(0)
                        )
                    }

                    _authenticatedUserFollowedUsers.update { followedUsers ->
                        followedUsers.filterNot {
                            it.userProfileId == userProfileId
                        }
                    }
                }
            } catch (ex: Exception) {
                _error.value = "Failed to follow user: ${ex.message}"
                Log.e("UserProfileVM", "Error loading data", ex)
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun likeUserProfile(userId: Long, userProfileId: Long) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val isSuccess = apiLikesHelper.likeUserProfile(userId, userProfileId)

                if (isSuccess) {
                    _userProfile.update {
                        it?.copy(
                            profileLikes = it.profileLikes + 1
                        )
                    }

                    _authenticatedUserLikedProfilesIds.update { userLikedProfiles ->
                        if (userLikedProfiles.contains(userProfileId)) {
                            userLikedProfiles
                        } else {
                            userLikedProfiles + userProfileId
                        }
                    }
                }
            } catch (ex: Exception) {
                _error.value = "Failed to like user profile: ${ex.message}"
                Log.e("UserProfileVM", "Error loading data", ex)
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun unlikeUserProfile(userId: Long, userProfileId: Long) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val isSuccess = apiLikesHelper.unlikeUserProfile(userId, userProfileId)

                if (isSuccess) {
                    _userProfile.update {
                        it?.copy(
                            profileLikes =  (it.profileLikes - 1).coerceAtLeast(0)
                        )
                    }

                    _authenticatedUserLikedProfilesIds.update { userLikedProfiles ->
                        userLikedProfiles - userProfileId
                    }
                }
            } catch (ex: Exception) {
                _error.value = "Failed to unlike user profile: ${ex.message}"
                Log.e("UserProfileVM", "Error loading data", ex)
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun addUserToFriends(requesterId: Long, receiverId: Long) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val isSuccess = apiFriendsHelper.sendFriendRequest(requesterId, receiverId)

                if (isSuccess) {
                    _authenticatedUserFriendStatus.value = "pending"
                }
            } catch (ex: Exception) {
                _error.value = "Failed to add user to friends: ${ex.message}"
                Log.e("UserProfileVM", "Error loading data", ex)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cancelFriendRequest(requesterId: Long, receiverId: Long) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val isSuccess =
                    apiFriendsHelper.deleteSentFriendRequest(requesterId, receiverId)

                if (isSuccess) {
                    _authenticatedUserFriendStatus.value = "none"
                }
            } catch (ex: Exception) {
                _error.value = "Failed to delete friend request: ${ex.message}"
                Log.e("UserProfileVM", "Error loading data", ex)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteFriend(userId: Long, friendId: Long) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val isSuccess =
                    apiFriendsHelper.deleteFriends(userId, friendId)

                if (isSuccess) {
                    _authenticatedUserFriendStatus.value = "none"
                }
            } catch (ex: Exception) {
                _error.value = "Failed to delete friend: ${ex.message}"
                Log.e("UserProfileVM", "Error loading data", ex)
            } finally {
                _isLoading.value = false
            }
        }
    }
}