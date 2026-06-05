package pl.skomunikacja.synclyapp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.ui.components.FriendCard
import pl.skomunikacja.synclyapp.ui.components.FriendRequestCard
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Black400
import pl.skomunikacja.synclyapp.ui.theme.Gray300
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.view_model.FriendsViewModel

@Composable
fun FriendsScreen(
    navController: NavHostController,
    viewModel: FriendsViewModel = viewModel()
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Friends", "Pending", "Sent")

    val authenticationData by ApplicationManager.authenticationData.collectAsState()
    val friends by viewModel.friends.collectAsState()
    val pendingRequests by viewModel.userPendingFriendRequests.collectAsState()
    val sentRequests by viewModel.userSentFriendRequests.collectAsState()

    LaunchedEffect(selectedTab) {
        if (selectedTab == 0) {
            authenticationData?.let { viewModel.fetchFriendsData(it.userId) }
        }
        if (selectedTab == 1) {
            authenticationData?.let { viewModel.fetchUserPendingFriendRequests(it.userId) }
        }
        if (selectedTab == 2) {
            authenticationData?.let { viewModel.fetchUserSentFriendRequests(it.userId) }
        }
    }

    var searchQuery by remember { mutableStateOf("") }
    val filteredFriends = friends.filter { friend ->
        friend.user.userProfile.displayName.contains(searchQuery, ignoreCase = true) ||
                friend.user.username.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black400)
            .padding(16.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Black300,
            contentColor = White100,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = Teal100
                )
            },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = when (index) {
                                0 -> title
                                1 -> title
                                2 -> title
                                else -> title
                            },
                            color = if (selectedTab == index) Teal100 else Gray300
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search for friends...", color = Gray300) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "search",
                            tint = Gray300
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Teal100,
                        unfocusedBorderColor = Gray300,
                        focusedTextColor = White100,
                        unfocusedTextColor = White100,
                        cursorColor = Teal100
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredFriends) { friend ->
                        FriendCard(
                            friend = friend,
                            onAvatarClick = {
                                navController.navigate("user_profile/${friend.user.userId}")
                            },
                            onCardClick = {
                                navController.navigate("user_profile/${friend.user.userId}")
                            },
                            onMoreClick = { /* TODO: Show options menu */ }
                        )
                    }
                }
            }
            1 -> {
                if (pendingRequests.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Brak oczekujących zaproszeń",
                            color = Gray300,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(sentRequests) { request ->
                            FriendRequestCard(
                                friendRequest = request,
                                isOutgoing = false,
                                onAccept = { requestId ->
                                    viewModel.acceptPendingFriendRequest(requestId, {}, {
                                        Toast.makeText(context, "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show()
                                    })
                               },
                                onReject = { requestId ->
                                    viewModel.declinePendingFriendRequest(requestId, {}, {
                                        Toast.makeText(context, "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show()
                                    })
                                },
                            )
                        }
                    }
                }
            }
            2 -> {
                if (sentRequests.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Brak wysłanych zaproszeń",
                            color = Gray300,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(sentRequests) { request ->
                            FriendRequestCard(
                                friendRequest = request,
                                isOutgoing = true,
                                onRemoveFriendRequest = { receiverId ->
                                    if (authenticationData != null) {
                                        viewModel.removeSentFriendRequest(receiverId, authenticationData!!.userId, {
                                            Toast.makeText(context, "Successfully removed friend request.", Toast.LENGTH_SHORT).show()
                                        }, {
                                            Toast.makeText(context, "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show()
                                        })
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}