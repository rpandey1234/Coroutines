package edu.stanford.rkpandey.coroutines

import BlogService
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "MainActivity"
private const val BASE_URL = "https://jsonplaceholder.typicode.com"

data class Post(val id: Int, val userId: Int, val title: String)
data class User(val id: Int, val name: String, val email: String)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNetwork.setOnClickListener {
            doApiRequests()
        }
    }

    private fun doApiRequests() {
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
        val blogService = retrofit.create(BlogService::class.java)
        blogService.getPost(1).enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.e(TAG, "onFailure $t")
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                val blogPost = response.body()
                if (blogPost == null) {
                    Log.e(TAG, "Did not receive valid response body")
                    return
                }
                blogService.getUser(blogPost.userId).enqueue(object : Callback<User> {
                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.e(TAG, "onFailure $t")
                    }

                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        val user = response.body()
                        if (user == null) {
                            Log.e(TAG, "Did not receive valid response body")
                            return
                        }
                        blogService.getPostsByUser(user.id).enqueue(object : Callback<List<Post>> {
                            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                                Log.e(TAG, "onFailure $t")
                            }

                            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                                val postsByUser = response.body()
                                if (postsByUser == null) {
                                    Log.e(TAG, "Did not receive valid response body")
                                    return
                                }
                                Log.i(TAG, "Done with all network requests!")
                                textView.text = "User ${user.name} made ${postsByUser.size} posts"
                            }
                        })
                    }
                })
            }
        })
    }
}