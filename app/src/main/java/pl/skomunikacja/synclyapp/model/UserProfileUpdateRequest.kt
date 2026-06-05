package pl.skomunikacja.synclyapp.model

data class UserProfileUpdateRequest(
    val username: String,
    val email: String,
    val displayName: String,
    val bio: String,
    val location: String,
    val website: String,
)
