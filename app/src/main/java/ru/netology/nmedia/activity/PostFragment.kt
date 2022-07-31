package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.PostContentFragment.Companion.textArg
import ru.netology.nmedia.data.ViewModel.PostViewModel
import ru.netology.nmedia.databinding.FragmentPostBinding




class PostFragment() : Fragment(){

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,

        ): View {

        val binding = FragmentPostBinding.inflate(inflater, container, false)

        val post = viewModel.postToFragment.value


        viewModel.data.map { posts ->
            posts.find { it.id == post!!.id }
        }.observe(viewLifecycleOwner) { post ->
            post ?: run {
                findNavController().navigateUp()
                return@observe
            }

            binding.postLayout.apply {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                like.isChecked = post.likedByMe
                like.text = post.displayNumbers(post.countLikes)
                share.text = post.displayNumbers(post.countShare)
                like.setIconResource(
                    if (post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                )
                video.text = post.video

                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    viewModel.onRemove(post)
                                    true
                                }
                                R.id.edit -> {

                                    findNavController().navigate(
                                        R.id.action_postFragment_to_postContentFragment,
                                        Bundle().apply
                                        {
                                            textArg = post.content
                                            viewModel.edit(post)
                                        })

                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()
                }


                video.setOnClickListener{
                    val intent = Intent().apply {
                        Intent.ACTION_VIEW
                        data = Uri.parse(post.video)
                    }
                    val playVideoIntent = Intent.createChooser(intent,(R.string.chooser_share_video.toString()))
                    startActivity(playVideoIntent)

                }

                like.setOnClickListener {
                   viewModel.onLike(post)
                }
                share.setOnClickListener {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }

                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)


                }

            }
        }

        return binding.root
    }


}