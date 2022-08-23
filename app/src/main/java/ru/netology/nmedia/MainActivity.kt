package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) { post ->
            binding.render(post)
        }

        binding.buttonLikes.setOnClickListener {
            viewModel.onbuttonLikesClicked()
        }

        binding.buttonReposts.setOnClickListener {
            viewModel.onbuttonRepostsClicked()
        }

    }

    private fun ActivityMainBinding.render(post: Post) {
        countLikes.text = getFormattedNumber(post.likes)
        countReposts.text = getFormattedNumber(post.reposts)
        countViews.text = getFormattedNumber(post.views)
        buttonLikes.setImageResource(getButtonLikesIconResId(post.likedByMe))
    }

    @DrawableRes
    private fun getButtonLikesIconResId(liked: Boolean) =
        if (liked) R.drawable.ic_likes_red_24dp else R.drawable.ic_likes_24dp

    private fun getFormattedNumber(number: Int): String {
        return when (number) {
            0 -> ""
            in 1..999 -> String.format(getString(R.string.numberOnes), number.toFloat())
            in 1_000..1_099 -> String.format(
                getString(R.string.numberThousands),
                kotlin.math.floor(number.toDouble() / 100) / 10
            )
            in 1_100..9_999 -> String.format(
                getString(R.string.numberThousandsAndHundreds),
                kotlin.math.floor(number.toDouble() / 100) / 10
            )
            in 10_000..999_999 -> String.format(
                getString(R.string.numberThousands),
                kotlin.math.floor(number.toDouble() / 100) / 10
            )
            in 1_000_000..1_099_000 -> String.format(
                getString(R.string.numberMillions),
                kotlin.math.floor(number.toDouble() / 100_000) / 10
            )
            in 1_100_000..9_999_999 -> String.format(
                getString(R.string.numberMillionsAndThousands),
                kotlin.math.floor(number.toDouble() / 100_000) / 10
            )
            else -> String.format(
                getString(R.string.numberMillions),
                kotlin.math.floor(number.toDouble() / 100_000) / 10
            )
        }

    }
}