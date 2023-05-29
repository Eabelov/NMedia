package ru.netology.nmedia

import android.os.Bundle
import android.service.voice.VoiceInteractionSession.ActivityId
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import java.text.DecimalFormat
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    var _binding: ActivityMainBinding? = null
    val binding: ActivityMainBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likes = 0,
            shareds = 0,
            viewers = 0,
            likedByMe = false
        )
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            binding.avatar.setImageResource(R.drawable.ic_netology_48dp)
            binding.menu.setImageResource(R.drawable.ic_menu)
            binding.views.setImageResource(R.drawable.ic_views_24)
            binding.share.setImageResource(R.drawable.ic_baseline_share_24)
            setLikeImage(post.likedByMe)

            likeCount?.text = formatNumber(post.likes)
            shareCount?.text = formatNumber(post.shareds)
            viewsCount?.text = formatNumber(post.viewers)


            setLikeImage(post.likedByMe)

            binding.like.setOnClickListener {
                updateLike(post)
            }

            binding.share.setOnClickListener {
                post.shareds++
                shareCount?.text = formatNumber(post.shareds)
            }

            binding.views.setOnClickListener {
                post.viewers++
                viewsCount?.text = formatNumber(post.viewers)
            }
        }
    }

    private fun setLikeImage(liked: Boolean) {
        if (liked) {
            binding.like?.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.like?.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun updateLike(post: Post) {
        post.likedByMe = !post.likedByMe
        if (post.likedByMe) {
            post.likes++
            setLikeImage(true)
        } else {
            post.likes--
            setLikeImage(false)
        }
        binding.likeCount?.text = formatNumber(post.likes)
    }
    private fun formatNumber(number: Int): String {
        if (number < 1000) {
            return number.toString()
        }

        val suffixes = arrayOf("", "K", "M")
        val formatter = DecimalFormat("#,##0.#")
        val exp = (Math.log10(number.toDouble()) / 3).toInt()
        return formatter.format(number / 10.0.pow(exp * 3)) + suffixes[exp]
    }
}

