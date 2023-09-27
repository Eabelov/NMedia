package ru.netology.nmedia.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.enum.AttachmentType

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorAvatar: String?,
    val author: String,
    val content: String,
    val published: Long,
    val likedByMe: Boolean,
    val sharedByMe: Boolean,
    val likes: Int = 0,
    val shares: Int = 0,
    val views: Int = 0,
    val video: String? = null,
    val videoUrl: String? = null,
) {
    fun toDto() = Post(
        id = id,
        authorAvatar = authorAvatar,
        author = author,
        content = content,
        published = published,
        likedByMe = likedByMe,
        sharedByMe = sharedByMe,
        likes = likes,
        shareds = shares,
        viewers = views,
        video = video,
        videoUrl = videoUrl
    )

    companion object {
        fun fromDto(dto: Post, hidden: Boolean) =
            PostEntity(
                id = dto.id,
                authorAvatar = dto.authorAvatar,
                author = dto.author,
                content = dto.content,
                published = dto.published,
                likedByMe = dto.likedByMe,
                sharedByMe = dto.sharedByMe,
                likes = dto.likes,
                shares = dto.shareds,
                views = dto.viewers,
                video = dto.video,
            )

    }
}