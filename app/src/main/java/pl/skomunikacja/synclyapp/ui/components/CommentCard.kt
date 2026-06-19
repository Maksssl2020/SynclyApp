package pl.skomunikacja.synclyapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Expand
import compose.icons.fontawesomeicons.solid.ExpandAlt
import compose.icons.fontawesomeicons.solid.Reply
import pl.skomunikacja.synclyapp.model.PostComment
import pl.skomunikacja.synclyapp.ui.theme.Black200
import pl.skomunikacja.synclyapp.ui.theme.Gray400
import pl.skomunikacja.synclyapp.ui.theme.Gray600
import pl.skomunikacja.synclyapp.ui.theme.Red100
import pl.skomunikacja.synclyapp.ui.theme.Teal100
import pl.skomunikacja.synclyapp.ui.theme.White100

@Composable
fun CommentCard(
    comment: PostComment,
    modifier: Modifier = Modifier,
    currentUserId: Long = 1L,
    onLikeClick: (Long) -> Unit = {},
    onReplyClick: (PostComment) -> Unit = {},
    onAuthorClick: (Long) -> Unit = {}
) {
    var showReplies by remember { mutableStateOf(false) }
    val isLiked = comment.likesBy.contains(currentUserId)
    val likesCount = comment.likesBy.size

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            ProfileAvatar(
                onAvatarClick = {
                    onAuthorClick(comment.authorId)
                },
                base64Image = comment.authorImage?.imageData,
                initials = comment.authorName.first().toString(),
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Teal100),
            )

            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    color = Black200,
                    shape = RoundedCornerShape(
                        topStart = 4.dp,
                        topEnd = 16.dp,
                        bottomEnd = 16.dp,
                        bottomStart = 16.dp
                    )
                ) {
                    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                        Text(
                            text = comment.authorName,
                            color = White100,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = comment.content,
                            color = Gray400,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.padding(start = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable {
                            onLikeClick(comment.id)
                        }
                    ) {
                        Icon(
                            if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (isLiked) Red100 else Gray400,
                            modifier = Modifier.size(15.dp)
                        )
                        if (likesCount > 0) {
                            Text(
                                text = likesCount.toString(),
                                color = if (isLiked) Red100 else Gray400,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable { onReplyClick(comment) }
                    ) {
                        Icon(
                            FontAwesomeIcons.Solid.Reply,
                            contentDescription = "Reply",
                            tint = Gray400,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Reply",
                            color = Gray400,
                            fontSize = 12.sp
                        )
                    }

                    if (comment.replies.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.clickable { showReplies = !showReplies }
                        ) {
                            Icon(
                                if (showReplies) FontAwesomeIcons.Solid.ExpandAlt else FontAwesomeIcons.Solid.Expand,
                                contentDescription = null,
                                tint = Teal100,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "${comment.replies.size} replies",
                                color = Teal100,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        if (showReplies && comment.replies.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .padding(start = 18.dp)
                    .height(IntrinsicSize.Min)
            ) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(Gray600)
                )
                Column(
                    modifier = Modifier.padding(start = 16.dp),
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
}

