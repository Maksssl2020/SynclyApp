package pl.skomunikacja.synclyapp.model.post

import com.google.gson.annotations.SerializedName
import pl.skomunikacja.synclyapp.model.Image

class TextPost(
    id: Long,
    createdAt: String,
    updatedAt: String,
    authorId: Long,
    authorName: String,
    authorUsername: String,
    tags: List<PostTag>,
    likesBy: List<Long>,
    savedBy: List<Long>,
    sharedBy: List<Long>,
    commentsCount: Int,
    authorAvatar: Image?,
    @SerializedName("title") val title: String?,
    @SerializedName("content") val content: String
) : Post(
    id, createdAt, updatedAt, PostType.TEXT,
    authorId, authorName, authorUsername,
    tags, likesBy, savedBy, sharedBy, commentsCount, authorAvatar
)