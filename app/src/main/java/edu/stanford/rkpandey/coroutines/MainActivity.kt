package edu.stanford.rkpandey.coroutines

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import edu.stanford.rkpandey.coroutines2.BlogService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigInteger
import java.util.*
import kotlin.system.measureTimeMillis

private const val TAG = "MainActivity"
private const val BASE_URL = "https://jsonplaceholder.typicode.com"

data class Post(val id: Int, val userId: Int, val title: String)
data class User(val id: Int, val name: String, val email: String)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i(TAG, "onCreate current thread: ${Thread.currentThread().name}")
        btnNetwork.setOnClickListener {
            doApiRequests()
        }

        btnCompute.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val timeTaken = doExpensiveWork()
            progressBar.visibility = View.INVISIBLE
            textView.text = timeTaken
        }
    }

    private fun doExpensiveWork(): String {
        val timeTakenMillis = measureTimeMillis { BigInteger.probablePrime(2200, Random()) }
        return "Time taken (ms): $timeTakenMillis"
    }

    private fun doApiRequests() {
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
        val blogService = retrofit.create(BlogService::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            Log.i(TAG, "doApiRequests coroutine thread: ${Thread.currentThread().name}")
            try {
                val blogPost = blogService.getPost(1)
                val user = blogService.getUser(blogPost.userId)
                val postsByUser = blogService.getPostsByUser(user.id)
                textView.text = "User ${user.name} made ${postsByUser.size} posts"
            } catch (exception: Exception) {
                Log.e(TAG, "Exception $exception")
            }
        }
    }
}