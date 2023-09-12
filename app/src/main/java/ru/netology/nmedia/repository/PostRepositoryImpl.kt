package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.internal.EMPTY_REQUEST
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit


class PostRepositoryImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    private companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }


    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(gson.fromJson(body, typeToken.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun saveAsync(post: Post, callback: PostRepository.Callback<Post>) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object: Callback{
                override fun onResponse(call: Call, response: Response) {
                    if(!response.isSuccessful){
                        callback.onError(Exception(response.message))
                    }
                    val body = requireNotNull(response.body?.string()){"body is null"}
                    callback.onSuccess(gson.fromJson(body, Post::class.java))
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun likeByIdAsync(post: Post, callback: PostRepository.Callback<Post>) {
        val id = post.id
        val request: Request = if (post.likedByMe) {
            Request.Builder()
                .delete("".toRequestBody())
        } else {
            Request.Builder()
                .post("".toRequestBody())
        }
            .url("${BASE_URL}/api/slow/posts/${id}/likes")
            .build()
        client.newCall(request)
            .enqueue(object: Callback{
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if(!response.isSuccessful){
                        callback.onError(Exception(response.message))
                    }
                    val body = requireNotNull(response.body?.string()){"body is null"}
                    callback.onSuccess(gson.fromJson(body, Post::class.java))
                }
            })
    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.Callback<Unit>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .enqueue(object: Callback{
                override fun onResponse(call: Call, response: Response) {
                    if(!response.isSuccessful){
                        callback.onError(Exception(response.message))
                    }
                    callback.onSuccess(Unit)
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }


    override fun shareById(id: Long) {
        TODO()
    }

    override fun viewsById(id: Long) {
        TODO()
    }
}