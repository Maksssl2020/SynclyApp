package pl.skomunikacja.synclyapp.model.post

import pl.skomunikacja.synclyapp.model.MediaRequest

class PhotoPostRequest(
    val tags: List<String>,
    val caption: String,
    val photos: List<MediaRequest>,
    val type: String = "photo"
) : PostRequest(
    tags
) {
    override fun toString(): String {
        return "PhotoPostRequest(tags=$tags, caption='$caption', photos=$photos, type='$type')"
    }
}
