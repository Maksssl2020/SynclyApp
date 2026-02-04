package pl.skomunikacja.synclyapp.model

import pl.skomunikacja.synclyapp.model.post.Post

data class PostCollection(
    val id: Long,
    val userId: Long,
    val title: String,
    val color: String,
    val default: Boolean,
    val posts: ArrayList<Post>
)
