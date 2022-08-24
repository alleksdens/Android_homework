package ru.netology.nmedia.dto

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long,
    val author: String = "",
    val content: String = "",
    val published: String = "",
    var likes: Int = 0,
    val likedByMe: Boolean = false,
    var reposts: Int = 0,
    var views: Int = 0,
    val videoURL: String = "",
)