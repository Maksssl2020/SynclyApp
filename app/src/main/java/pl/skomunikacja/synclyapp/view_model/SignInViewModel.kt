package pl.skomunikacja.synclyapp.view_model

import androidx.lifecycle.ViewModel
import pl.skomunikacja.synclyapp.config.RetrofitClient
import pl.skomunikacja.synclyapp.helpers.ApiUsersHelper
import pl.skomunikacja.synclyapp.model.AuthenticationResponse

class SignInViewModel : ViewModel() {
    private val apiUsersHelper = ApiUsersHelper(RetrofitClient.apiUsersService)

    suspend fun signIn(username: String, password: String): AuthenticationResponse? {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            return apiUsersHelper.authenticate(username, password)
        }

        return null;
    }
}