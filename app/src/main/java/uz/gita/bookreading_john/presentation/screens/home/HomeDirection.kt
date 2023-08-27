package uz.gita.bookreading_john.presentation.screens.home

import uz.gita.bookreading_john.data.model.BookData


interface HomeDirection {
    suspend fun navigateToAboutBookScreen(bookData: BookData)
}