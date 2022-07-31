package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.PostContentFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.data.ViewModel.PostViewModel
import ru.netology.nmedia.databinding.FragmentFeedBinding


class FeedFragment : Fragment() {

    val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )


        val adapter = PostsAdapter(object : OnInteractionListener {


            override fun onOpenPost(post: Post) {
                viewModel.postToFragment.value = post

                findNavController().navigate(
                    R.id.action_feedFragment_to_postFragment
                )
            }



            override fun onLike(post: Post) {
                viewModel.onLike(post)
            }

            override fun onEdit(post: Post) {

                findNavController().navigate(
                    R.id.action_feedFragment_to_postContentFragment,
                    Bundle().apply
                    {
                        textArg = post.content
                        viewModel.edit(post)
                    })


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
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
                viewModel.onShare(post)
            }

            override fun onVideo(post: Post) {
                val intent = Intent().apply {
                    Intent.ACTION_VIEW
                    data = Uri.parse(post.video)
                }
                val playVideoIntent = Intent.createChooser(intent,(R.string.chooser_share_video.toString()))
                startActivity(playVideoIntent)

            }

        })

        binding.postsRecyclerView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }



        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_postContentFragment)
        }


        return binding.root
    }
}























