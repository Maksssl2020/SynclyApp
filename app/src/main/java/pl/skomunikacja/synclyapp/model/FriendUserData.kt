package pl.skomunikacja.synclyapp.model

data class FriendUserData(
    val user: UserData,
    val mutualFriendsCount: Long? = 0L
)
