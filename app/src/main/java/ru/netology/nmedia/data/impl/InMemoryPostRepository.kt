package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository

class InMemoryPostRepository : PostRepository{

    private val posts get()= checkNotNull(data.value)

    override val data = MutableLiveData(
        List(10) { index -> Post(
        id = index + 1L,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Контент номер : $index ",
        published = "14 июня в 13:00",
        likedByMe = false,
        countShare = 999 + index * 100,
        countLikes = 1_099 + index * 100
        )}
    )


    override fun like(postId: Long) {
        data.value = posts.map{if (it.id == postId){
                it.copy(likedByMe = !it.likedByMe,
                countLikes = (if (!it.likedByMe) it.countLikes + 1 else it.countLikes - 1))
        } else it
        }


    }
    override fun share(postId: Long) {
        data.value = posts.map{if (it.id == postId){
            it.copy( countShare = it.countShare + 1)
        } else it
        }
    }

}