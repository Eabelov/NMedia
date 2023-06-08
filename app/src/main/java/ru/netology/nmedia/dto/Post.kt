package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 0,
    var shareds: Int = 1000,
    var viewers: Int = 0,
    var likedByMe: Boolean = false
)
