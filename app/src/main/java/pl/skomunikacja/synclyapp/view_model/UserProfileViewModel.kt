package pl.skomunikacja.synclyapp.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.skomunikacja.synclyapp.config.RetrofitClient
import pl.skomunikacja.synclyapp.helpers.ApiFollowsHelper
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

    private val _userProfile = MutableStateFlow<UserProfileData?>(null)
    val userProfile = _userProfile.asStateFlow()

    private val _userFollowedUsers = MutableStateFlow(emptyList<UserProfileData>())
    val userFollowedUsers = _userFollowedUsers.asStateFlow()

    private val _userLikedProfilesIds = MutableStateFlow(emptyList<Long>())
    val userLikedProfilesIds = _userLikedProfilesIds.asStateFlow()

    private val _userProfilePosts = MutableStateFlow<List<Post>>(emptyList())
    val userProfilePosts = _userProfilePosts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadUserData(userId: Long) {
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

    fun loadAuthenticatedUserData(userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val followedUsersByUserId = apiFollowsHelper.getFollowedUsersByUserId(userId)
                val userLikedProfiles = apiLikesHelper.getUserLikedProfilesIds(userId)

                _userFollowedUsers.value = followedUsersByUserId
                _userLikedProfilesIds.value = userLikedProfiles
            } finally {
                _isLoading.value = false
            }
        }
    }
}