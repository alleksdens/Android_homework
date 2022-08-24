package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel(
    application: Application
) : AndroidViewModel(application),
    PostInteractionListener {

    private val repository: PostRepository = InMemoryPostRepository

    val data by repository::data

    val sharePostContent = SingleLiveEvent<String>()
    val navigateToEditContentScreenEvent = SingleLiveEvent<String>()
    val navigateToViewContentScreenEvent = SingleLiveEvent<Long>()
    val playVideoURL = SingleLiveEvent<String>()

    val currentPost = MutableLiveData<Post?>(null)

    fun setCurrentPost(postId: Long) {
        currentPost.value = repository.getById(postId)
    }

    fun onAddClicked() {
        navigateToEditContentScreenEvent.call()
    }

    fun onButtonSaveClicked(content: String) {
        if (content.isBlank()) return

        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Me",
            content = content,
            published = "today",

            )
        repository.save(post)
        currentPost.value = null
    }

    override fun onButtonLikesClicked(post: Post) {
        repository.like(post.id)
        setCurrentPost(post.id)
    }

    override fun onButtonRepostsClicked(post: Post) {
        sharePostContent.value = post.content
    }

    override fun onButtonPlayVideoClicked(post: Post) {
        playVideoURL.value = post.videoURL
    }

    override fun onContentClicked(post: Post) {
        currentPost.value = post
        navigateToViewContentScreenEvent.value = post.id
    }

    override fun onButtonRemoveClicked(post: Post) =
        repository.remove(post.id)

    override fun onButtonEditClicked(post: Post) {
        currentPost.value = post
        navigateToEditContentScreenEvent.value = post.content
    }
}