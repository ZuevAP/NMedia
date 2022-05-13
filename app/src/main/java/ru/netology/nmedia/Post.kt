package ru.netology.nmedia

import java.text.DecimalFormat

data class Post (
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Int = 0,
    var likedByMe: Boolean = false,
    var countShare: Int = 0,
    var countLikes: Int = 0


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