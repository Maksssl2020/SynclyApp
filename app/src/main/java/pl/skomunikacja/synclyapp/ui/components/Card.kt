package pl.skomunikacja.synclyapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.skomunikacja.synclyapp.model.FriendUserData
import pl.skomunikacja.synclyapp.ui.theme.Black200
import pl.skomunikacja.synclyapp.ui.theme.Gray300
import pl.skomunikacja.synclyapp.ui.theme.Red100
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.ui.timeAgoOrDate

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
                        text = "${friend.mutualFriendsCount} wspólnych znajomych",
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
                        contentDescription = "Więcej opcji",
                        tint = Gray300
                    )
                }

                // Status indicator
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
                            "BLOCKED" -> "Zablokowany"
                            "INACTIVE" -> "Nieaktywny"
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

