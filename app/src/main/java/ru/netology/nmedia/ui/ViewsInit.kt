package ru.netology.nmedia.ui

import android.content.Context
import android.widget.PopupMenu
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.dto.Post
import kotlin.math.pow

fun PostBinding.listen(post: Post, listener: PostInteractionListener) {

    val popupMenu by lazy {
        PopupMenu(root.context, options).apply {
            inflate(R.menu.options_post)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.remove -> {
                        listener.onButtonRemoveClicked(post)
                        true
                    }
                    R.id.edit -> {
                        listener.onButtonEditClicked(post)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    buttonLikes.setOnClickListener { listener.onButtonLikesClicked(post) }
    buttonReposts.setOnClickListener { listener.onButtonRepostsClicked(post) }
    buttonPlayVideo.setOnClickListener { listener.onButtonPlayVideoClicked(post) }
    videoContent.setOnClickListener { listener.onButtonPlayVideoClicked(post) }
    constraintLayout.setOnClickListener { listener.onContentClicked(post) }
    options.setOnClickListener { popupMenu.show() }

}

fun PostBinding.bind(post: Post) {
    buttonLikes.text = getFormattedNumber(post.likes, root.context)
    buttonReposts.text = getFormattedNumber(post.reposts, root.context)
    iconViews.text = getFormattedNumber(post.views, root.context)
    mainContent.text = post.content
    authorName.text = post.author
    postDate.text = post.published
    buttonLikes.isChecked = post.likedByMe
    groupVideo.visibility =
        if (post.videoURL.isBlank()) android.view.View.GONE else android.view.View.VISIBLE
}

fun getFormattedNumber(number: Int, context: Context): String {

    fun getTruncatedNumber(
        divideDigits: Int,
        truncateDigits: Int
    ): Double {
        return kotlin.math.floor(number.toDouble() / 10F.pow(divideDigits)) / 10F.pow(
            truncateDigits
        )
    }

    return when (number) {
        0 -> ""
        in 1..999 -> String.format(
            context.getString(R.string.numberOnes),
            number.toFloat()
        )
        in 1_000..1_099 -> String.format(
            context.getString(R.string.numberThousands),
            getTruncatedNumber(2, 1)
        )
        in 1_100..9_999 -> String.format(
            context.getString(R.string.numberThousandsAndHundreds),
            getTruncatedNumber(2, 1)
        )
        in 10_000..999_999 -> String.format(
            context.getString(R.string.numberThousands),
            getTruncatedNumber(2, 1)
        )
        in 1_000_000..1_099_000 -> String.format(
            context.getString(R.string.numberMillions),
            getTruncatedNumber(6, 1)
        )
        in 1_100_000..9_999_999 -> String.format(
            context.getString(R.string.numberMillionsAndThousands),
            getTruncatedNumber(6, 1)
        )
        else -> String.format(
            context.getString(R.string.numberMillions),
            getTruncatedNumber(6, 1)
        )
    }
}