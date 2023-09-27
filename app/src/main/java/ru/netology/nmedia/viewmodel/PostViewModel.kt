package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.GeneralCallback
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import kotlin.Exception

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    sharedByMe = false,
    published = 0,
    likes = 0,
    viewers = 0,
    videoUrl = null,
    authorAvatar = null,
    shareds = 0,
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel> = _data
    val edited = MutableLiveData(empty)
    private val _smallErrorHappened = SingleLiveEvent<Unit>()
    val smallErrorHappened: LiveData<Unit> = _smallErrorHappened


    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit> = _postCreated
    fun loadPosts() {
        _data.postValue(FeedModel(loading = true))
        val data = repository.getAll(object : GeneralCallback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun likeById(post: Post) {
        if (!post.likedByMe) {
            repository.likeById(post.id, object : GeneralCallback<Post> {
                override fun onSuccess(data: Post) {
                    _data.postValue(FeedModel(posts = _data.value?.posts.orEmpty().map {
                        if (it.id == post.id) data else it
                    }))
                }

                override fun onError(e: Exception) {
                    _smallErrorHappened.postValue(Unit)
                }
            })
        } else {
            repository.unlikeById(post.id, object : GeneralCallback<Post> {
                override fun onSuccess(data: Post) {
                    _data.postValue(FeedModel(posts = _data.value?.posts.orEmpty().map {
                        if (it.id == post.id) data else it
                    }))
                }

                override fun onError(e: Exception) {
                    _smallErrorHappened.postValue(Unit)
                }

            })

        }


    }

    fun sharedById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        repository.removeById(id, object : GeneralCallback<Unit> {
            override fun onSuccess(data: Unit) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .filter { it.id != id }
                    )
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = old))
                _smallErrorHappened.postValue(Unit)
            }
        })
    }

    init {
        loadPosts()
    }


    fun save() {
        edited.value?.let {
            repository.save(it, object : GeneralCallback<Post> {
                override fun onSuccess(data: Post) {
                    _postCreated.postValue(Unit)
                }

                override fun onError(e: Exception) {
                    _smallErrorHappened.postValue(Unit)
                }

            })

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