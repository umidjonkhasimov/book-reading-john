package uz.gita.bookreading_john.presentation.screens.savedbooks

interface SavedBookDirection {
    suspend fun navigateToReadBookScreen(bookName: String, savedPage: Int, totalPage: Int)
}