package ru.netology.nmedia.adapter

import ru.netology.nmedia.dto.Post

interface PostInteractionListener {
    fun onButtonLikesClicked(post: Post)
    fun onButtonRepostsClicked(post: Post)
    fun onButtonRemoveClicked(post: Post)
    fun onButtonEditClicked(post: Post)
    fun onButtonCancelClicked()
}