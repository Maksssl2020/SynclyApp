package pl.skomunikacja.synclyapp.model

import android.net.Uri

data class AvatarUploadState(
    val avatarUri: Uri? = null,
    val avatarBase64: String? = null,
    val uploadingAvatar: Boolean = false
)
