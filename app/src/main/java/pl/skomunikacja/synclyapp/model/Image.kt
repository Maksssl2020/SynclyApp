package pl.skomunikacja.synclyapp.model

data class Image(
    val imageId: Long,
    val imageData: String? = null,
    val url: String? = null,
    val mimeType: String? = null,
)