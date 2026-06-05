package pl.skomunikacja.synclyapp.helpers

import okhttp3.MultipartBody
import pl.skomunikacja.synclyapp.model.AuthenticationRequest
import pl.skomunikacja.synclyapp.model.AuthenticationResponse
import pl.skomunikacja.synclyapp.model.Image
import pl.skomunikacja.synclyapp.model.SignUpRequest
import pl.skomunikacja.synclyapp.model.UserData
import pl.skomunikacja.synclyapp.model.UserProfileData
import pl.skomunikacja.synclyapp.model.UserProfileUpdateRequest
import pl.skomunikacja.synclyapp.service.ApiUsersService
import java.io.File

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

    suspend fun uploadAvatar(userId: Long, data: MultipartBody.Part): Image? {
        return try {
            val response = api.uploadAvatar(userId, data)
            response.isSuccessful
            response.body()
        } catch (ex: Exception) {
            println("Upload Avatar Failed: ${ex.message}")
            null
        }
    }

    suspend fun updateUserProfile(userId: Long, data: UserProfileUpdateRequest): UserProfileData? {
        return try {
            val response = api.updateUserProfile(userId, data)
            response.isSuccessful
            response.body()
        } catch (ex: Exception) {
            println("Update User Profile Failed: ${ex.message}")
            null
        }
    }
}