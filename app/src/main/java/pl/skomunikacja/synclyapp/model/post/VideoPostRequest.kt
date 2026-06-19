package pl.skomunikacja.synclyapp.model.post

class VideoPostRequest(
    val description: String,
    val tags: List<String>,
    val videoUrls: List<String>,
    val type: String = "video"
) : PostRequest(
    tags
) {
    override fun toString(): String {
        return "VideoPostRequest(description='$description', tags=$tags, videoUrls=$videoUrls, type='$type')"
    }
}

