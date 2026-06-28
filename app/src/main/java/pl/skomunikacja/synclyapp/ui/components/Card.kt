package pl.skomunikacja.synclyapp.ui.components

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Link
import pl.skomunikacja.synclyapp.helpers.Utils.timeAgoOrDate
import pl.skomunikacja.synclyapp.model.FriendRequestData
import pl.skomunikacja.synclyapp.model.FriendUserData
import pl.skomunikacja.synclyapp.model.PostCollection
import pl.skomunikacja.synclyapp.model.post.SharedPost
import pl.skomunikacja.synclyapp.ui.theme.Black200
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Gray300
import pl.skomunikacja.synclyapp.ui.theme.Red100
import pl.skomunikacja.synclyapp.ui.theme.Red200
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100

@Composable
fun FriendCard(
    friend: FriendUserData,
    onAvatarClick: () -> Unit = {},
    onCardClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Black200
        ),
        shape = RoundedCornerShape(12.dp),
        onClick = onCardClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        when (friend.user.status) {
                            "ACTIVE" -> Teal100
                            "BLOCKED" -> Red100
                            "INACTIVE" -> Gray300
                            else -> Gray300
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                ProfileAvatar(
                    base64Image = friend.user.userProfile.avatar?.imageData,
                    modifier = Modifier.size(32.dp),
                    onAvatarClick = onAvatarClick,
                    initials = friend.user.userProfile.displayName.first().toString().uppercase()
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = friend.user.userProfile.displayName,
                    color = White100,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Text(
                    text = "@${friend.user.username}",
                    color = Gray300,
                    fontSize = 14.sp
                )
                if (friend.mutualFriendsCount != null && friend.mutualFriendsCount > 0) {
                    Text(
                        text = "${friend.mutualFriendsCount} mutual friends",
                        color = Teal100,
                        fontSize = 12.sp
                    )
                }
                if (friend.user.userProfile.bio != null) {
                    Text(
                        text = friend.user.userProfile.bio,
                        color = Gray300,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                IconButton(onClick = onMoreClick) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = Gray300
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                when (friend.user.status) {
                                    "ACTIVE" -> if (friend.user.status == "ACTIVE") Teal100 else Gray300
                                    "BLOCKED" -> Red100
                                    "INACTIVE" -> Gray300
                                    else -> Gray300
                                }
                            )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = when (friend.user.status) {
                            "BLOCKED" -> "Blocked"
                            "INACTIVE" -> "Inactive"
                            "ACTIVE" -> "Online"
                            else -> timeAgoOrDate(friend.user.lastActive)
                        },
                        color = when (friend.user.status) {
                            "ACTIVE" -> if (friend.user.status == "ACTIVE") Teal100 else Gray300
                            "BLOCKED" -> Red100
                            "INACTIVE" -> Gray300
                            else -> Gray300
                        },
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileStatCard(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = White100
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = Gray300
        )
    }
}


@Composable
fun FriendRequestCard(
    friendRequest: FriendRequestData,
    onNavigateToUserProfile: (Long) -> Unit,
    isOutgoing: Boolean = false,
    onAccept: ((requestId: Long) -> Unit)? = null,
    onReject: ((requestId: Long) -> Unit)? = null,
    onRemoveFriendRequest: ((receiverId: Long) -> Unit)? = null,
) {
    val user = if (isOutgoing) friendRequest.receiver else friendRequest.requester

    Card(
        modifier = Modifier.fillMaxWidth(),
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
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Teal100),
                contentAlignment = Alignment.Center
            ) {
                ProfileAvatar(
                    initials = user.userProfile.displayName.first().toString(),
                    onAvatarClick = {
                        onNavigateToUserProfile(user.userProfile.userProfileId)
                    },
                    modifier = Modifier,
                    base64Image = user.userProfile.avatar?.imageData
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.userProfile.displayName,
                    color = White100,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "@${user.username}",
                    color = Gray300,
                    fontSize = 14.sp
                )
                Text(
                    text = timeAgoOrDate(friendRequest.createdAt),
                    color = Gray300,
                    fontSize = 12.sp
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isOutgoing) {
                    Button(
                        onClick = { onRemoveFriendRequest?.invoke(friendRequest.receiver.userId) },
                        colors = ButtonDefaults.buttonColors(containerColor = Red200),
                        modifier = Modifier.size(40.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Cancel",
                            tint = White100,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    Button(
                        onClick = { onAccept?.invoke(friendRequest.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = Teal100),
                        modifier = Modifier.size(40.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Accept",
                            tint = White100,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Button(
                        onClick = { onReject?.invoke(friendRequest.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = Red200),
                        modifier = Modifier.size(40.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Decline",
                            tint = White100,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun YoutubeLinkCard(url: String) {
    val context = LocalContext.current

    Surface(
        color = Black300,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Teal100),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                context.startActivity(intent)
            }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                FontAwesomeIcons.Solid.Link,
                contentDescription = "YouTube",
                tint = Teal100,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Open video on youtube",
                color = Teal100,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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