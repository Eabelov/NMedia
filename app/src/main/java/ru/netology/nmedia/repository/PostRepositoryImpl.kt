package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostApi
import kotlin.random.Random


class PostRepositoryImpl : PostRepository {
    private var randomResponceCode = Random.nextBoolean()
    override fun getAll(postsCallback: GetAllCallback<List<Post>>) {
        PostApi.service.getAll()
            .enqueue(object : retrofit2.Callback<List<Post>> {
                override fun onResponse(
                    call: retrofit2.Call<List<Post>>,
                    response: retrofit2.Response<List<Post>>
                ) {
                    if (!response.isSuccessful) {
                        postsCallback.onError(RuntimeException(response.message()))
                    }
                    postsCallback.onSuccess(
                        response.body() ?: throw RuntimeException("body is null")
                    )
                }

                override fun onFailure(call: retrofit2.Call<List<Post>>, t: Throwable) {
                    postsCallback.onError(RuntimeException(t))
                }
            })
    }

    override fun save(post: Post, postsCallback: GetAllCallback<Post>) {
        PostApi.service.save(post)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        postsCallback.onError(RuntimeException(response.message()))
                    }
                    if (randomResponceCode) {
                        postsCallback.onSuccess(
                            response.body() ?: throw RuntimeException("body is null")
                        )
                    } else {
                        postsCallback.onError(RuntimeException(response.message()))
                    }
                    randomResponceCode = !randomResponceCode
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    postsCallback.onError(RuntimeException(t))
                }

            })
    }

    override fun likeById(id: Long, postsCallback: GetAllCallback<Post>) {
        PostApi.service.likeById(id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        postsCallback.onError(RuntimeException(response.message()))
                    }
                    if (randomResponceCode) {
                        postsCallback.onSuccess(
                            response.body() ?: throw RuntimeException("body is null")
                        )
                    } else {
                        postsCallback.onError(RuntimeException(response.message()))
                    }
                    randomResponceCode = !randomResponceCode
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    postsCallback.onError(RuntimeException(t))
                }
            })
    }

    override fun unlikeById(id: Long, postsCallback: GetAllCallback<Post>) {

        PostApi.service.unlikeById(id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        postsCallback.onError(RuntimeException(response.message()))
                    }
                    if (randomResponceCode) {
                        postsCallback.onSuccess(
                            response.body() ?: throw RuntimeException("body is null")
                        )
                    } else {
                        postsCallback.onError(RuntimeException(response.message()))
                    }
                    randomResponceCode = !randomResponceCode
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    postsCallback.onError(RuntimeException(t))
                }

            })
    }

    override fun shareById(id: Long) {
        TODO("Not yet implemented")
    }


    override fun removeById(id: Long, postsCallback: GetAllCallback<Unit>) {
        PostApi.service.removeById(id)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (!response.isSuccessful) {
                        postsCallback.onError(RuntimeException(response.message()))
                    }
                    if (randomResponceCode) {
                        postsCallback.onSuccess(Unit)
                    } else {
                        postsCallback.onError(RuntimeException(response.message()))
                    }
                    randomResponceCode = !randomResponceCode
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    postsCallback.onError(RuntimeException(t))
                }

            })

    }
}