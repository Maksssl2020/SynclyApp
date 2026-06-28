package pl.skomunikacja.synclyapp.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.skomunikacja.synclyapp.config.RetrofitClient
import pl.skomunikacja.synclyapp.helpers.ApiFollowsHelper
import pl.skomunikacja.synclyapp.helpers.ApiPostsHelper
import pl.skomunikacja.synclyapp.helpers.ApiTagsHelper
import pl.skomunikacja.synclyapp.helpers.ApiUsersHelper
import pl.skomunikacja.synclyapp.model.Tag
import pl.skomunikacja.synclyapp.model.UserData
import pl.skomunikacja.synclyapp.model.UserProfileData
import pl.skomunikacja.synclyapp.model.post.Post

class SearchViewModel : ViewModel() {
    private val apiFollowsHelper = ApiFollowsHelper(RetrofitClient.apiFollowsService)
    private val apiPostsHelper = ApiPostsHelper(RetrofitClient.apiPostsService)
    private val apiUsersHelper = ApiUsersHelper(RetrofitClient.apiUsersService)
    private val apiTagsHelper = ApiTagsHelper(RetrofitClient.apiTagsService)

    private val _searchedPosts = MutableStateFlow(emptyList<Post>())
    val searchedPosts = _searchedPosts.asStateFlow()

    private val _searchedUsers = MutableStateFlow(emptyList<UserData>())
    val searchedUsers = _searchedUsers.asStateFlow()

    private val _searchedTags = MutableStateFlow(emptyList<Tag>())
    val searchedTags = _searchedTags.asStateFlow()

    private val _followedUsers = MutableStateFlow(emptyList<UserProfileData>())
    val followedUsers = _followedUsers.asStateFlow()

    fun fetchFollowedUsers(userId: Long) {
        viewModelScope.launch {
            val followedUsersByUserId = apiFollowsHelper.getFollowedUsersByUserId(userId)
            _followedUsers.value = followedUsersByUserId
        }
    }

    fun toggleUserFollow(userProfileId: Long, userId: Long, isFollowed: Boolean) {
        viewModelScope.launch {
            if (isFollowed) {
                val unfollowedUser = apiFollowsHelper.unfollowUser(userProfileId, userId)
                if (unfollowedUser != null) {
                    _followedUsers.value = _followedUsers.value.filter {
                        it.userProfileId != userProfileId
                    }

                    _searchedUsers.value = _searchedUsers.value.map { user ->
                        if (user.userProfile.userProfileId == userProfileId) {
                            user.copy(
                                userProfile = user.userProfile.copy(
                                    followersCount = user.userProfile.followersCount - 1
                                )
                            )
                        } else user
                    }
                }
            } else {
                val followedUser = apiFollowsHelper.followUser(userProfileId, userId)
                if (followedUser != null) {
                    _followedUsers.value += followedUser

                    _searchedUsers.value = _searchedUsers.value.map { user ->
                        if (user.userProfile.userProfileId == userProfileId) {
                            user.copy(
                                userProfile = user.userProfile.copy(
                                    followersCount = user.userProfile.followersCount + 1
                                )
                            )
                        } else user
                    }
                }
            }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            if (!query.isEmpty()) {
                val searchPostsResult = apiPostsHelper.searchPosts(query)
                _searchedPosts.value = searchPostsResult

                val searchUsersResult = apiUsersHelper.searchUsers(query)
                _searchedUsers.value = searchUsersResult

                val searchTagsResult = apiTagsHelper.searchTags(query)
                _searchedTags.value = searchTagsResult
            }
        }
    }
}