package uz.gita.bookreading_john.presentation.screens.bookread

import uz.gita.bookreading_john.navigation.AppNavigator
import javax.inject.Inject

class BookReadDirectionImpl @Inject constructor(
    private val appNavigator: AppNavigator
): BookReadDirection {

    override suspend fun popBackStack() {
        appNavigator.popBack()
    }
}