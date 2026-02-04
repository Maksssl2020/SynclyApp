package pl.skomunikacja.synclyapp.helpers

import okhttp3.MultipartBody
import okhttp3.RequestBody
import pl.skomunikacja.synclyapp.model.post.Post
import pl.skomunikacja.synclyapp.service.ApiPostsService
import retrofit2.Response

class ApiPostsHelper(
    private val api: ApiPostsService
) {

    suspend fun searchPosts(query: String): List<Post> =
        api.searchPosts(query)

    suspend fun getUserPosts(userId: Long): List<Post> =
        api.getUserPostsByUserId(userId)

    suspend fun getForYouFeed(userId: Long, offset: Int, limit: Int): List<Post> =
        api.getForYouFeed(userId, offset, limit)

    suspend fun getFollowingFeed(userId: Long, offset: Int, limit: Int): List<Post> =
        api.getFollowingFeed(userId, offset, limit)

    suspend fun sharePost(userId: Long, postId: Long): Boolean =
        api.sharePost(userId, postId).isSuccessful

    suspend fun unsharePost(userId: Long, postId: Long): Boolean =
        api.unsharePost(userId, postId).isSuccessful

    suspend fun createPost(userId: Long, data: RequestBody, files: List<MultipartBody.Part>?): Response<Unit> =
        api.createPost(userId, data, files)
}