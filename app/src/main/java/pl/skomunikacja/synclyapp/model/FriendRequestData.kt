package pl.skomunikacja.synclyapp.model

data class FriendRequestData(
    val id: Long,
    val requester: UserData,
    val receiver: UserData,
    val status: String,
    val createdAt: String
)
