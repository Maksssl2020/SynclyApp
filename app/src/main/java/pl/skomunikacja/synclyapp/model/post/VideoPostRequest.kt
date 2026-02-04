package pl.skomunikacja.synclyapp.model.post

import pl.skomunikacja.synclyapp.model.MediaRequest

class VideoPostRequest(
    val description: String,
    val tags: List<String>,
    val videos: List<MediaRequest>,
    val type: String = "video"
) : PostRequest(
    tags
) {
    override fun toString(): String {
        return "VideoPostRequest(description='$description', tags=$tags, videos=$videos, type='$type')"
    }
}

