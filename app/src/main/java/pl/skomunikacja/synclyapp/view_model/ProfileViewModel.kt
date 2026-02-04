package pl.skomunikacja.synclyapp.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pl.skomunikacja.synclyapp.config.RetrofitClient
import pl.skomunikacja.synclyapp.helpers.ApiUsersHelper
import pl.skomunikacja.synclyapp.model.UserProfileData

class ProfileViewModel : ViewModel() {
    private val apiUsersHelper = ApiUsersHelper(RetrofitClient.apiUsersService)

    private val _userProfile = MutableStateFlow<UserProfileData?>(null)
    val userProfile = _userProfile.asStateFlow()

    suspend fun fetchUserProfileData(userId: Long) {
        val userProfileData = apiUsersHelper.getUserProfile(userId)
        _userProfile.value = userProfileData
    }
}