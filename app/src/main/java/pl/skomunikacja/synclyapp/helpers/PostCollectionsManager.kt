package pl.skomunikacja.synclyapp.helpers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pl.skomunikacja.synclyapp.model.PostCollection
import pl.skomunikacja.synclyapp.model.post.Post

object PostCollectionsManager {

    private val _userPostCollections = MutableStateFlow(emptyList<PostCollection>())
    val userPostCollections = _userPostCollections.asStateFlow()

    fun changeUserPostCollections(userPostCollections: List<PostCollection>) {
        _userPostCollections.value = userPostCollections
    }

    fun addPostToCollection(postCollectionId: Long, post: Post) {
        val alreadyInNonDefault = checkIfCollectionsContainsPost(post.id)

        _userPostCollections.value = _userPostCollections.value.map { collection ->
            when {
                collection.id == postCollectionId ->
                    collection.copy(posts = ArrayList(collection.posts + post))

                collection.default && !alreadyInNonDefault &&
                        collection.posts.none { it.id == post.id } ->
                    collection.copy(posts = ArrayList(collection.posts + post))

                else -> collection
            }
        }
    }

    fun removePostFromCollection(postCollectionId: Long, postId: Long) {
        _userPostCollections.value = _userPostCollections.value.map { collection ->
            if (collection.id == postCollectionId) {
                val updatedPosts = collection.posts.filterNot { it.id == postId }
                collection.copy(posts = ArrayList(updatedPosts))
            } else collection
        }

        val stillInNonDefault = checkIfCollectionsContainsPost(postId)

        if (!stillInNonDefault) {
            _userPostCollections.value = _userPostCollections.value.map { collection ->
                if (collection.default) {
                    val updatedPosts = collection.posts.filterNot { it.id == postId }
                    collection.copy(posts = ArrayList(updatedPosts))
                } else collection
            }
        }
    }

    fun checkIfCollectionsContainsPost(postId: Long): Boolean {
        return _userPostCollections.value
            .filterNot { it.default }
            .any { collection -> collection.posts.any { it.id == postId } }
    }
}