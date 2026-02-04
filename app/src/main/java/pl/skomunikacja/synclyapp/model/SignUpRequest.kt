package pl.skomunikacja.synclyapp.model

data class SignUpRequest(
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String
)