package ru.netology.nmedia.dto

data class Post(val id: Long,
                var likes: Int = 0,
                var likedByMe: Boolean = false,
                var reposts: Int = 0,
                var views: Int = 0,)
