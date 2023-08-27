package uz.gita.bookreading_john.presentation.screens.explore

import androidx.lifecycle.LiveData
import uz.gita.bookreading_john.data.model.BookData
import uz.gita.bookreading_john.data.model.CategoryItemData

interface RecommendedViewModel {

    val recommendedBooks: LiveData<List<BookData>>
    val errorData: LiveData<String>
    val loadingData: LiveData<Boolean>

    fun getRecommendedBooks()
    fun navigateToAboutScreen(bookData: BookData)
}