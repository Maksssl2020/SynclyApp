package pl.skomunikacja.synclyapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.UserMinus
import compose.icons.fontawesomeicons.solid.UserPlus
import pl.skomunikacja.synclyapp.model.UserProfileData
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Black400
import pl.skomunikacja.synclyapp.ui.theme.Gray300
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.view_model.UserProfileViewModel

@Composable
fun ProfileActionButtons(
    userProfile: UserProfileData,
    viewModel: UserProfileViewModel
) {
    val userFollowedUsers by viewModel.userFollowedUsers.collectAsState()
    val userLikedProfiles by viewModel.userLikedProfilesIds.collectAsState()

    val isFollowed = userFollowedUsers.any { user -> user.profileOwnerId == userProfile.profileOwnerId }
    val isLiked = userLikedProfiles.contains(userProfile.profileOwnerId)

    var isFollowing by remember { mutableStateOf(false) }
    var friendStatus by remember { mutableStateOf("none") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = {
                isFollowing = !isFollowing
                // TODO: Implement follow/unfollow API call
            },
            modifier = Modifier
                .weight(1f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFollowing) Gray300 else Teal100
            )
        ) {
            Text(
                text = if (isFollowing) "Przestań obserwować" else "Obserwuj",
                color = if (isFollowing) White100 else Black400,
                fontSize = 12.sp
            )
        }

        IconButton(
            onClick = {
                // TODO: Implement like/unlike profile API call
            },
            modifier = Modifier
                .background(
                    color = if (isLiked) Teal100 else Black300,
                    shape = RoundedCornerShape(8.dp)
                )
                .size(48.dp)
        ) {
            Icon(
                Icons.Default.Favorite,
                contentDescription = if (isLiked) "Odlub profil" else "Polub profil",
                tint = if (isLiked) Black400 else White100
            )
        }
    }

    // Friend Request Button
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        when (friendStatus) {
            "none" -> {
                Button(
                    onClick = {
                        friendStatus = "pending"
                        // TODO: Implement send friend request API call
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Black300),
                    border = BorderStroke(1.dp, Teal100),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        FontAwesomeIcons.Solid.UserPlus,
                        contentDescription = "Dodaj do znajomych",
                        tint = Teal100,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Dodaj do znajomych", color = Teal100)
                }
            }
            "pending" -> {
                Button(
                    onClick = {
                        friendStatus = "none"
                        // TODO: Implement cancel friend request API call
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Gray300),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Anuluj zaproszenie", color = White100)
                }
            }
            "friends" -> {
                Button(
                    onClick = {
                        friendStatus = "none"
                        // TODO: Implement remove friend API call
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        FontAwesomeIcons.Solid.UserMinus,
                        contentDescription = "Usuń ze znajomych",
                        tint = White100,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Usuń ze znajomych", color = White100)
                }
            }
        }
    }
}