package edu.stanford.rkpandey.coroutines2

import edu.stanford.rkpandey.coroutines.Post
import edu.stanford.rkpandey.coroutines.User
import retrofit2.http.GET
import retrofit2.http.Path

interface BlogService {
    @GET("/posts/{id}")
    suspend fun getPost(@Path("id") postId: Int): Post

    @GET("/users/{id}")
    suspend fun getUser(@Path("id") userId: Int): User

    @GET("/users/{id}/posts")
    suspend fun getPostsByUser(@Path("id") userId: Int): List<Post>
}