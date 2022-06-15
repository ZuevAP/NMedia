package ru.netology.nmedia.data.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.databinding.PostBinding


internal class PostsAdapter(
    private val onLikeClicked: (Post) -> Unit,
    private val onShareClicked: (Post) -> Unit
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallBack
){
   inner class ViewHolder(private val binding: PostBinding): RecyclerView.ViewHolder(binding.root){


        fun bind(post: Post) = with(binding){
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.setImageResource(
                if (post.likedByMe) ru.netology.nmedia.R.drawable.ic_baseline_favorite_24 else ru.netology.nmedia.R.drawable.ic_baseline_favorite_border_24
            )
            like.setOnClickListener {
                onLikeClicked(post)
            }

            share.setOnClickListener {
                onShareClicked(post)
            }

            countLike.text = post.countLikes.toString()
            countShare.text = post.countShare.toString()

        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    private object  DiffCallBack : DiffUtil.ItemCallback<Post>(){
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem
    }
}