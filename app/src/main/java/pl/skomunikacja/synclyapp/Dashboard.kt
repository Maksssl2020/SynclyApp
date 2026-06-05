package pl.skomunikacja.synclyapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.FastForward
import compose.icons.fontawesomeicons.solid.ObjectGroup
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.model.DashboardTab
import pl.skomunikacja.synclyapp.ui.components.DashboardPostCard
import pl.skomunikacja.synclyapp.ui.theme.Black200
import pl.skomunikacja.synclyapp.ui.theme.Black400
import pl.skomunikacja.synclyapp.ui.theme.Gray400
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.view_model.DashboardViewModel

@Composable
fun DashboardScreen(
    navController: NavHostController,
    dashboardViewModel: DashboardViewModel = viewModel(),
) {
    val authenticationData by ApplicationManager.authenticationData.collectAsState()
    val userPostCollections by ApplicationManager.userPostCollections.collectAsState()
    val activeTab by dashboardViewModel.activeTab.collectAsState()
    val forYouPosts by dashboardViewModel.dashboardForYouPosts.collectAsState()
    val followedPosts by dashboardViewModel.dashboardFollowedPosts.collectAsState()
    val listState = rememberLazyListState()

    val posts = if (activeTab == DashboardTab.FOR_YOU) forYouPosts else followedPosts

    LaunchedEffect(authenticationData != null && userPostCollections.isEmpty()) {
        dashboardViewModel.fetchUserPostCollections(authenticationData!!.userId)
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.layoutInfo.totalItemsCount }
            .collect { (firstVisible, totalItems) ->
                if (firstVisible + 5 >= totalItems) { // kiedy jesteśmy 5 elementów od końca
                    authenticationData?.userId?.let { dashboardViewModel.loadDashboardPosts(it) }
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black400)
    ) {
        Surface(
            color = Black200,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { dashboardViewModel.switchTab(DashboardTab.FOR_YOU) }
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            FontAwesomeIcons.Solid.FastForward,
                            contentDescription = "For You",
                            tint = if (activeTab == DashboardTab.FOR_YOU) White100 else Gray400,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "For You",
                            color = if (activeTab == DashboardTab.FOR_YOU) White100 else Gray400,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (activeTab == DashboardTab.FOR_YOU) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(Teal100)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { dashboardViewModel.switchTab(DashboardTab.FOLLOWED) }
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            FontAwesomeIcons.Solid.ObjectGroup,
                            contentDescription = "Following",
                            tint = if (activeTab == DashboardTab.FOLLOWED) White100 else Gray400,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Followed",
                            color = if (activeTab == DashboardTab.FOLLOWED) White100 else Gray400,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (activeTab == DashboardTab.FOLLOWED) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(Teal100)
                        )
                    }
                }
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(posts) { post ->
                authenticationData?.userId?.let {
                    DashboardPostCard(
                        post = post,
                        currentUserId = it,
                        onAuthorClick = { authorId ->
                            navController.navigate("user_profile/${authorId}")
                        },
                        userPostCollections = userPostCollections
                    )
                }
            }

            item {
                if (posts.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Teal100,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}