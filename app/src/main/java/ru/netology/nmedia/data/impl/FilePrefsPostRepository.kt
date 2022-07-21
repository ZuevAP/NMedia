package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.Post
import ru.netology.nmedia.data.PostRepository
import kotlin.properties.Delegates

class FilePrefsPostRepository (private  val application: Application) : PostRepository{

    private  val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

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
            application.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use{
                it.write(gson.toJson(value))
            }


            data.value = value
        }


    override val data : MutableLiveData<List<Post>>

    init {
        val file = application.filesDir.resolve(FILE_NAME)
        val posts: List<Post> = if (file.exists()){
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()

            reader.use {
                gson.fromJson(it,type)
            }
        }else emptyList()

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
        const val  FILE_NAME = "post.json"
    }


}