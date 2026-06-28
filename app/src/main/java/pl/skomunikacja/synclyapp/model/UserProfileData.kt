package pl.skomunikacja.synclyapp.model

data class UserProfileData(
    val userProfileId: Long,
    val profileOwnerId: Long,
    val username: String,
    val email: String,
    val displayName: String,
    val bio: String?,
    val location: String?,
    val birthday: String?,
    val joinedAt: String,
    val profileLikes: Long,
    val followersCount: Int,
    val followingCount: Int,
    val website: String?,
    val postsCount: Long,
    val friendsCount: Long,
    val avatar: Image?
)
