package pl.skomunikacja.synclyapp.model.post
import com.google.gson.annotations.SerializedName
import pl.skomunikacja.synclyapp.model.Image

class PhotoPost(
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
    @SerializedName("caption") val caption: String,
    @SerializedName("imageUrls") val imageUrls: List<String>
) : Post(
    id, createdAt, updatedAt, PostType.PHOTO,
    authorId, authorName, authorUsername,
    tags, likesBy, savedBy, sharedBy, commentsCount, authorAvatar
)