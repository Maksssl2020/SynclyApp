package pl.skomunikacja.synclyapp.model.post

class TextPostRequest(
    val title: String? = null,
    val content: String,
    val tags: List<String> = emptyList(),
    val type: String = "text"
) : PostRequest(
    tags
) {
    override fun toString(): String {
        return "TextPostRequest(title=$title, content='$content', tags=$tags, type='$type')"
    }
}

