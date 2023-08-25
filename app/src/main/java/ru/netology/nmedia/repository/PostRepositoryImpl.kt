package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST
import ru.netology.nmedia.dto.Post
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

    override fun getAll(): List<Post> {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typeToken.type)
            }
    }

    override fun likeById(id: Long): Post {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts/$id/likes")
            .post(EMPTY_REQUEST)
            .build()

        return client.newCall(request)
            .execute()
            .body?.string()
            .let { gson.fromJson(it, Post::class.java)}  ?: throw RuntimeException("body is null")
    }

    override fun unLikeById(id: Long): Post {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts/$id/likes")
            .delete()
            .build()

        return client.newCall(request)
            .execute()
            .body?.string()
            .let { gson.fromJson(it, Post::class.java)}  ?: throw RuntimeException("body is null")
    }

    override fun shareById(id: Long) {
        TODO()
    }

    override fun viewsById(id: Long) {
        TODO()
    }

    override fun save(post: Post) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .execute()
            .let { it.body?.string() ?: error("Body is null") }
            .let { gson.fromJson(it, Post::class.java) }
    }

    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts/$id")
            .delete()
            .build()

        client.newCall(request)
            .execute()
    }
}