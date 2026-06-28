package pl.skomunikacja.synclyapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ListAlt
import pl.skomunikacja.synclyapp.helpers.normalizeWebsiteUrl
import pl.skomunikacja.synclyapp.model.UserProfileData
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Gray300
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.view_model.UserProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    title: String = "Syncly",
    onPostCollectionsClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Teal100
            )
        },
        actions = {
            IconButton(onClick = onPostCollectionsClick) {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.ListAlt,
                    contentDescription = "PostCollections",
                    tint = White100,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Black300,
            titleContentColor = White100,
            navigationIconContentColor = White100,
            actionIconContentColor = White100
        )
    )
}

@Composable
fun ProfileHeader(
    userProfile: UserProfileData,
    isOwnProfile: Boolean,
    viewModel: UserProfileViewModel
) {
    val uriHandler = LocalUriHandler.current

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

        if (userProfile.bio != null) {
            Text(
                text = userProfile.bio,
                fontSize = 14.sp,
                color = White100,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp)
            )
        }

        Text(
            text = "📍 ${userProfile.location}",
            fontSize = 14.sp,
            color = Gray300,
            modifier = Modifier.padding(top = 4.dp)
        )

        if (userProfile.website != null) {
            val websiteUrl = remember(userProfile.website) {
                normalizeWebsiteUrl(userProfile.website)
            }

            Text(
                text = "🌐 ${userProfile.website}",
                fontSize = 14.sp,
                color = Teal100,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable {
                        uriHandler.openUri(websiteUrl)
                    }
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
