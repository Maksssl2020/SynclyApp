package pl.skomunikacja.synclyapp.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.skomunikacja.synclyapp.config.RetrofitClient
import pl.skomunikacja.synclyapp.helpers.ApiLikesHelper
import pl.skomunikacja.synclyapp.model.PostComment

class CommentViewModel(initialComment: PostComment) : ViewModel() {
    private val apiLikesHelper = ApiLikesHelper(RetrofitClient.apiLikesService)

    private val _comment = MutableStateFlow(initialComment)
    val comment = _comment.asStateFlow()

    fun toggleLike(userId: Long) {
       viewModelScope.launch {
           val currentComment = _comment.value
           val isLiked = currentComment.likesBy.contains(userId)

           val success =
               if (isLiked)
                   apiLikesHelper.unlikeComment(userId, currentComment.id)
               else
                   apiLikesHelper.likeComment(userId, currentComment.id)

           if (success) {
               _comment.value = updateComment(currentComment, userId, isLiked)
           }
       }
    }

    fun updateComment(comment: PostComment, userId: Long, wasActive: Boolean): PostComment {
        val newLikes = if (wasActive) comment.likesBy - userId else comment.likesBy + userId
        return comment.copyWith(newLikes)
    }
}