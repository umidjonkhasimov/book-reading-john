package uz.gita.bookreading_john.presentation.screens.explore

import uz.gita.bookreading_john.data.model.BookData


interface RecommendedDirection {
    suspend fun navigateToAboutScreen(bookData: BookData)
}