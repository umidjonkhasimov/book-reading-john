package uz.gita.bookreading_john.presentation.screens.savedbooks

import androidx.lifecycle.LiveData
import uz.gita.bookreading_john.data.model.BookData

interface SavedViewModel {
    val booksData: LiveData<List<BookData>>
    val errorData: LiveData<String>

    fun navigateToReadBookScreen(bookName: String, savedPage: Int, totalPage: Int)
}