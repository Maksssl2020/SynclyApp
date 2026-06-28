package pl.skomunikacja.synclyapp.view_model

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import pl.skomunikacja.synclyapp.config.RetrofitClient
import pl.skomunikacja.synclyapp.helpers.ApiUsersHelper
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.model.AvatarUploadState
import pl.skomunikacja.synclyapp.model.UserProfileData
import pl.skomunikacja.synclyapp.model.UserProfileUpdateRequest

class ProfileViewModel(
    private val application: Application
) : AndroidViewModel(application) {
    private val apiUsersHelper = ApiUsersHelper(RetrofitClient.apiUsersService)

    private val _state = MutableStateFlow(AvatarUploadState())
    val state = _state.asStateFlow()

    private val _userProfile = MutableStateFlow<UserProfileData?>(null)
    val userProfile = _userProfile.asStateFlow()

    suspend fun fetchUserProfileData(userId: Long) {
        val userProfileData = apiUsersHelper.getUserProfile(userId)
        _userProfile.value = userProfileData

        ApplicationManager.changeAvatarInAuthenticationData(userProfileData?.avatar)
    }

    suspend fun updateUserProfile(userId: Long, data: UserProfileUpdateRequest) {
        val response = apiUsersHelper.updateUserProfile(userId, data)
        _userProfile.value = response
    }

    suspend fun uploadAvatar(context: Context, userId: Long) {
        if (_state.value.avatarUri != null) {
            val multipartBodyPart =
                uriToMultipartBodyPart(context, _state.value.avatarUri!!, "file")
            val uploadedAvatar = apiUsersHelper.uploadAvatar(userId, multipartBodyPart)

            if (uploadedAvatar != null) {
                _state.update {
                    it.copy(
                        avatarBase64 = uploadedAvatar.imageData
                    )
                }

                _userProfile.update {
                    it?.copy(
                        avatar = uploadedAvatar
                    )
                }
            }

            ApplicationManager.changeAvatarInAuthenticationData(uploadedAvatar)
        }
    }

    fun handleFileChange(uri: Uri?) {
        if (uri == null) return

        val base64 = uriToBase64(uri)

        _state.update {
            it.copy(
                avatarUri = uri,
                avatarBase64 = base64
            )
        }
    }

    fun clearSelectedAvatar() {
        _state.update {
            it.copy(
                avatarUri = null,
                avatarBase64 = null
            )
        }
    }

    fun logout() {
        ApplicationManager.logoutUser()
    }
    private fun uriToBase64(uri: Uri): String? {
        return try {
            val inputStream = getApplication<Application>()
                .contentResolver
                .openInputStream(uri)

            val bytes = inputStream?.use {
                it.readBytes()
            } ?: return null

            val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)

            "data:image/jpeg;base64,$base64"
        } catch (e: Exception) {
            null
        }
    }

    fun uriToMultipartBodyPart(
        context: Context,
        uri: Uri,
        partName: String = "file"
    ): MultipartBody.Part {
        val contentResolver = context.contentResolver

        val mimeType = contentResolver.getType(uri) ?: "image/jpeg"

        val inputStream = contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open input stream from Uri")

        val bytes = inputStream.use { it.readBytes() }

        val requestBody = bytes.toRequestBody(
            contentType = mimeType.toMediaTypeOrNull()
        )

        return MultipartBody.Part.createFormData(
            name = partName,
            filename = "avatar.jpg",
            body = requestBody
        )
    }
}