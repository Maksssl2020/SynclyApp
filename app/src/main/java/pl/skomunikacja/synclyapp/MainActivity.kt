package pl.skomunikacja.synclyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Home
import compose.icons.fontawesomeicons.solid.Plus
import compose.icons.fontawesomeicons.solid.Search
import compose.icons.fontawesomeicons.solid.User
import compose.icons.fontawesomeicons.solid.Users
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.ui.components.AppHeader
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.SynclyAppTheme
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SynclyAppTheme {
                AppScreen()
            }
        }
    }
}

@Composable
fun AppScreen() {
    val navController: NavHostController = rememberNavController()
    val authenticationData = ApplicationManager.authenticationData.collectAsState()

    if (authenticationData.value == null) {
        NavHost(
            navController = navController,
            startDestination = "login"
        ) {
            composable("login") {
                SignInScreen(
                    onLoginSuccess = { },
                    onNavigateToRegister = { navController.navigate("register") }
                )
            }
            composable("register") {
                SignUpScreen(
                    onRegisterSuccess = { },
                    onNavigateToLogin = { navController.navigate("login") }
                )
            }
        }
    } else {
        MainAppContent(navController)
    }
}

@Composable
fun MainAppContent(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    Scaffold(
        topBar = {
            AppHeader(
                title = when (currentRoute) {
                    "home" -> "Syncly"
                    "friends" -> "Friends"
                    "search" -> "Search"
                    "profile" -> "Profile"
                    else -> "Syncly"
                },
                onPostCollectionsClick = { navController.navigate("postCollections") },
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Black300,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
            ) {
                IconButton(
                    onClick = {
                        navController.navigate("home") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = false }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        FontAwesomeIcons.Solid.Home,
                        contentDescription = "home",
                        tint = if (currentRoute == "home") Teal100 else White100,
                        modifier = Modifier.size(22.dp)
                    )
                }

                IconButton(
                    onClick = {
                        navController.navigate("friends") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = false }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        FontAwesomeIcons.Solid.Users,
                        contentDescription = "friends",
                        tint = if (currentRoute == "friends") Teal100 else White100,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate("add_post") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .border(2.dp, Teal100, RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            FontAwesomeIcons.Solid.Plus,
                            tint = Teal100,
                            contentDescription = "add_post",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                IconButton(
                    onClick = {
                        navController.navigate("search") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = false }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        FontAwesomeIcons.Solid.Search,
                        contentDescription = "search",
                        tint = if (currentRoute == "search") Teal100 else White100,
                        modifier = Modifier.size(22.dp)
                    )
                }

                IconButton(
                    onClick = {
                        navController.navigate("profile") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = false }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        FontAwesomeIcons.Solid.User,
                        contentDescription = "profile",
                        tint = if (currentRoute == "profile") Teal100 else White100,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") { DashboardScreen(navController) }
            composable("postCollectionForm") { PostCollectionFormScreen(onGoBack = {
                navController.popBackStack()
            }) }
            composable("signIn") {
                SignInScreen(
                    onLoginSuccess = { navController.navigate("home") },
                    onNavigateToRegister = { navController.navigate("register") }
                )
            }
            composable("postCollections") {
                PostCollectionScreen(
                    onCollectionClick = {
                        collection -> navController.navigate("postCollectionDetails/${collection.id}")
                    },
                    onNavigateToPostCollectionForm = {
                        navController.navigate("postCollectionForm")
                    }
                )
            }
            composable(
                route = "postCollectionDetails/{collectionId}",
                arguments = listOf(navArgument("collectionId") {type = NavType.LongType})
            ) { backStackEntry ->
                val collectionId = backStackEntry.arguments?.getLong("collectionId")!!
                PostCollectionDetailsScreen(
                    collectionId = collectionId,
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable("friends") { FriendsScreen(onNavigateToUserProfile = {
                navController.navigate("user_profile/${it}")
            }) }
            composable("search") { SearchScreen(onNavigateToUserProfile = {
                navController.navigate("user_profile/${it}")
            }) }
            composable("profile") { ProfileScreen(
                onNavigateToSignInScreen = {
                    navController.navigate("signIn")
                }
            ) }
            composable("add_post") {
                AddPostScreen(
                    onPostCreated = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = false }
                        }
                    },
            )}
            composable(
                route = "user_profile/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.LongType })
            ) { backStackEntry ->
                val userId: Long = backStackEntry.arguments?.getLong("userId") ?: -1L
                UserProfileScreen(userId = userId)
            }
        }
    }
}