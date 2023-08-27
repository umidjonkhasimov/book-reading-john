package uz.gita.bookreading_john.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.bookreading_john.data.source.local.sharedpref.MySharedPref
import uz.gita.bookreading_john.data.source.local.sharedpref.impl.MySharedPrefImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SharedPrefModule {

    @Binds
    @Singleton
    fun bindSharedPref(impl: MySharedPrefImpl): MySharedPref
}