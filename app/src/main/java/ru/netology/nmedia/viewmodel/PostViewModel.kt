package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.lang.Exception
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    videoUrl = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel> = _data
    val edited = MutableLiveData(empty)

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit> = _postCreated


    fun likeById(post: Post) {
        thread {
            try {
                val updatedPost = if (post.likedByMe) {
                    repository.unLikeById(post.id)
                } else {
                    repository.likeById(post.id)
                }
                val newPosts = _data.value?.posts?.map {
                    if (it.id == post.id) {
                        updatedPost
                    } else {
                        it
                    }
                }.orEmpty()
                _data.postValue(_data.value?.copy(posts = newPosts))
            } catch (e: Exception){
                e.printStackTrace()
            }
            }
        }

        fun sharedById(id: Long) = repository.shareById(id)
        fun removeById(id: Long) {
            thread {
                val old = _data.value

                _data.postValue(
                    old?.copy(
                        posts = old.posts.filter {
                            it.id != id

                        }
                    )
                )
                try {
                    repository.removeById(id)
                } catch (e: Exception) {
                    _data.postValue(old)
                }
            }
        }

        init {
            loadPosts()
        }

        fun loadPosts() {
            thread {
                _data.postValue(FeedModel(loading = true))
                try {
                    val data = repository.getAll()
                    FeedModel(posts = data, empty = data.isEmpty())
                } catch (e: Exception) {
                    FeedModel(error = true)
                }.also { _data.postValue(it) }
            }
        }

        fun save() {
            edited.value?.let {
                thread {
                    repository.save(it)
                    _postCreated.postValue(Unit)
                }
            }
            edited.postValue(empty)
        }

        fun changeContent(content: String) {
            val text = content.trim()
            if (edited.value?.content == text) {
                return
            }
            edited.value = edited.value?.copy(content = text)
        }

        fun edit(post: Post) {
            edited.value = post
        }

        fun clickPost(post: Post) {

        }
    }