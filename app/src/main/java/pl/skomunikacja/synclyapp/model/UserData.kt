package pl.skomunikacja.synclyapp.model

data class UserData(
    val userId: Long,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val createdAt: String,
    val lastActive: String,
    val role: String,
    val status: String,
    val mutualFriendsCount: Int? = 0,
    val userProfile: UserProfileData
)
