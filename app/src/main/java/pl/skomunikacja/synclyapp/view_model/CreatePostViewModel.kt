package pl.skomunikacja.synclyapp.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pl.skomunikacja.synclyapp.config.RetrofitClient
import pl.skomunikacja.synclyapp.config.RetrofitClient.gson
import pl.skomunikacja.synclyapp.helpers.ApiPostsHelper
import pl.skomunikacja.synclyapp.helpers.ApiTagsHelper
import pl.skomunikacja.synclyapp.model.Tag
import pl.skomunikacja.synclyapp.model.post.PhotoPostRequest
import pl.skomunikacja.synclyapp.model.post.PostRequest
import java.io.File

class CreatePostViewModel : ViewModel() {
    private val apiPostsHelper = ApiPostsHelper(RetrofitClient.apiPostsService)
    private val apiTagsHelper = ApiTagsHelper(RetrofitClient.apiTagsService)
    private val _tags = MutableStateFlow(emptyList<Tag>())
    val tags = _tags.asStateFlow()

    fun fetchAllTags() {
        viewModelScope.launch {
            val allTags = apiTagsHelper.getAllTags()
            _tags.value = allTags
        }
    }

    fun createPost(
        userId: Long,
        request: PostRequest,
        files: List<File>? = null,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val json = gson.toJson(request)
                val dataPart = json.toRequestBody("application/json".toMediaType())

                val fileParts = files?.map { file ->
                    val type = if (request is PhotoPostRequest) "image/*" else "video/*"
                    MultipartBody.Part.createFormData(
                        "files",
                        file.name,
                        file.asRequestBody(type.toMediaTypeOrNull())
                    )
                }

                val response = apiPostsHelper.createPost(userId, dataPart, fileParts)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError(Exception("Error: ${response.errorBody()}"))
                }
            } catch (exception: Exception) {
                onError(exception)
            }
        }
    }
}