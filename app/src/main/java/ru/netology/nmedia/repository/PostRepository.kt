package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAllAsync(callback: Callback<List<Post>>)
    fun saveAsync(post: Post, Callback: Callback<Post>)
    fun likeByIdAsync(post: Post, Callback: Callback<Post>)
    fun removeByIdAsync(id: Long, Callback: Callback<Unit>)
    fun shareById(id: Long)
    fun viewsById(id: Long)
    interface Callback<T> {
        fun onSuccess(posts: T) {}
        fun onError(e: Exception) {}
    }
}