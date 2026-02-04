package pl.skomunikacja.synclyapp.model

data class Tag(
    val id: String,
    val name: String,
    val description: String,
    val postsCount: Long,
    val followersCount: Long,
    val trending: Boolean,
    val color: String,
    val type: String,
    val tagCategory: String,
    val createdAt: String
)
