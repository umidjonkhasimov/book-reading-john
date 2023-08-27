package uz.gita.bookreading_john.navigation

import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow

typealias NavigationArg = NavController.() -> Unit

interface NavigationHandler {
    val navigationBuffer: Flow<NavigationArg>
}