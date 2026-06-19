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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.UserMinus
import compose.icons.fontawesomeicons.solid.UserPlus
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.model.FriendStatus
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
    val authenticatedUserData by ApplicationManager.authenticationData.collectAsState()
    val authenticatedUserId = authenticatedUserData?.userId ?: return

    val authenticatedUserFollowedUsers by viewModel.authenticatedUserFollowedUsers.collectAsState()
    val authenticatedUserLikedProfiles by viewModel.authenticatedUserLikedProfilesIds.collectAsState()
    val authenticatedUserFriendRequestStatus by viewModel.authenticatedUserFriendStatus.collectAsState()

    val isLiked = authenticatedUserLikedProfiles.contains(userProfile.userProfileId)

    val isFollowing = authenticatedUserFollowedUsers.any { followedUser ->
        followedUser.profileOwnerId == userProfile.profileOwnerId
    }

    print("authenticatedUserFriendRequestStatus: $authenticatedUserFriendRequestStatus")
    print("authenticatedUserFriendRequestStatus: $authenticatedUserFriendRequestStatus")
    print("authenticatedUserFriendRequestStatus: $authenticatedUserFriendRequestStatus")
    print("authenticatedUserFriendRequestStatus: $authenticatedUserFriendRequestStatus")
    print("authenticatedUserFriendRequestStatus: $authenticatedUserFriendRequestStatus")


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = {

                if (isFollowing) {
                    viewModel.unfollowUser(userProfile.userProfileId, authenticatedUserId)
                } else{
                    viewModel.followUser(userProfile.userProfileId, authenticatedUserId)
                }
            },
            modifier = Modifier
                .weight(1f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFollowing) Gray300 else Teal100
            )
        ) {
            Text(
                text = if (isFollowing) "Unfollow" else "Follow",
                color = if (isFollowing) White100 else Black400,
                fontSize = 12.sp
            )
        }

        IconButton(
            onClick = {
                if (isLiked) {
                    viewModel.unlikeUserProfile(authenticatedUserId, userProfile.userProfileId)
                } else {
                    viewModel.likeUserProfile(authenticatedUserId, userProfile.userProfileId)
                }
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
                contentDescription = if (isLiked) "Unlike profile" else "Like profile",
                tint = if (isLiked) Black400 else White100
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        when (authenticatedUserFriendRequestStatus) {
            FriendStatus.NONE, FriendStatus.DECLINED -> {
                Button(
                    onClick = {
                        viewModel.addUserToFriends(authenticatedUserId, userProfile.profileOwnerId)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Black300),
                    border = BorderStroke(1.dp, Teal100),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        FontAwesomeIcons.Solid.UserPlus,
                        contentDescription = "Add to friends",
                        tint = Teal100,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add to friends", color = Teal100)
                }
            }
            FriendStatus.PENDING -> {
                Button(
                    onClick = {
                        viewModel.cancelFriendRequest(authenticatedUserId, userProfile.profileOwnerId)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Gray300),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cancel friend request", color = White100)
                }
            }
            FriendStatus.ACCEPTED -> {
                Button(
                    onClick = {
                        viewModel.deleteFriend(authenticatedUserId, userProfile.profileOwnerId)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        FontAwesomeIcons.Solid.UserMinus,
                        contentDescription = "Delete friend",
                        tint = White100,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete friend", color = White100)
                }
            }
            FriendStatus.BLOCKED -> {}
        }
    }
}