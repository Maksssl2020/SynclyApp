package pl.skomunikacja.synclyapp.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.skomunikacja.synclyapp.config.RetrofitClient
import pl.skomunikacja.synclyapp.helpers.ApiPostCollectionsHelper
import pl.skomunikacja.synclyapp.helpers.ApiUsersHelper
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.helpers.PostCollectionsManager
import pl.skomunikacja.synclyapp.model.AuthenticationResponse

class SignInViewModel : ViewModel() {
    private val apiUsersHelper = ApiUsersHelper(RetrofitClient.apiUsersService)
    private val apiPostCollectionsHelper =
        ApiPostCollectionsHelper(RetrofitClient.apiPostCollectionsService)

    suspend fun signIn(username: String, password: String): AuthenticationResponse? {
        if (username.isBlank() || password.isBlank()) {
            return null
        }

        val authenticationResponse = apiUsersHelper.authenticate(username, password)

        if (authenticationResponse != null) {
            ApplicationManager.changeAuthenticationData(authenticationResponse)
            fetchUserPostCollections(authenticationResponse.userId)
        }

        return authenticationResponse
    }

    private suspend fun fetchUserPostCollections(userId: Long) {
        val userAllPostCollections =
            apiPostCollectionsHelper.getUserAllPostCollections(userId)

        PostCollectionsManager.changeUserPostCollections(userAllPostCollections)
    }
}