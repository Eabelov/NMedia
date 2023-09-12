package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: Long,
    var likedByMe: Boolean = false,
    var likes: Int = 0,
    var shareds: Int = 0,
    var viewers: Int = 0,
    var videoUrl: String?,
    val authorAvatar: String?
)
