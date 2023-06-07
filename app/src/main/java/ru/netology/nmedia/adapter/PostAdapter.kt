package ru.netology.nmedia.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import java.text.DecimalFormat
import kotlin.math.pow

typealias OnLikeListener = (post: Post) -> Unit

class PostsAdapter(private val onLikeListener: OnLikeListener, private val onShareListener: OnLikeListener, private val onViewListener: OnLikeListener) :
    RecyclerView.Adapter<PostViewHolder>() {
    var list = emptyList<Post>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeListener, onShareListener, onViewListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = list[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = list.size

}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnLikeListener,
    private val onViewListener: OnLikeListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeCount.text = formatNumber(post.likes)
            shareCount.text = formatNumber(post.shareds)
            viewsCount.text = formatNumber(post.viewers)
            like.setImageResource(
                if (post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
            )
            share.setImageResource(R.drawable.ic_baseline_share_24)
            views.setImageResource(R.drawable.ic_views_24)
            like.setOnClickListener {
                onLikeListener(post)
            }
            share.setOnClickListener {
                onShareListener(post)
            }
            views.setOnClickListener{
                onViewListener(post)
            }
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