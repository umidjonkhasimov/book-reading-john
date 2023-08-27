package uz.gita.bookreading_john.navigation

import androidx.navigation.NavDirections

typealias AppScreen = NavDirections

interface AppNavigator {
    suspend fun navigateTo(screen: AppScreen)
    suspend fun popBack()
}