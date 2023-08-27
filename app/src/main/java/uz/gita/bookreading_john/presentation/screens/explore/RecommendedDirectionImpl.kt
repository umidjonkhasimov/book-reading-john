package uz.gita.bookreading_john.presentation.screens.explore

import uz.gita.bookreading_john.data.model.BookData
import uz.gita.bookreading_john.navigation.AppNavigator
import javax.inject.Inject

class RecommendedDirectionImpl @Inject constructor(
    private val appNavigator: AppNavigator
) : RecommendedDirection {

    override suspend fun navigateToAboutScreen(bookData: BookData) {
        appNavigator.navigateTo(RecommendedScreenDirections.actionRecommendedScreenToAboutBookScreen(bookData))
    }
}