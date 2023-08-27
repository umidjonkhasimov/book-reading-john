package uz.gita.bookreading_john.data.model

import java.io.Serializable

data class BookData(
    val id: String = "",
    val author: String = "",
    val bookUrl: String = "",
    val coverUrl: String = "",
    val genre: String = "",
    val page: String = "",
    val rate: String = "",
    val reference: String = "",
    val title: String = "",
    val year: String = "",
    val about: String = ""
) : Serializable