package pl.skomunikacja.synclyapp.view_model

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.skomunikacja.synclyapp.config.RetrofitClient
import pl.skomunikacja.synclyapp.helpers.ApiPostCollectionsHelper
import pl.skomunikacja.synclyapp.helpers.ApiPostsHelper
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.helpers.PostCollectionsManager
import pl.skomunikacja.synclyapp.model.DashboardTab
import pl.skomunikacja.synclyapp.model.post.Post

class DashboardViewModel  : ViewModel() {
    private val apiPostsHelper = ApiPostsHelper(RetrofitClient.apiPostsService)
    private val apiPostCollectionsHelper = ApiPostCollectionsHelper(RetrofitClient.apiPostCollectionsService)

    private val _activeTab = MutableStateFlow(DashboardTab.FOR_YOU)
    val activeTab = _activeTab.asStateFlow()

    private val _postsCurrentPageForYou = MutableStateFlow(0)
    private val _postsCurrentPageFollowing = MutableStateFlow(0)

    private val _dashboardForYouPosts = MutableStateFlow<List<Post>>(emptyList())
    val dashboardForYouPosts = _dashboardForYouPosts.asStateFlow()

    private val _dashboardFollowedPosts = MutableStateFlow<List<Post>>(emptyList())
    val dashboardFollowedPosts = _dashboardFollowedPosts.asStateFlow()

    private val _dashboardUserPosts = MutableStateFlow<List<Post>>(emptyList())
    val dashboardUserPosts = _dashboardUserPosts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _isFetchingPostCollections = MutableStateFlow(false)
    val isFetchingPostCollections = _isFetchingPostCollections.asStateFlow()

    private var endReachedForYou = false
    private var endReachedFollowing = false
    private var userPostsLoaded = false

    fun loadDashboardPosts(userId: Long) {
        viewModelScope.launch {
            if (_isLoading.value) return@launch

            when (_activeTab.value) {
                DashboardTab.FOR_YOU -> loadForYouPosts(userId)
                DashboardTab.FOLLOWED -> loadFollowedPosts(userId)
                DashboardTab.USER -> loadUserPosts(userId)
            }
        }
    }

    fun ensurePostCollectionsLoaded() {
        val authenticationData = ApplicationManager.authenticationData.value
        val userId = authenticationData?.userId ?: return

        if (PostCollectionsManager.userPostCollections.value.isNotEmpty()) {
            return
        }

        if (_isFetchingPostCollections.value) {
            return
        }

        viewModelScope.launch {
            _isFetchingPostCollections.value = true

            try {
                val collections =
                    apiPostCollectionsHelper.getUserAllPostCollections(userId)

                PostCollectionsManager.changeUserPostCollections(collections)
            } catch (ex: Exception) {
                Log.e("DashboardViewModel", "Failed to fetch post collections", ex)
            } finally {
                _isFetchingPostCollections.value = false
            }
        }
    }

    private suspend fun loadForYouPosts(userId: Long) {
        if (endReachedForYou) return

        _isLoading.value = true

        try {
            val forYouFeed = apiPostsHelper.getForYouFeed(
                userId = userId,
                offset = _postsCurrentPageForYou.value,
                limit = 10
            )

            if (forYouFeed.isEmpty()) {
                endReachedForYou = true
            } else {
                _dashboardForYouPosts.value += forYouFeed
                _postsCurrentPageForYou.value += 1
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            _isLoading.value = false
        }
    }

    private suspend fun loadFollowedPosts(userId: Long) {
        if (endReachedFollowing) return

        _isLoading.value = true

        try {
            val followingFeed = apiPostsHelper.getFollowingFeed(
                userId = userId,
                offset = _postsCurrentPageFollowing.value,
                limit = 10
            )

            if (followingFeed.isEmpty()) {
                endReachedFollowing = true
            } else {
                _dashboardFollowedPosts.value += followingFeed
                _postsCurrentPageFollowing.value += 1
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            _isLoading.value = false
        }
    }

    private suspend fun loadUserPosts(userId: Long) {
        if (userPostsLoaded) return

        _isLoading.value = true

        try {
            val userPosts = apiPostsHelper.getUserPosts(userId)
            _dashboardUserPosts.value = userPosts
            userPostsLoaded = true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            _isLoading.value = false
        }
    }


    fun switchTab(tab: DashboardTab) {
        _activeTab.value = tab
    }
}