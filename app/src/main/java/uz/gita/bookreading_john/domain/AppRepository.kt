package uz.gita.bookreading_john.domain

import android.content.Context
import kotlinx.coroutines.flow.Flow
import uz.gita.bookreading_john.data.model.BookData
import uz.gita.bookreading_john.data.model.CategoryData
import uz.gita.bookreading_john.data.model.CategoryItemData

interface AppRepository {

    fun getAllBooks(): Flow<Result<List<CategoryData>>>

    fun downloadBookByUrl(context: Context, book: BookData): Flow<Result<BookData>>

    fun getSavedBooks(context: Context): Flow<Result<List<BookData>>>

    fun getCategories(): Flow<Result<List<CategoryItemData>>>

    fun getRecommendedBooks(): Flow<Result<List<BookData>>>

    fun getBooksByCategory(category: String): Flow<Result<List<BookData>>>

    fun searchForBooks(query: String): Flow<Result<List<BookData>>>
}