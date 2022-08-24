package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostViewFragmentBinding
import ru.netology.nmedia.viewModel.SinglePostViewModel

class PostViewFragment : Fragment() {

    private val args by navArgs<PostViewFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostViewFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val viewModel: SinglePostViewModel by viewModels(::requireParentFragment)

        viewModel.data.observe(viewLifecycleOwner) {
            with(viewModel.getPostById(args.postId)) {
                this?.let {
                    binding.postContent.bind(it)
                    binding.postContent.listen(it, viewModel)
                }
            }
        }

        viewModel.playVideoURL.observe(viewLifecycleOwner) { videoURL ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoURL))
            startActivity(intent)
        }

        viewModel.navigateToEditContentScreenEvent.observe(viewLifecycleOwner) { initialContent ->
            val direction = PostViewFragmentDirections.fromPostViewToPostEdit(initialContent)
            findNavController().navigate(direction)
        }

        viewModel.sharePostContent.observe(viewLifecycleOwner) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(
                intent, getString(R.string.chooser_share_post)
            )
            startActivity(shareIntent)
        }

        viewModel.removePost.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        setFragmentResultListener(
            requestKey = PostEditFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != PostEditFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostContent = bundle.getString(
                PostEditFragment.RESULT_KEY
            ) ?: return@setFragmentResultListener
            viewModel.onButtonSaveClicked(newPostContent)
        }
    }.root
}