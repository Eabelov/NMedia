package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityEditPostBinding


class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.editPost.requestFocus()

        val postContent = intent.getStringExtra("content") // Получаем содержимое поста из интента
        binding.editPost.setText(postContent) // Устанавливаем полученное содержимое в поле ввода

        binding.ok.setOnClickListener {
            val intent = Intent()
            if (binding.editPost.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.editPost.text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content) // Передаем измененное содержимое поста
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }


        binding.cancelEdit.setOnClickListener {
            finish()
        }
    }
}
