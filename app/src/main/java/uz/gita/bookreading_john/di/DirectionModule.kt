package uz.gita.bookreading_john.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.gita.bookreading_john.presentation.screens.bookread.BookReadDirection
import uz.gita.bookreading_john.presentation.screens.bookread.BookReadDirectionImpl
import uz.gita.bookreading_john.presentation.screens.home.HomeDirection
import uz.gita.bookreading_john.presentation.screens.home.HomeDirectionImpl
import uz.gita.bookreading_john.presentation.screens.explore.RecommendedDirection
import uz.gita.bookreading_john.presentation.screens.explore.RecommendedDirectionImpl
import uz.gita.bookreading_john.presentation.screens.savedbooks.SavedBookDirection
import uz.gita.bookreading_john.presentation.screens.savedbooks.SavedBookDirectionImpl
import uz.gita.bookreading_john.presentation.screens.about.AboutBookDirection
import uz.gita.bookreading_john.presentation.screens.about.AboutBookDirectionImpl

@Module
@InstallIn(ViewModelComponent::class)
interface DirectionModule {

    @Binds
    fun bindHomeScreenDirection(impl: RecommendedDirectionImpl): RecommendedDirection

    @Binds
    fun bindSavedBooksScreenDirection(impl: SavedBookDirectionImpl): SavedBookDirection

    @Binds
    fun bindExploreScreenDirection(impl: HomeDirectionImpl): HomeDirection

    @Binds
    fun bindBookReadScreenDirection(impl: BookReadDirectionImpl): BookReadDirection

    @Binds
    fun bindAboutBookScreenDirection(impl: AboutBookDirectionImpl): AboutBookDirection
}