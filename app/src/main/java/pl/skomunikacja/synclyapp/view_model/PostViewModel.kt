package pl.skomunikacja.synclyapp.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.skomunikacja.synclyapp.config.RetrofitClient
import pl.skomunikacja.synclyapp.helpers.ApiCommentsHelper
import pl.skomunikacja.synclyapp.helpers.ApiLikesHelper
import pl.skomunikacja.synclyapp.helpers.ApiPostCollectionsHelper
import pl.skomunikacja.synclyapp.helpers.ApiPostsHelper
import pl.skomunikacja.synclyapp.model.CommentReplyRequest
import pl.skomunikacja.synclyapp.model.CommentRequest
import pl.skomunikacja.synclyapp.model.PostComment
import pl.skomunikacja.synclyapp.model.post.Post
import pl.skomunikacja.synclyapp.model.post.PostActionType

class PostViewModel(initialPost: Post) : ViewModel() {
    private val apiPostsHelper = ApiPostsHelper(RetrofitClient.apiPostsService)
    private val apiCommentsHelper = ApiCommentsHelper(RetrofitClient.apiCommentsService)
    private val apiLikesHelper = ApiLikesHelper(RetrofitClient.apiLikesService)
    private val apiPostCollectionHelper = ApiPostCollectionsHelper(RetrofitClient.apiPostCollectionsService)

    private val _post = MutableStateFlow(initialPost)
    val post = _post.asStateFlow()

    private val _postComments = MutableStateFlow(emptyList<PostComment>())
    val postComments = _postComments.asStateFlow()

    fun showPostComments() {
        viewModelScope.launch {
            val allPostComments = apiCommentsHelper.getAllPostComments(post.value.id)
            _postComments.value = allPostComments
        }
    }

    fun addCommentToPost(
        request: CommentRequest,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val response = apiCommentsHelper.addCommentToPost(request)
            if (response.isSuccessful && response.body() != null) {
                _postComments.value += response.body()!!
                post.value.copyWith(
                    commentsCount = post.value.commentsCount + 1
                )
            }

            onResult(response.isSuccessful)
        }
    }

    fun addCommentReply(
        request: CommentReplyRequest,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val response = apiCommentsHelper.addCommentReply(request)
            if (response.isSuccessful && response.body() != null) {
                val reply = response.body()!!

                _postComments.value = _postComments.value.map { comment ->
                    if (comment.id == request.parentCommentId) {
                        comment.copy(
                            replies = comment.replies + reply
                        )
                    } else {
                        comment
                    }
                }

                post.value.copyWith(
                    commentsCount = post.value.commentsCount + 1
                )

                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    fun likeUnlikePostComment(userId: Long, commentId: Long) {
        viewModelScope.launch {
            val foundComment = findCommentById(_postComments.value, commentId) ?: return@launch

            val isLikedByUser = foundComment.likesBy.contains(userId)

            val result: Boolean = if (isLikedByUser) {
                apiLikesHelper.unlikeComment(userId, commentId)
            } else {
                apiLikesHelper.likeComment(userId, commentId)
            }

            if (result) {
                _postComments.update { comments ->
                    updateCommentLikeRecursively(
                        comments = comments,
                        commentId = commentId,
                        userId = userId,
                        wasLiked = isLikedByUser
                    )
                }
            }
        }
    }

    private fun findCommentById(
        comments: List<PostComment>,
        commentId: Long
    ): PostComment? {
        comments.forEach { comment ->
            if (comment.id == commentId) {
                return comment
            }

            val foundReply = findCommentById(comment.replies, commentId)
            if (foundReply != null) {
                return foundReply
            }
        }

        return null
    }

    private fun updateCommentLikeRecursively(
        comments: List<PostComment>,
        commentId: Long,
        userId: Long,
        wasLiked: Boolean
    ): List<PostComment> {
        return comments.map { comment ->
            if (comment.id == commentId) {
                comment.copy(
                    likesBy = if (wasLiked) {
                        comment.likesBy - userId
                    } else {
                        comment.likesBy + userId
                    }
                )
            } else {
                comment.copy(
                    replies = updateCommentLikeRecursively(
                        comments = comment.replies,
                        commentId = commentId,
                        userId = userId,
                        wasLiked = wasLiked
                    )
                )
            }
        }
    }

    fun toggleAction(userId: Long, action: PostActionType) {
        viewModelScope.launch {
            val currentPost = _post.value

            val isActive = when (action) {
                PostActionType.LIKE -> currentPost.likesBy.contains(userId)
                PostActionType.SAVE -> currentPost.savedBy.contains(userId)
                PostActionType.SHARE -> currentPost.sharedBy.contains(userId)
            }

            val success = try {
                when (action) {
                    PostActionType.LIKE ->
                        if (isActive) apiLikesHelper.unlikePost(userId, currentPost.id)
                        else apiLikesHelper.likePost(userId, currentPost.id)

                    PostActionType.SAVE ->
                        TODO()
//                        if (isActive) apiPostCollectionHelper.unsavePostFromCollection(currentPost.id.toLong())
//                        else apiHelper.savePost(currentPost.id.toLong())

                    PostActionType.SHARE ->
                        if (isActive) apiPostsHelper.unsharePost(userId, currentPost.id)
                        else apiPostsHelper.sharePost(userId, currentPost.id)

                }
            } catch (e: Exception) {
                Log.e("PostVM", "toggleAction error", e)
                false
            }

            if (success) {
                _post.value = updatePost(currentPost, userId, action, isActive)
            }
        }
    }

    private fun updatePost(post: Post, userId: Long, action: PostActionType, wasActive: Boolean): Post {
        return when (action) {
            PostActionType.LIKE -> post.copyWith(
                likesBy = if (wasActive) post.likesBy - userId else post.likesBy + userId
            )
            PostActionType.SAVE -> post.copyWith(
                savedBy = if (wasActive) post.savedBy - userId else post.savedBy + userId
            )
            PostActionType.SHARE -> post.copyWith(
                sharedBy = if (wasActive) post.sharedBy - userId else post.sharedBy + userId
            )

        }
    }
}