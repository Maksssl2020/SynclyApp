package pl.skomunikacja.synclyapp.helpers

import pl.skomunikacja.synclyapp.model.FriendStatus
import pl.skomunikacja.synclyapp.model.PostCollection
import pl.skomunikacja.synclyapp.model.PostCollectionRequest
import pl.skomunikacja.synclyapp.service.ApiPostCollectionsService

class ApiPostCollectionsHelper(
    private val api: ApiPostCollectionsService
) {

    suspend fun getUserAllPostCollections(userId: Long): List<PostCollection> {
        return try {
            val response = api.getUserAllPostCollections(userId)
            response.isSuccessful
            response.body() ?: emptyList()
        } catch (ex: Exception) {
            println("Failed to fetch post collections: ${ex.message}")
            emptyList()
        }
    }


    suspend fun getPostCollectionById(postCollectionId: Long): PostCollection? {
        return try {
            val response = api.getPostCollectionById(postCollectionId)
            response.isSuccessful
            response.body()
        } catch (ex: Exception) {
            println("Failed to fetch post collection: ${ex.message}")
            null
        }
    }

    suspend fun savePostToCollection(postCollectionId: Long, postId: Long): Boolean =
        api.savePostToCollection(postCollectionId, postId).isSuccessful

    suspend fun createPostCollection(userId: Long, postCollectionRequest: PostCollectionRequest): PostCollection?  {
        return try {
            val response =  api.createPostCollection(userId, postCollectionRequest);
            response.isSuccessful
            response.body()
        } catch (ex: Exception) {
            println("Failed to create post collection: ${ex.message}")
            null
        }
    }

    suspend fun unsavePostFromCollection(postCollectionId: Long, postId: Long): Boolean =
        api.unsavePostFromCollection(postCollectionId, postId).isSuccessful
}