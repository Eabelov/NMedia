package ru.netology.nmedia.adapter


import android.content.Intent
import android.net.Uri
import ru.netology.nmedia.utils.FormatNumber.formatNumber
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.extensions.load
import ru.netology.nmedia.extensions.loadCircle

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onView(post: Post) {}
    fun onRemove(post: Post) {}
    fun onEdit(post: Post) {}
    fun openVideoUrl(post: Post) {}
    fun clickPost(post: Post) {}
}

class PostsAdapter(private val onInteractionListener: OnInteractionListener) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    private val baseUrl = "http://10.0.2.2:9999"
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published.toString()
            content.text = post.content
            avatar.setImageResource(R.drawable.ic_netology_48dp)
            like.text = formatNumber(post.likes)
            share.text = formatNumber(post.shareds)
            views.text = formatNumber(post.viewers)
            avatar.loadCircle("$baseUrl/avatars/${post.authorAvatar}")
            attachment.load("$baseUrl/images/${post.attachment?.url}")
            attachment.contentDescription = post.attachment?.description
            attachment.isVisible = !post.attachment?.url.isNullOrBlank()
            like.isChecked = post.likedByMe
            like.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            views.setOnClickListener {
                onInteractionListener.onView(post)
            }
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }

                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
            if (post.videoUrl == null) {
                videoThumbnail.visibility = View.GONE
            } else {
                videoThumbnail.visibility = View.VISIBLE
                videoThumbnail.setImageResource(R.drawable.ic_play)
            }
            videoThumbnail.setOnClickListener{
                onInteractionListener.openVideoUrl(post)
            }
            root.setOnClickListener{
                onInteractionListener.clickPost(post)
            }
        }
    }

}


class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}