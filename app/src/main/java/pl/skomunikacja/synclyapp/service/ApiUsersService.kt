package pl.skomunikacja.synclyapp.service

import pl.skomunikacja.synclyapp.model.AuthenticationRequest
import pl.skomunikacja.synclyapp.model.AuthenticationResponse
import pl.skomunikacja.synclyapp.model.SignUpRequest
import pl.skomunikacja.synclyapp.model.UserData
import pl.skomunikacja.synclyapp.model.UserProfileData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiUsersService {

    @GET("api/v1/users-profiles/android-app/{userId}")
    suspend fun getUserProfile(
        @Path("userId") userId: Long
    ): UserProfileData?

    @GET("api/v1/users/search")
    suspend fun searchUsers(
        @Query("query") query: String
    ): List<UserData>

    @Headers("Accept: application/json")
    @POST("api/v1/authentication/android-app/login")
    suspend fun postAuthentication(
        @Body data: AuthenticationRequest
    ): AuthenticationResponse?

    @Headers("Accept: application/json")
    @POST("api/v1/authentication/register")
    suspend fun postSignUp(
        @Body data: SignUpRequest
    ): Response<Unit>
}