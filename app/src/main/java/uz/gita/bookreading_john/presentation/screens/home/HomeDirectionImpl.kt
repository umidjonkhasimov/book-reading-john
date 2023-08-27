package uz.gita.bookreading_john.presentation.screens.home

import uz.gita.bookreading_john.data.model.BookData
import uz.gita.bookreading_john.navigation.AppNavigator
import javax.inject.Inject

class HomeDirectionImpl @Inject constructor(
    private val appNavigator: AppNavigator
) : HomeDirection {

    override suspend fun navigateToAboutBookScreen(bookData: BookData) {
        appNavigator.navigateTo(
            HomeScreenDirections.actionHomeScreenToAboutBookScreen(bookData)
        )
    }
}