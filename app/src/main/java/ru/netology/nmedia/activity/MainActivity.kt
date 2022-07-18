package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.data.ViewModel.PostViewModel
import ru.netology.nmedia.data.ViewModel.empty
import ru.netology.nmedia.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




        val adapter = PostsAdapter(object : OnInteractionListener {

            override fun onLike(post: Post) {
                viewModel.onLike(post)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                editPostLauncher.launch(post.content)

            }

            override fun onRemove(post: Post) {
                viewModel.onRemove(post)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent,getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }
            override fun onVideo(post: Post) {
                val videoIntent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                if (videoIntent.resolveActivity(packageManager) != null) {
                    startActivity(videoIntent)
                }

                }

        })

        binding.postsRecyclerView.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }


        val newPostLauncher = registerForActivityResult(PostResultContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.changeContent(result)
            viewModel.onSaveButtonClicked()
        }

        binding.fab.setOnClickListener {
            newPostLauncher.launch()
        }

    }
    val editPostLauncher = registerForActivityResult(EditPostResultContract()) { result ->
        result ?: return@registerForActivityResult

        viewModel.changeContent(result)
        viewModel.onSaveButtonClicked()
    }



}






















