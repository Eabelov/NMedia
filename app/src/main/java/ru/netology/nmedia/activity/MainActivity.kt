package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel
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

        val viewModel by viewModels<PostViewModel> ()
        viewModel.data.observe(this) {post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                binding.avatar.setImageResource(R.drawable.ic_netology_48dp)
                binding.menu.setImageResource(R.drawable.ic_menu)
                binding.views.setImageResource(R.drawable.ic_views_24)
                binding.share.setImageResource(R.drawable.ic_baseline_share_24)
                like.setImageResource(if (!post.likedByMe) R.drawable.ic_baseline_favorite_border_24 else R.drawable.ic_baseline_favorite_24)
                likeCount?.text = formatNumber(post.likes)
                shareCount?.text = formatNumber(post.shareds)
                viewsCount?.text = formatNumber(post.viewers)
            }
        }
        binding.like.setOnClickListener {
            viewModel.like()
        }

        binding.share.setOnClickListener {
            viewModel.share()
        }

        binding.views.setOnClickListener {
            viewModel.views()
        }

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

