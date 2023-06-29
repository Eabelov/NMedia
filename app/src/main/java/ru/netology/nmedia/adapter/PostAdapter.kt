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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onView(post: Post) {}
    fun onRemove(post: Post) {}
    fun onEdit(post: Post) {}
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
        val videoThumbnail = holder.itemView.findViewById<ImageView>(R.id.videoThumbnail)
        //val playButton = holder.itemView.findViewById<ImageButton>(R.id.playButton)

        if (post.videoUrl == null) {
            videoThumbnail.visibility = View.GONE
            //playButton.visibility = View.GONE
        } else {
            videoThumbnail.visibility = View.VISIBLE
            //playButton.visibility = View.VISIBLE

            videoThumbnail.setImageResource(R.drawable.ic_play)

            // Обработчик картинки
            videoThumbnail.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl.toString()))
                holder.itemView.context.startActivity(intent)
            }

            // Обработчик кнопки
            /*playButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl.toString()))
                holder.itemView.context.startActivity(intent)
                finish()
            }*/
        }
    }

}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            avatar.setImageResource(R.drawable.ic_netology_48dp)
            like.text = formatNumber(post.likes)
            share.text = formatNumber(post.shareds)
            views.text = formatNumber(post.viewers)
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