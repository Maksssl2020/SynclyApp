package pl.skomunikacja.synclyapp.helpers

import pl.skomunikacja.synclyapp.model.CommentReplyRequest
import pl.skomunikacja.synclyapp.model.CommentRequest
import pl.skomunikacja.synclyapp.model.PostComment
import pl.skomunikacja.synclyapp.service.ApiCommentsService
import retrofit2.Response

class ApiCommentsHelper(
    private val api: ApiCommentsService
) {

    suspend fun getAllPostComments(postId: Long): List<PostComment> =
        api.getAllPostComments(postId)

    suspend fun addCommentToPost(commentRequest: CommentRequest): Response<PostComment> =
        api.addCommentToPost(commentRequest)

    suspend fun addCommentReply(commentReplyRequest: CommentReplyRequest): Response<PostComment> =
        api.addCommentReply(commentReplyRequest)
}