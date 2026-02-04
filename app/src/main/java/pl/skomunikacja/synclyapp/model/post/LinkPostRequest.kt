package pl.skomunikacja.synclyapp.model.post

class LinkPostRequest(
    val title: String? = null,
    val description: String,
    val links: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val type: String = "link"
) : PostRequest(
    tags
) {
    override fun toString(): String {
        return "LinkPostRequest(title=$title, description='$description', links=$links, tags=$tags, type='$type')"
    }
}

