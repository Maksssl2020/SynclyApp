package pl.skomunikacja.synclyapp.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.skomunikacja.synclyapp.config.RetrofitClient
import pl.skomunikacja.synclyapp.helpers.ApiPostCollectionsHelper
import pl.skomunikacja.synclyapp.helpers.ApplicationManager
import pl.skomunikacja.synclyapp.model.PostCollection
import pl.skomunikacja.synclyapp.model.post.Post

class PostCollectionsViewModel : ViewModel() {
    private val apiPostCollectionsHelper = ApiPostCollectionsHelper(RetrofitClient.apiPostCollectionsService)
    val postCollections = ApplicationManager.userPostCollections

    private val _selectedPostCollection = MutableStateFlow<PostCollection?>(null)
    val selectedPostCollection = _selectedPostCollection.asStateFlow()


    fun loadPostCollectionById(collectionId: Long) {
        viewModelScope.launch {
            val found = postCollections.value.find { it.id == collectionId }
            if (found != null) {
                _selectedPostCollection.value = found
            } else {
                val fromApi = apiPostCollectionsHelper.getPostCollectionById(collectionId)
                _selectedPostCollection.value = fromApi
            }
        }
    }

    fun savePostToCollection(postCollectionId: Long, post: Post, onSuccess: () -> Unit){
        viewModelScope.launch {
            val result = apiPostCollectionsHelper.savePostToCollection(postCollectionId, post.id)
            if (result) {
                ApplicationManager.addPostToCollection(postCollectionId, post)
                onSuccess()
            }
        }
    }

    fun unsavePostFromCollection(postCollectionId: Long, postId: Long, onSuccess: () -> Unit){
        viewModelScope.launch {
            val result = apiPostCollectionsHelper.unsavePostFromCollection(postCollectionId, postId)
            if (result) {
                ApplicationManager.removePostFromCollection(postCollectionId, postId)
                onSuccess()
            }
        }
    }
}