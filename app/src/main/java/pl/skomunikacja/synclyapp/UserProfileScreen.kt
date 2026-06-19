package pl.skomunikacja.synclyapp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.model.PostCollection
import pl.skomunikacja.synclyapp.model.UserProfileData
import pl.skomunikacja.synclyapp.model.post.Post
import pl.skomunikacja.synclyapp.model.post.SharedPost
import pl.skomunikacja.synclyapp.ui.components.DashboardPostCard
import pl.skomunikacja.synclyapp.ui.components.ProfileActionButtons
import pl.skomunikacja.synclyapp.ui.components.ProfileAvatar
import pl.skomunikacja.synclyapp.ui.components.ProfileStatCard
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
    val userPostCollections by ApplicationManager.userPostCollections.collectAsStateWithLifecycle()

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
                items(userProfilePosts) { post ->
                    DashboardPostCard(
                        post = post,
                        userPostCollections = userPostCollections
                    )
                }
            }
            1 -> {
                items(sharedPosts) { sharedPost ->
                    SharedPostCard(
                        sharedPost = sharedPost,
                        userPostCollections = userPostCollections
                    )
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

@Composable
fun ProfileHeader(
    userProfile: UserProfileData,
    isOwnProfile: Boolean,
    viewModel: UserProfileViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Teal100),
            contentAlignment = Alignment.Center
        ) {
            ProfileAvatar(
                base64Image = userProfile.avatar?.imageData,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                initials = userProfile.displayName.take(2).uppercase()
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = userProfile.displayName,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = White100
        )

        Text(
            text = "@${userProfile.username}",
            fontSize = 16.sp,
            color = Gray300,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = userProfile.bio,
            fontSize = 14.sp,
            color = White100,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp)
        )

        Text(
            text = "📍 ${userProfile.location}",
            fontSize = 14.sp,
            color = Gray300,
            modifier = Modifier.padding(top = 4.dp)
        )

        if (userProfile.website != null) {
            Text(
                text = "🌐 ${userProfile.website}",
                fontSize = 14.sp,
                color = Teal100,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileStatCard("Posts", userProfile.postsCount.toString())
            ProfileStatCard("Followers", userProfile.followersCount.toString())
            ProfileStatCard("Following", userProfile.followingCount.toString())
            ProfileStatCard("Profile Likes", userProfile.profileLikes.toString())
        }


        if (!isOwnProfile) {
            ProfileActionButtons(
                userProfile = userProfile,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun SharedPostCard(
    sharedPost: SharedPost,
    userPostCollections: List<PostCollection>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Black300),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = "Shared",
                    tint = Gray300,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${sharedPost.sharedBy.userProfile.displayName} shared",
                    color = Gray300,
                    fontSize = 14.sp
                )
            }

            DashboardPostCard(post = sharedPost.originalPost, userPostCollections = userPostCollections)
        }
    }
}