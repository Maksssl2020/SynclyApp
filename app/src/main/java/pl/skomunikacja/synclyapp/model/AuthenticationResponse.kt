package pl.skomunikacja.synclyapp.model

data class AuthenticationResponse(
    val userId: Long,
    val username: String,
    val role: String,
    val avatar: Image?
)
