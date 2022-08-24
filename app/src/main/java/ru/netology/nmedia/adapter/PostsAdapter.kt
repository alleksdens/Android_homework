package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.dto.Post
import kotlin.math.pow

internal class PostsAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)

        return ViewHolder(
            binding,
            interactionListener,
        )
    }

    class ViewHolder(
        private val binding: PostBinding,
        listener: PostInteractionListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.options).apply {
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

        init {
            binding.buttonLikes.setOnClickListener { listener.onButtonLikesClicked(post) }
            binding.buttonReposts.setOnClickListener { listener.onButtonRepostsClicked(post) }
        }

        fun bind(post: Post) {
            this.post = post

            with(binding) {
                buttonLikes.text = getFormattedNumber(post.likes)
                buttonReposts.text = getFormattedNumber(post.reposts)
                iconViews.text = getFormattedNumber(post.views)
                mainContent.text = post.content
                authorName.text = post.author
                postDate.text = post.published
                buttonLikes.isChecked = post.likedByMe
                options.setOnClickListener { popupMenu.show() }
            }
        }

        @DrawableRes
        private fun getButtonLikesIconResId(liked: Boolean) =
            if (liked) R.drawable.ic_likes_red_24dp else R.drawable.ic_likes_24dp

        private fun getFormattedNumber(number: Int): String {

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
                    itemView.context.getString(R.string.numberOnes),
                    number.toFloat()
                )
                in 1_000..1_099 -> String.format(
                    itemView.context.getString(R.string.numberThousands),
                    getTruncatedNumber(2, 1)
                )
                in 1_100..9_999 -> String.format(
                    itemView.context.getString(R.string.numberThousandsAndHundreds),
                    getTruncatedNumber(2, 1)
                )
                in 10_000..999_999 -> String.format(
                    itemView.context.getString(R.string.numberThousands),
                    getTruncatedNumber(2, 1)
                )
                in 1_000_000..1_099_000 -> String.format(
                    itemView.context.getString(R.string.numberMillions),
                    getTruncatedNumber(6, 1)
                )
                in 1_100_000..9_999_999 -> String.format(
                    itemView.context.getString(R.string.numberMillionsAndThousands),
                    getTruncatedNumber(6, 1)
                )
                else -> String.format(
                    itemView.context.getString(R.string.numberMillions),
                    getTruncatedNumber(6, 1)
                )
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {

        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem
    }
}