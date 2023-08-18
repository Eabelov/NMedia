package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val shareds: Int = 0,
    val viewers: Int = 0,
    val videoUrl: String?
) {
    fun toDto() = Post(id, author, content, published, likedByMe, likes, shareds, viewers, videoUrl)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.author, dto.content, dto.published, dto.likedByMe, dto.likes, dto.shareds, dto.viewers, dto.videoUrl)

    }
}