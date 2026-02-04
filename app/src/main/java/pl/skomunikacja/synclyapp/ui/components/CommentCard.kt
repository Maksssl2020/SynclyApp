package pl.skomunikacja.synclyapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Expand
import compose.icons.fontawesomeicons.solid.Minus
import compose.icons.fontawesomeicons.solid.Reply
import pl.skomunikacja.synclyapp.model.PostComment
import pl.skomunikacja.synclyapp.ui.theme.Gray400
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.ui.timeAgoOrDate
import pl.skomunikacja.synclyapp.view_model.CommentViewModel

@Composable
fun CommentCard(
    comment: PostComment,
    currentUserId: Long = 1L,
    onLikeClick: (Long) -> Unit = {},
    onReplyClick: (PostComment) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showReplies by remember { mutableStateOf(false) }
    val viewModel = remember(comment.id) { CommentViewModel(comment) }
    val state by viewModel.comment.collectAsState()

    val isLiked = state.likesBy.contains(currentUserId)
    val likesCount = state.likesBy.size

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileAvatar(
                onAvatarClick = {},
                base64Image = comment.authorImage?.imageData,
                initials = comment.authorName.first().toString(),
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Teal100),
            )

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = comment.authorName,
                        color = White100,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "@${comment.authorUsername}",
                        color = Gray400,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "• ${timeAgoOrDate(comment.createdAt)}",
                        color = Gray400,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = comment.content,
                    color = White100,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable {
                            viewModel.toggleLike(currentUserId)
                        }
                    ) {
                        Icon(
                            if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like comment",
                            tint = if (isLiked) Color(0xFFFF6B6B) else Gray400,
                            modifier = Modifier.size(16.dp)
                        )
                        if (likesCount > 0) {
                            Text(
                                text = likesCount.toString(),
                                color = Gray400,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Reply button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable {
                            println("REPLYING TO: ${comment.id}")
                            onReplyClick(comment)
                        }
                    ) {
                        Icon(
                            FontAwesomeIcons.Solid.Reply,
                            contentDescription = "Reply",
                            tint = Gray400,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Odpowiedz",
                            color = Gray400,
                            fontSize = 12.sp
                        )
                    }

                    // Show replies button
                    if (comment.replies.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.clickable { showReplies = !showReplies }
                        ) {
                            Icon(
                                if (showReplies) FontAwesomeIcons.Solid.Minus else FontAwesomeIcons.Solid.Expand,
                                contentDescription = if (showReplies) "Hide replies" else "Show replies",
                                tint = Teal100,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${comment.replies.size} odpowiedzi",
                                color = Teal100,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Replies
        if (showReplies && comment.replies.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier.padding(start = 44.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                comment.replies.forEach { reply ->
                    CommentCard(
                        comment = reply,
                        currentUserId = currentUserId,
                        onLikeClick = onLikeClick,
                        onReplyClick = onReplyClick
                    )
                }
            }
        }
    }
}

