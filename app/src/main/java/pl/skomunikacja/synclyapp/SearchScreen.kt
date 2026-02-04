package pl.skomunikacja.synclyapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.skomunikacja.synclyapp.model.post.Post
import pl.skomunikacja.synclyapp.ui.components.DashboardPostCard
import pl.skomunikacja.synclyapp.ui.theme.Gray300
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.view_model.SearchViewModel
import androidx.core.graphics.toColorInt
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Search
import compose.icons.fontawesomeicons.solid.User
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.model.PostCollection
import pl.skomunikacja.synclyapp.model.Tag
import pl.skomunikacja.synclyapp.model.UserData
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Black400
import pl.skomunikacja.synclyapp.ui.theme.Gray500
import pl.skomunikacja.synclyapp.ui.theme.Teal100

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel()
) {
    val authenticationData by ApplicationManager.authenticationData.collectAsState()
    val userPostCollections by ApplicationManager.userPostCollections.collectAsState()

    var searchText by remember { mutableStateOf("") }

    var selectedTab by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }

    val postResults by viewModel.searchedPosts.collectAsState()
    val userResults by viewModel.searchedUsers.collectAsState()
    val tagResults by viewModel.searchedTags.collectAsState()

    val followedUsers by viewModel.followedUsers.collectAsState()
    val followedUserIds by remember(followedUsers) {
        derivedStateOf {
            followedUsers.map { it.userProfileId }
        }
    }
    var searchJob: Job? = null

    LaunchedEffect(Unit) {
        authenticationData?.let { viewModel.fetchFollowedUsers(it.userId) }
    }


    LaunchedEffect(searchText) {
        searchJob?.cancel()
        if (searchText.isNotBlank()) {
            searchJob = launch {
                delay(300)
                isLoading = true
                viewModel.search(searchText)
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black400)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Szukaj",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = White100,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = {
                    Text(
                        text = "Szukaj użytkowników, tagów, postów...",
                        color = Gray300
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.Search,
                        contentDescription = "Search",
                        tint = Gray300,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Teal100,
                    unfocusedBorderColor = Gray500,
                    focusedTextColor = White100,
                    unfocusedTextColor = White100,
                    cursorColor = Teal100
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }

        if (searchText.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.Search,
                        contentDescription = "Search",
                        tint = Gray300,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "Zacznij wpisywać, aby wyszukać",
                        color = Gray300,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        } else {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Black300,
                contentColor = White100,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Teal100
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            "Użytkownicy (${userResults.size})",
                            color = if (selectedTab == 0) Teal100 else Gray300
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            "Tagi (${tagResults.size})",
                            color = if (selectedTab == 1) Teal100 else Gray300
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Text(
                            "Posty (${postResults.size})",
                            color = if (selectedTab == 2) Teal100 else Gray300
                        )
                    }
                )
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Teal100)
                }
            } else {
                when (selectedTab) {
                    0 -> UserSearchResults(
                        userResults,
                        followedUserIds,
                        {userProfileId ->
                        authenticationData?.let {
                            viewModel.toggleUserFollow(userProfileId, it.userId, false)
                        }
                    },
                        { userProfileId ->
                            authenticationData?.let {
                                viewModel.toggleUserFollow(userProfileId, it.userId, true)
                            }
                        }
                    )
                    1 -> TagSearchResults(tagResults)
                    2 -> PostSearchResults(
                        postResults,
                        userPostCollections
                    )
                }
            }
        }
    }
}


@Composable
fun UserSearchResults(
    users: List<UserData>,
    followedUserIds: List<Long>,
    onFollow: (userProfileId: Long) -> Unit,
    onUnfollow: (userProfileId: Long) -> Unit
) {
    if (users.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Nie znaleziono użytkowników",
                color = Gray300,
                fontSize = 16.sp
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(users) { user ->
                val isFollowed = followedUserIds.contains(user.userProfile.userProfileId)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Navigate to user profile */ },
                    colors = CardDefaults.cardColors(containerColor = Black300),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Gray500),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = FontAwesomeIcons.Solid.User,
                                contentDescription = "Avatar",
                                tint = Gray300,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp)
                        ) {
                            Text(
                                text = user.userProfile.displayName,
                                color = White100,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "@${user.username}",
                                color = Gray300,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "${user.userProfile.followersCount} obserwujących",
                                color = Gray300,
                                fontSize = 12.sp
                            )
                        }

                        Button(
                            onClick = {
                                if (isFollowed) {
                                    onUnfollow(user.userProfile.userProfileId)
                                } else {
                                    onFollow(user.userProfile.userProfileId)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isFollowed) Gray500 else Teal100
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (isFollowed) "Obserwujesz" else "Obserwuj",
                                color = if (isFollowed) White100 else Black400,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TagSearchResults(tags: List<Tag>) {
    if (tags.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Nie znaleziono tagów",
                color = Gray300,
                fontSize = 16.sp
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tags) { tag ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Navigate to tag posts */ },
                    colors = CardDefaults.cardColors(containerColor = Black300),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(androidx.compose.ui.graphics.Color(tag.color.toColorInt()))
                        )

                        Text(
                            text = "#${tag.name}",
                            color = White100,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PostSearchResults(
    posts: List<Post>,
    userPostCollections: List<PostCollection>
) {
    if (posts.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Nie znaleziono postów",
                color = Gray300,
                fontSize = 16.sp
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(posts) { post ->
                DashboardPostCard(
                    post = post,
                    userPostCollections = userPostCollections
                )
            }
        }
    }
}
