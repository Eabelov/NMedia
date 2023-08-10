package ru.netology.nmedia.activity


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostViewHolder
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel


class PostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPostBinding.inflate(layoutInflater, container, false)
        arguments?.textArg
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            binding.onePost.apply {
                posts.map { post ->
                    if (post.id == arguments?.getLong("postId")) {
                        PostViewHolder(this, object : OnInteractionListener {
                            override fun onLike(post: Post) {
                                viewModel.likeById(post.id)
                            }

                            override fun onShare(post: Post) {
                                val intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, post.content)
                                    type = "text/plain"
                                }

                                val shareIntent =
                                    Intent.createChooser(
                                        intent,
                                        getString(R.string.chooser_share_post)
                                    )
                                startActivity(shareIntent)
                            }

                            override fun onRemove(post: Post) {
                                viewModel.removeById(post.id)
                                findNavController().navigateUp()
                            }

                            override fun onEdit(post: Post) {
                                viewModel.edit(post)
                                findNavController().navigate(
                                    R.id.action_postFragment_to_editPostFragment,
                                    Bundle().apply {
                                        textArg = post.content
                                    }
                                )
                            }
                        }).bind(post)
                    }
                }
            }
        }

        return binding.root
    }
}




