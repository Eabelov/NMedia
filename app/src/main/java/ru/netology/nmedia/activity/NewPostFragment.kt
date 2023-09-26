package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.utils.HideKeyboard
import ru.netology.nmedia.utils.StringProperty
import ru.netology.nmedia.viewmodel.PostViewModel
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import ru.netology.nmedia.R

class NewPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringProperty
    }

    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )
        val animationSlideDown =
            AnimationUtils.loadAnimation(this.requireContext(), R.anim.slide_down)
        val animationSlideUp = AnimationUtils.loadAnimation(this.requireContext(), R.anim.slide_up)
        arguments?.textArg
            ?.let(binding.editNewPost::setText)

        binding.ok.setOnClickListener {
            viewModel.changeContent(binding.editNewPost.text.toString())
            viewModel.save()
            HideKeyboard.hideKeyboard(requireView())
        }

        binding.banner.positiveButton.setOnClickListener {
            viewModel.loadPosts()
            findNavController().navigateUp()
            binding.banner.root.startAnimation(animationSlideUp)
            binding.banner.root.isVisible = false
        }
        binding.banner.negativeButton.setOnClickListener {
            ActivityCompat.finishAffinity(this.requireActivity())
        }

        viewModel.smallErrorHappened.observe(viewLifecycleOwner) {
            binding.banner.root.isVisible = true
            binding.banner.root.startAnimation(animationSlideDown)
        }

        viewModel.postCreated.observe(viewLifecycleOwner) {
            viewModel.loadPosts()
            findNavController().navigateUp()
        }
        return binding.root
    }
}