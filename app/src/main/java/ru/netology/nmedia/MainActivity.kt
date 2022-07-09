package ru.netology.nmedia
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.data.ViewModel.PostViewModel
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.AndroidUtils

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val viewModel: PostViewModel by viewModels()

        val adapter = PostsAdapter(object : OnInteractionListener {

            override fun onLike(post: Post) {
                viewModel.onLike(post)
            }

            override fun onShare(post: Post) {
                viewModel.onShare(post)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onRemove(post: Post) {
                viewModel.onRemove(post)
            }
        })
        binding.postsRecyclerView.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.groupClear.visibility = View.GONE

        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }

            with(binding.contentEditText,) {
                binding.groupClear.visibility = View.VISIBLE


                requestFocus()
                setText(post.content)
            }

        }

        binding.imageButtonClear.setOnClickListener {
            with(binding.contentEditText) {
                clearFocus()
                setText("")
                AndroidUtils.hideKeyboard(this)
                binding.groupClear.visibility = View.GONE
            }
        }

        binding.saveButton.setOnClickListener {
            with(binding.contentEditText) {


                viewModel.changeContent(text.toString())
                viewModel.onSaveButtonClicked()

                clearFocus()
                setText("")
                AndroidUtils.hideKeyboard(this)
                binding.groupClear.visibility = View.GONE
            }
        }




            }
        }

















