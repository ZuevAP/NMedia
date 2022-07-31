package ru.netology.nmedia

import kotlinx.serialization.Serializable
import java.text.DecimalFormat
@Serializable
data class Post (
    var id: Long,
    val author: String,
    var content: String,
    val published: String,
    var likes: Int = 0,
    var likedByMe: Boolean = false,
    var countShare: Int = 0,
    var countLikes: Int = 0,
    val video: String


) {

    fun displayNumbers(number: Int): String {

        val decimalFormat = DecimalFormat("#.#")
        return when (number) {
            in 0..1099 -> "$number"
            in 1100..9_999 -> "${decimalFormat.format(number.toFloat() / 1_000)}K"
            in 10_000..999_999 -> "${number / 1_000}K"
            else -> "${decimalFormat.format(number.toFloat() / 1_000_000)}M"

        }
    }

}