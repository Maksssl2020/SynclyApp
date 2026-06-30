package pl.skomunikacja.synclyapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.helpers.PostCollectionsManager
import pl.skomunikacja.synclyapp.model.PostCollection
import pl.skomunikacja.synclyapp.model.UserProfileData
import pl.skomunikacja.synclyapp.model.post.Post
import pl.skomunikacja.synclyapp.model.post.SharedPost
import pl.skomunikacja.synclyapp.ui.components.DashboardPostCard
import pl.skomunikacja.synclyapp.ui.components.ProfileHeader
import pl.skomunikacja.synclyapp.ui.components.SharedPostCard
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Black400
import pl.skomunikacja.synclyapp.ui.theme.Gray300
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.view_model.UserProfileViewModel

@Composable
fun UserProfileScreen(
    userId: Long,
    viewModel: UserProfileViewModel = viewModel()
) {
    val authenticationData by ApplicationManager.authenticationData.collectAsStateWithLifecycle()
    val userPostCollections by PostCollectionsManager.userPostCollections.collectAsStateWithLifecycle()

    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val userPosts by viewModel.userProfilePosts.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Posts", "Shared")

    LaunchedEffect(userId, authenticationData?.userId) {
        viewModel.loadProfileScreen(
            profileOwnerId = userId,
            authenticatedUserId = authenticationData?.userId
        )
    }

    when {
        isLoading -> FullScreenLoading()
        error != null -> FullScreenError(error!!)
        userProfile == null -> FullScreenError("User not found")

        else -> ProfileContent(
            userProfile = userProfile!!,
            userProfilePosts = userPosts,
            selectedTab = selectedTab,
            onTabChange = { selectedTab = it },
            tabs = tabs,
            isOwnProfile = authenticationData?.userId == userId,
            userPostCollections = userPostCollections,
            viewModel = viewModel
        )
    }
}

@Composable
fun ProfileContent(
    userProfile: UserProfileData,
    userProfilePosts: List<Post>,
    selectedTab: Int,
    onTabChange: (index: Int) -> Unit,
    tabs: List<String>,
    isOwnProfile: Boolean,
    userPostCollections: List<PostCollection>,
    viewModel: UserProfileViewModel
) {
    val sharedPosts = remember {
        listOf<SharedPost>()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Black400),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ProfileHeader(
                userProfile = userProfile,
                isOwnProfile = isOwnProfile,
                viewModel = viewModel
            )
        }

        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Black300,
                contentColor = White100,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Teal100
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { onTabChange(index) },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTab == index) Teal100 else Gray300,
                                fontWeight = if (selectedTab == index) FontWeight.Medium else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }

        when (selectedTab) {
            0 -> {
                if (userProfilePosts.isNotEmpty()) {
                    items(userProfilePosts) { post ->
                        DashboardPostCard(
                            post = post,
                            userPostCollections = userPostCollections
                        )
                    }
                } else {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(40.dp).border(2.dp, Teal100, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                            Text(
                                text = "User has no posts...",
                                fontSize = 15.sp,
                                color = Teal100,
                            )
                        }
                    }
                }
            }
            1 -> {
                if (sharedPosts.isNotEmpty()) {
                    items(sharedPosts) { sharedPost ->
                        SharedPostCard(
                            sharedPost = sharedPost,
                            userPostCollections = userPostCollections
                        )
                    }
                } else {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(40.dp).border(2.dp, Teal100, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                            Text(
                                text = "User has no shared posts.",
                                fontSize = 15.sp,
                                color = Teal100
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FullScreenLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun FullScreenError(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
    }
}


