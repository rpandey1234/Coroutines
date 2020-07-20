import edu.stanford.rkpandey.coroutines.Post
import edu.stanford.rkpandey.coroutines.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BlogService {
    @GET("/posts/{id}")
    fun getPost(@Path("id") postId: Int): Call<Post>

    @GET("/users/{id}")
    fun getUser(@Path("id") userId: Int): Call<User>

    @GET("/users/{id}/posts")
    fun getPostsByUser(@Path("id") userId: Int): Call<List<Post>>
}