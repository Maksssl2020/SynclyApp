package pl.skomunikacja.synclyapp.helpers

import pl.skomunikacja.synclyapp.model.AuthenticationRequest
import pl.skomunikacja.synclyapp.model.AuthenticationResponse
import pl.skomunikacja.synclyapp.model.SignUpRequest
import pl.skomunikacja.synclyapp.model.UserData
import pl.skomunikacja.synclyapp.model.UserProfileData
import pl.skomunikacja.synclyapp.service.ApiUsersService

class ApiUsersHelper(
    private val api: ApiUsersService
) {
    suspend fun searchUsers(query: String): List<UserData> =
        api.searchUsers(query)


    suspend fun getUserProfile(userId: Long): UserProfileData? =
        api.getUserProfile(userId)

    suspend fun authenticate(username: String, password: String): AuthenticationResponse? {
        return try {
            api.postAuthentication(
                AuthenticationRequest(username, password)
            )
        } catch (ex: Exception) {
            println("Authentication Failed: ${ex.message}")
            null
        }
    }

    suspend fun signUp(data: SignUpRequest): Boolean {
        return try {
            val response = api.postSignUp(data)
            response.isSuccessful
        } catch (ex: Exception) {
            println("Register Failed: ${ex.message}")
            false
        }
    }
}