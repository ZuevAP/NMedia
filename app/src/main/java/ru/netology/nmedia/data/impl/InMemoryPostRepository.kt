package ru.netology.nmedia.data.impl


import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository

class InMemoryPostRepository : PostRepository{
    override val data = MutableLiveData<Post>(Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netology.ru",
        published = "09 мая в 13:00",
        likedByMe = false,
        countShare = 999,
        countLikes = 1_099
        ))

    override fun like() {
        val currentPost = checkNotNull(data.value)

        val likePost = currentPost.copy(likedByMe = !currentPost.likedByMe,
            countLikes = (if (!currentPost.likedByMe) currentPost.countLikes + 1 else currentPost.countLikes - 1))

        data.value = likePost

    }
    override fun share(){
        val share = checkNotNull(data.value)

        val postShare = share.copy( countShare = share.countShare + 1)

        data.value = postShare

    }

}