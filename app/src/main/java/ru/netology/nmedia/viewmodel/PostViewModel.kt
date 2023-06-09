package ru.netology.nmedia.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

private val empty = Post(
    id = 0,
    content =  "",
    author = "",
    likedByMe = false,
    published = ""
)
class PostViewModel: ViewModel() {
    //val isEditing = MutableLiveData<Boolean>().apply { value = false }
    private val _isEditing = MutableLiveData(false)
    val isEditing: LiveData<Boolean> = _isEditing
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    val edited = MutableLiveData(empty)
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun viewsById(id: Long) = repository.viewsById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }
    fun edit(post: Post) {
        edited.value = post
    }
    fun changeContent(content: String){
        val text = content.trim()
        if (edited.value?.content != text) {
            edited.value = edited.value?.copy(content = text)
        }
    }

    fun cancelEditing() {
        _isEditing.value = false
    }
    fun getCancelButtonVisibility(): Int {
        return if (_isEditing.value == true) View.VISIBLE else View.GONE
    }
}