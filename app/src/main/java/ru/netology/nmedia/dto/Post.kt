package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String = "",
    val content: String = "",
    val published: String = "",
    var likes: Int = 0,
    val likedByMe: Boolean = false,
    var reposts: Int = 0,
    var views: Int = 0,
)