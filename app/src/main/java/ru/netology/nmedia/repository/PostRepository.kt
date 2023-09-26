package ru.netology.nmedia.repository
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(postsCallback: GetAllCallback<List<Post>>)
    fun likeById(id: Long, postsCallback: GetAllCallback<Post>)
    fun unlikeById(id: Long, postsCallback: GetAllCallback<Post>)
    fun shareById(id: Long)
    fun save(post: Post, postsCallback: GetAllCallback<Post>)
    fun removeById(id: Long,postsCallback: GetAllCallback<Unit>)
}
interface GetAllCallback<T> {
    fun onSuccess(data:T)
    fun onError(e: Exception)
}