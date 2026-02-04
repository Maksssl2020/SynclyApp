package pl.skomunikacja.synclyapp.model

data class CommentReplyRequest(
    val parentCommentId: Long,
    val userId: Long,
    val content: String
)
