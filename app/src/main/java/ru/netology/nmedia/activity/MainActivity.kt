package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel
import java.text.DecimalFormat
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    //var _binding: ActivityMainBinding? = null
    //val binding: ActivityMainBinding
        //get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter ({
            viewModel.likeById(it.id)
        },{
            viewModel.shareById(it.id)
        },{
            viewModel.viewsById(it.id)
        })
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.list = posts
        }

    }

    /*binding.like.setOnClickListener {
        viewModel.like()
    }

    binding.share.setOnClickListener {
        viewModel.share()
    }

    binding.views.setOnClickListener {
        viewModel.views()
    }

} }*/
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

