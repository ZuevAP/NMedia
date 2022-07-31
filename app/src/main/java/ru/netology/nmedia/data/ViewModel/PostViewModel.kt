package ru.netology.nmedia.data.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.FilePrefsPostRepository


val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
     video=""
)

class PostViewModel(
    application: Application
): AndroidViewModel(application), OnInteractionListener {

    private val repository: PostRepository = FilePrefsPostRepository(application)

    val data by repository::data

     var edited = MutableLiveData(empty)

    var postToFragment = MutableLiveData(empty)




    override fun onLike(post: Post) = repository.like(post.id)

    override fun onShare(post: Post) = repository.share(post.id)

    override fun onRemove(post: Post) = repository.removeById(post.id)




    fun onSaveButtonClicked(){

        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post

    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

}