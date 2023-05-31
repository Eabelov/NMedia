package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import java.text.DecimalFormat
import kotlin.math.pow

class PostRepositoryInMemoryImpl: PostRepository {

    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        published = "21 мая в 18:36",
        likes = 0,
        shareds = 0,
        viewers = 1000,
        likedByMe = false
    )

    private val data = MutableLiveData(post)

    override fun get() = data

    override fun like() {
        post = post.copy(likedByMe = !post.likedByMe, likes = if (post.likedByMe) post.likes - 1 else post.likes + 1)
        data.value = post
    }

    override fun share() {
        post = post.copy(shareds = post.shareds + 1)
        data.value = post
    }

    override fun views() {
        post = post.copy(viewers = post.viewers + 1)
        data.value = post
    }
}