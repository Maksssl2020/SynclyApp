package pl.skomunikacja.synclyapp.model.post

class QuotePostRequest(
    val quote: String,
    val source: String? = null,
    val tags: List<String> = emptyList(),
    val type: String = "quote"
) : PostRequest(
    tags
) {
    override fun toString(): String {
        return "QuotePostRequest(quote='$quote', source=$source, tags=$tags, type='$type')"
    }
}

