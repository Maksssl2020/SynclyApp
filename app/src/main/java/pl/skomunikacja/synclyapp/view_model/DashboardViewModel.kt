package pl.skomunikacja.synclyapp.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.skomunikacja.synclyapp.config.RetrofitClient
import pl.skomunikacja.synclyapp.helpers.ApiPostCollectionsHelper
import pl.skomunikacja.synclyapp.helpers.ApiPostsHelper
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
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


    private var isLoading = false
    private var endReachedForYou = false
    private var endReachedFollowing = false

    fun loadDashboardPosts(userId: Long) {
        viewModelScope.launch {
            if (isLoading) return@launch
            isLoading = true

            try {
                if (_activeTab.value == DashboardTab.FOR_YOU && !endReachedForYou) {
                    val forYouFeed = apiPostsHelper.getForYouFeed(userId, _postsCurrentPageForYou.value, 10)
                    if (forYouFeed.isEmpty()) {
                        endReachedForYou = true
                    } else {
                        _dashboardForYouPosts.value += forYouFeed
                        _postsCurrentPageForYou.value += 1
                    }
                } else if (_activeTab.value == DashboardTab.FOLLOWED && !endReachedFollowing) {
                    val followingFeed = apiPostsHelper.getFollowingFeed(userId, _postsCurrentPageFollowing.value, 10)
                    if (followingFeed.isEmpty()) {
                        endReachedFollowing = true
                    } else {
                        _dashboardFollowedPosts.value += followingFeed
                        _postsCurrentPageFollowing.value += 1
                    }
                }
            } catch (e: Exception) {
                // obsługa błędu
            } finally {
                isLoading = false
            }
        }
    }


    fun fetchUserPostCollections(userId: Long) {
        viewModelScope.launch {
            val userAllPostCollections = apiPostCollectionsHelper.getUserAllPostCollections(userId)
            ApplicationManager.changeUserPostCollections(userAllPostCollections)
        }
    }

    fun switchTab(tab: DashboardTab) {
        _activeTab.value = tab
    }
}