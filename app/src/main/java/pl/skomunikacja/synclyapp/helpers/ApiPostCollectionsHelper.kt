package pl.skomunikacja.synclyapp.helpers

import pl.skomunikacja.synclyapp.model.PostCollection
import pl.skomunikacja.synclyapp.service.ApiPostCollectionsService

class ApiPostCollectionsHelper(
    private val api: ApiPostCollectionsService
) {

    suspend fun getUserAllPostCollections(userId: Long): List<PostCollection> =
        api.getUserAllPostCollections(userId)

    suspend fun getPostCollectionById(postCollectionId: Long): PostCollection =
        api.getPostCollectionById(postCollectionId)

    suspend fun savePostToCollection(postCollectionId: Long, postId: Long): Boolean =
        api.savePostToCollection(postCollectionId, postId).isSuccessful

    suspend fun unsavePostFromCollection(postCollectionId: Long, postId: Long): Boolean =
        api.unsavePostFromCollection(postCollectionId, postId).isSuccessful
}