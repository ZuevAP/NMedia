package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit

import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository
import kotlin.properties.Delegates


class SharedPrefsPostRepository(application: Application) : PostRepository{


    private val prefs = application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE
    )
    var nextId: Long by Delegates.observable(
        prefs.getLong(NEXT_ID_PREFS_KEY, 0L)){
            _,_, newValue -> prefs.edit { putLong(NEXT_ID_PREFS_KEY, newValue) }
        }


    private var posts

    get()= checkNotNull(data.value){}

    set(value){

        prefs.edit{
            val serializedPost = Json.encodeToString(value)
            putString(POSTS_PREFS_KEY,serializedPost)
        }
        data.value = value
    }


    override val data : MutableLiveData<List<Post>>

    init {
        val serializedPost = prefs.getString(POSTS_PREFS_KEY,null)
        val posts: List<Post> = if (serializedPost != null){
            Json.decodeFromString(serializedPost)
        } else emptyList()
        data = MutableLiveData(posts)

    }


    override fun like(postId: Long) {
        posts = posts.map{if (it.id == postId){
            it.copy(likedByMe = !it.likedByMe,
                countLikes = (if (!it.likedByMe) it.countLikes + 1 else it.countLikes - 1))
        } else it
        }


    }
    override fun share(postId: Long) {
        posts = posts.map{if (it.id == postId){
            it.copy( countShare = it.countShare + 1)
        } else it
        }
    }

    override fun removeById(postId: Long) {
        posts = posts.filter{ it.id != postId }

        return
    }

    override fun save(post: Post) {
        if (post.id == 0L) {

            posts = listOf(
                post.copy(
                    id = nextId,
                    author = "Me",
                    content = post.content,
                    likedByMe = false,
                    published = "now  $nextId"
                )
            ) + posts
            nextId++

            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }

    }
private companion object{

    const val  GENERATED_POST_AMOUNT = 1000
    const val  POSTS_PREFS_KEY = "post"
    const val  NEXT_ID_PREFS_KEY = "nextId"
}

}