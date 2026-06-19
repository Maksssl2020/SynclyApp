package pl.skomunikacja.synclyapp.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.skomunikacja.synclyapp.model.CommentReplyRequest
import pl.skomunikacja.synclyapp.model.CommentRequest
import pl.skomunikacja.synclyapp.model.PostComment
import pl.skomunikacja.synclyapp.ui.theme.Black300
import pl.skomunikacja.synclyapp.ui.theme.Gray400
import pl.skomunikacja.synclyapp.ui.theme.White100
import pl.skomunikacja.synclyapp.view_model.PostViewModel

@Composable
fun PostCommentsSection(
    postId: Long,
    viewModel: PostViewModel,
    currentUserId: Long,
    comments: List<PostComment>,
    onAuthorClick: (Long) -> Unit,
) {
    val context = LocalContext.current
    var replyTo by remember { mutableStateOf<PostComment?>(null) }

    Spacer(modifier = Modifier.height(16.dp))
    Surface(
        color = Black300,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Comments (${comments.size})",
                color = White100,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            CommentInput(
                replyingTo = replyTo?.authorName,
                onCancelReply = {
                    replyTo = null
                },
                onSendComment = { commentText ->
                    if (replyTo == null) {
                        val commentRequest = CommentRequest(
                            postId,
                            currentUserId,
                            commentText
                        )
                        viewModel.addCommentToPost(commentRequest) { result ->
                            if (!result) {
                                Toast.makeText(
                                    context,
                                    "Something went wrong.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        val commentReplyRequest = CommentReplyRequest(
                            replyTo!!.id,
                            currentUserId,
                            commentText
                        )
                        viewModel.addCommentReply(commentReplyRequest) { result ->
                            if (!result) {
                                Toast.makeText(
                                    context,
                                    "Something went wrong.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            )

            if (comments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    comments.forEach { comment ->
                        CommentCard(
                            comment = comment,
                            currentUserId = currentUserId,
                            onLikeClick = { commentId ->
                                viewModel.likeUnlikePostComment(currentUserId, commentId)
                            },
                            onReplyClick = {
                                replyTo = it
                            },
                            onAuthorClick = {
                                onAuthorClick(it)
                            }
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No comments yet. Be the first!",
                    color = Gray400,
                    fontSize = 14.sp
                )
            }
        }
    }
}
