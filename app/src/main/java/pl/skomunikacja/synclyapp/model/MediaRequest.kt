package pl.skomunikacja.synclyapp.model

import java.io.File

data class MediaRequest(
    val url: String? = null,
    val mediaFile: File? = null,
    val mediaType: MediaType
)
