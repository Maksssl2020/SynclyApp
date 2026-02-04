package pl.skomunikacja.synclyapp.model

data class CommentRequest(
    val postId: Long,
    val userId: Long,
    val content: String
)
