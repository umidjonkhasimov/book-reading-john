package uz.gita.bookreading_john.presentation.screens.about

import uz.gita.bookreading_john.navigation.AppNavigator
import javax.inject.Inject

class AboutBookDirectionImpl @Inject constructor(
    private val appNavigator: AppNavigator
): AboutBookDirection {

    override suspend fun popBackStack() {
        appNavigator.popBack()
    }
}