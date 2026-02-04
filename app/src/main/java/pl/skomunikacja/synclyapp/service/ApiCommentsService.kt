package pl.skomunikacja.synclyapp.service

import pl.skomunikacja.synclyapp.model.CommentReplyRequest
import pl.skomunikacja.synclyapp.model.CommentRequest
import pl.skomunikacja.synclyapp.model.PostComment
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiCommentsService {

    @GET("api/v1/comments/{postId}")
    suspend fun getAllPostComments(
        @Path("postId") postId: Long
    ): List<PostComment>

    @POST("api/v1/comments/add-comment-to-post")
    suspend fun addCommentToPost(
        @Body postCommentRequest: CommentRequest
    ): Response<PostComment>

    @POST("api/v1/comments/add-comment-reply")
    suspend fun addCommentReply(
        @Body postCommentRequest: CommentReplyRequest
    ): Response<PostComment>
}