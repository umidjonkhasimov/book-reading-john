package uz.gita.bookreading_john.presentation.screens.home

import androidx.lifecycle.LiveData
import uz.gita.bookreading_john.data.model.BookData
import uz.gita.bookreading_john.data.model.CategoryData

interface HomeViewModel {

    val allBooksData: LiveData<List<CategoryData>>
    val bookByCategory: LiveData<List<BookData>>
    val searchedBooks: LiveData<List<BookData>>
    val errorData: LiveData<String>
    val loadingData: LiveData<Boolean>

    fun navigateToAboutBookScreen(bookData: BookData)

    fun getAllBooks()
    fun getBooksByCategory(categoryTitle: String)
    fun searchForBook(query: String)
}