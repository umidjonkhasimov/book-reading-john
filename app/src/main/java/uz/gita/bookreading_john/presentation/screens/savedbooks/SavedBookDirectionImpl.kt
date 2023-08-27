package uz.gita.bookreading_john.presentation.screens.savedbooks

import uz.gita.bookreading_john.navigation.AppNavigator
import javax.inject.Inject

class SavedBookDirectionImpl @Inject constructor(
    private val appNavigator: AppNavigator
) : SavedBookDirection {

    override suspend fun navigateToReadBookScreen(
        bookName: String,
        savedPage: Int,
        totalPage: Int
    ) {
        appNavigator.navigateTo(
            SavedBooksScreenDirections.actionSavedBooksScreenToBookReadScreen(
                bookName, savedPage, totalPage
            )
        )
    }
}