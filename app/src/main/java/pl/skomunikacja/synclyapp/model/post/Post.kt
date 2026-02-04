package pl.skomunikacja.synclyapp.model.post

import com.google.gson.annotations.SerializedName
import pl.skomunikacja.synclyapp.model.Image

open class Post(
    @SerializedName("id") open val id: Long,
    @SerializedName("createdAt") open val createdAt: String,
    @SerializedName("updatedAt") open val updatedAt: String,
    @SerializedName("postType") open val postType: PostType,
    @SerializedName("authorId") open val authorId: Long,
    @SerializedName("authorName") open val authorName: String,
    @SerializedName("authorUsername") open val authorUsername: String,
    @SerializedName("tags") open val tags: List<PostTag>,
    @SerializedName("likesBy") open val likesBy: List<Long>,
    @SerializedName("savedBy") open val savedBy: List<Long>,
    @SerializedName("sharedBy") open val sharedBy: List<Long>,
    @SerializedName("commentsCount") open val commentsCount: Int,
    @SerializedName("authorAvatar") open val authorAvatar: Image?
) {
    fun copyWith(
        likesBy: List<Long> = this.likesBy,
        savedBy: List<Long> = this.savedBy,
        sharedBy: List<Long> = this.sharedBy,
        commentsCount: Int = this.commentsCount
    ): Post {
        return Post(
            id,
            createdAt,
            updatedAt,
            postType,
            authorId,
            authorName,
            authorUsername,
            tags,
            likesBy,
            savedBy,
            sharedBy,
            commentsCount,
            authorAvatar
        )
    }
}
