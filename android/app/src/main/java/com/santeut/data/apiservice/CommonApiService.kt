package com.santeut.data.apiservice

import com.santeut.data.model.CustomResponse
import com.santeut.data.model.request.CreateCommentRequest
import com.santeut.data.model.response.CommentListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommonApiService {
    @POST("/api/common/comment/{postId}/{postType}")
    suspend fun createComment(
        @Path("postId") postId: Int,
        @Path("postType") postType: String,
        @Body createCommentRequest: CreateCommentRequest
    ): CustomResponse<Unit>

    @GET("/api/common/comment/{postId}/{postType}")
    suspend fun getComments(
        @Path("postId") postId: Int,
        @Path("postType") postType: String,
    ): CustomResponse<CommentListResponse>

}