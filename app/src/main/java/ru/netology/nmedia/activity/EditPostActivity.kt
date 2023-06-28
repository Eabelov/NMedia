package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.netology.nmedia.databinding.ActivityEditPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class EditPostActivity : AppCompatActivity() {
    //private val viewModel: PostViewModel by viewModels()
    private lateinit var viewModel: PostViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.editPost.requestFocus()
        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)

        val postContent = intent.getStringExtra("content") // Получаем содержимое поста из интента
        binding.editPost.setText(postContent) // Устанавливаем полученное содержимое в поле ввода

        binding.ok.setOnClickListener {
            val intent = Intent()
            if (binding.editPost.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.editPost.text.toString()
                intent.putExtra("content", content) // Передаем измененное содержимое поста
                intent.putExtra("postId", postContent) // Передаем идентификатор поста
                //setResult(Activity.RESULT_OK, intent)
                viewModel.changeContent(content)

            }
            setResult(Activity.RESULT_OK, intent)
            viewModel.save()
            finish()
        }


        binding.cancelEdit.setOnClickListener {
            finish()
        }
    }
}
