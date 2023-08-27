package uz.gita.bookreading_john.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.bookreading_john.domain.AppRepository
import uz.gita.bookreading_john.domain.impl.AppRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindRepository(impl: AppRepositoryImpl): AppRepository
}