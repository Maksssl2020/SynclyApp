package pl.skomunikacja.synclyapp.view_model

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.skomunikacja.synclyapp.config.RetrofitClient
import pl.skomunikacja.synclyapp.helpers.ApiPostCollectionsHelper
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.helpers.PostCollectionsManager
import pl.skomunikacja.synclyapp.model.PostCollection
import pl.skomunikacja.synclyapp.model.PostCollectionRequest
import pl.skomunikacja.synclyapp.model.post.Post

class PostCollectionsViewModel : ViewModel() {
    private val apiPostCollectionsHelper = ApiPostCollectionsHelper(RetrofitClient.apiPostCollectionsService)
    val postCollections = PostCollectionsManager.userPostCollections

    private val _selectedPostCollection = MutableStateFlow<PostCollection?>(null)
    val selectedPostCollection = _selectedPostCollection.asStateFlow()

    private val _creatingPostCollection = MutableStateFlow(false)
    val creatingPostCollection = _creatingPostCollection.asStateFlow()

    private val _loadingPostCollection = MutableStateFlow(false)
    val loadingPostCollection = _loadingPostCollection.asStateFlow()

    fun loadPostCollectionById(collectionId: Long) {
        viewModelScope.launch {
            _loadingPostCollection.value = true

            try {
                val found = postCollections.value.find { it.id == collectionId }
                if (found != null) {
                    _selectedPostCollection.value = found
                } else {
                    val fromApi = apiPostCollectionsHelper.getPostCollectionById(collectionId)
                    _selectedPostCollection.value = fromApi
                }
            } catch (ex: Exception) {
                Log.e("loadPostCollectionById", "Error Loading Post Collection", ex)
            } finally {
                _loadingPostCollection.value = false
            }
        }
    }

    fun savePostToCollection(postCollectionId: Long, post: Post, onSuccess: () -> Unit){
        viewModelScope.launch {
            val result = apiPostCollectionsHelper.savePostToCollection(postCollectionId, post.id)
            if (result) {
                PostCollectionsManager.addPostToCollection(postCollectionId, post)
                onSuccess()
            }
        }
    }

    fun unsavePostFromCollection(postCollectionId: Long, postId: Long, onSuccess: () -> Unit){
        viewModelScope.launch {
            val result = apiPostCollectionsHelper.unsavePostFromCollection(postCollectionId, postId)
            if (result) {
                PostCollectionsManager.removePostFromCollection(postCollectionId, postId)
                onSuccess()
            }
        }
    }

    fun createPostCollection(userId: Long, title: String, color: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _creatingPostCollection.value = true

            try {
                val postCollectionRequest = PostCollectionRequest(title, color)

                val result =
                    apiPostCollectionsHelper.createPostCollection(userId, postCollectionRequest)

                if (result != null) {
                    val updatedCollections =
                        apiPostCollectionsHelper.getUserAllPostCollections(userId)

                    PostCollectionsManager.changeUserPostCollections(updatedCollections)

                    onSuccess()
                }
            } catch (ex: Exception) {
                Log.e("createPostCollection", "Error Create Post Collection", ex)
            } finally {
                _creatingPostCollection.value = false
            }
        }
    }
}