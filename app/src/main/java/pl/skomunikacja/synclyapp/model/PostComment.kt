package pl.skomunikacja.synclyapp.model

import pl.skomunikacja.synclyapp.model.post.PostRequest

data class PostComment(
    val id: Long,
    val authorId: Long,
    val postId: Long,
    val parentId: Long,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val authorUsername: String,
    val authorName: String,
    val authorImage: Image?,
    val likesBy: List<Long>,
    val replies: List<PostComment>
) {
    fun copyWith(
        likesBy: List<Long> = this.likesBy
    ): PostComment {
        return PostComment(
            id,
            authorId,
            postId,
            parentId,
            content,
            createdAt,
            updatedAt,
            authorUsername,
            authorName,
            authorImage,
            likesBy,
            replies
        )
    }
}
