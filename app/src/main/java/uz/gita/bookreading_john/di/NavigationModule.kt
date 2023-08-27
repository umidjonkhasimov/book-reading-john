package uz.gita.bookreading_john.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.bookreading_john.navigation.AppNavigator
import uz.gita.bookreading_john.navigation.NavigationDispatcher
import uz.gita.bookreading_john.navigation.NavigationHandler

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    fun bindsNavigationAppNavigator(navigationDispatcher: NavigationDispatcher): AppNavigator

    @Binds
    fun bindsNavigationHandler(navigationDispatcher: NavigationDispatcher): NavigationHandler
}