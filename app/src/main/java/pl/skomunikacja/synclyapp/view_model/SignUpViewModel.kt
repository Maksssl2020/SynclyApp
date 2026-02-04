package pl.skomunikacja.synclyapp.view_model

import androidx.lifecycle.ViewModel
import pl.skomunikacja.synclyapp.config.RetrofitClient
import pl.skomunikacja.synclyapp.helpers.ApiUsersHelper
import pl.skomunikacja.synclyapp.model.SignUpRequest

class SignUpViewModel : ViewModel() {
    private val apiUsersHelper = ApiUsersHelper(RetrofitClient.apiUsersService)

    suspend fun signup(
        username: String,
        password: String,
        email: String,
        firstName: String,
        lastName: String
    ): Boolean {
        if (
            username.isNotEmpty() &&
            password.isNotEmpty() &&
            email.isNotEmpty() &&
            firstName.isNotEmpty() &&
            lastName.isNotEmpty()
            ) {
            val signUpRequest = SignUpRequest(
                username,
                password,
                email,
                firstName,
                lastName
            )

            return apiUsersHelper.signUp(signUpRequest)
        }

        return false;
    }
}