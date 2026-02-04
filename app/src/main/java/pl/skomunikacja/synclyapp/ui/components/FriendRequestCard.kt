package pl.skomunikacja.synclyapp.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.skomunikacja.synclyapp.model.FriendRequestData
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Gray300
import pl.skomunikacja.synclyapp.ui.theme.Red200
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.ui.timeAgoOrDate

@Composable
fun FriendRequestCard(
    friendRequest: FriendRequestData,
    isOutgoing: Boolean = false,
    onAccept: ((requestId: Long) -> Unit)? = null,
    onReject: ((requestId: Long) -> Unit)? = null,
    onRemoveFriendRequest: ((receiverId: Long) -> Unit)? = null
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
                    .background(Gray300),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.userProfile.displayName.first().toString(),
                    color = White100,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
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

            // Action buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isOutgoing) {
                    // Cancel button for outgoing requests
                    Button(
                        onClick = { onRemoveFriendRequest?.invoke(friendRequest.receiver.userId) },
                        colors = ButtonDefaults.buttonColors(containerColor = Red200),
                        modifier = Modifier.size(40.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Anuluj",
                            tint = White100,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    // Accept and reject buttons for incoming requests
                    Button(
                        onClick = { onAccept?.invoke(friendRequest.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = Teal100),
                        modifier = Modifier.size(40.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Akceptuj",
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
                            contentDescription = "Odrzuć",
                            tint = White100,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
