package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository

class InMemoryPostRepository : PostRepository{

    private var nextId = 11L

    private val posts get()= checkNotNull(data.value)

    override val data = MutableLiveData(
        List(10) { index -> Post(
        id = 1L + index,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Контент номер : ${index + 1} ",
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

    override fun removeById(postId: Long) {
        data.value = posts.filter{ it.id != postId }
        data.value = posts
        return
    }

    override fun save(post: Post) {
        if (post.id == 0L) {

            data.value = listOf(
                post.copy(
                    id = nextId,
                    author = "Me",
                    content = post.content,
                    likedByMe = false,
                    published = "now  $nextId"
                )
            ) + posts
            nextId++
            data.value = posts
            return
        }

        data.value = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
    }
}