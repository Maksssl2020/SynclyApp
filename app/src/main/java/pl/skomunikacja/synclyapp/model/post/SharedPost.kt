package pl.skomunikacja.synclyapp.model.post

import pl.skomunikacja.synclyapp.model.UserData

data class SharedPost(
    val id: String,
    val sharedBy: UserData,
    val originalPost: Post,
    val sharedAt: String
)
