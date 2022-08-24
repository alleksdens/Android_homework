package ru.netology.nmedia

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.hideKeyboard
import ru.netology.nmedia.util.showKeyboard
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(viewModel)

        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.buttonCancel.setOnClickListener {
            viewModel.onButtonCancelClicked()
        }

        binding.buttonSave.setOnClickListener {
            with(binding.contentEdit) {
                val content = text.toString()
                viewModel.onButtonSaveClicked(content)
            }
        }

        viewModel.currentPost.observe(this) { currentPost ->
            with(binding) {
                val content = currentPost?.content
                contentEdit.setText(content)
                previousText.hint = content
                if (content != null) {
                    contentEdit.requestFocus()
                    contentEdit.showKeyboard()
                    groupEdit.visibility = View.VISIBLE
                } else {
                    contentEdit.clearFocus()
                    contentEdit.hideKeyboard()
                    groupEdit.visibility = View.GONE

                    //if (currentPost == null) postsRecyclerView.layoutManager?.scrollToPosition(0)
                }
            }

        }

    }
}