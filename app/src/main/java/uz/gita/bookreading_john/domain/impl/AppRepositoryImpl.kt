package uz.gita.bookreading_john.domain.impl

import android.content.Context
import android.os.Environment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import uz.gita.bookreading_john.data.model.BookData
import uz.gita.bookreading_john.data.model.CategoryData
import uz.gita.bookreading_john.data.model.CategoryItemData
import uz.gita.bookreading_john.data.source.local.sharedpref.MySharedPref
import uz.gita.bookreading_john.domain.AppRepository
import uz.gita.bookreading_john.utils.logging
import java.io.File
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val myPrefs: MySharedPref,
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : AppRepository {

    override fun getAllBooks(): Flow<Result<List<CategoryData>>> = callbackFlow {
        val fantasy = ArrayList<BookData>()
        val psychology = ArrayList<BookData>()
        val thriller = ArrayList<BookData>()
        val literature = ArrayList<BookData>()

        getAllLiteratureBooks()
            .onSuccess { literature.addAll(it) }
            .onFailure { trySend(Result.failure(it)) }

        getAllFantasyBooks()
            .onSuccess { fantasy.addAll(it) }
            .onFailure { trySend(Result.failure(it)) }

        getAllPsychologyBooks()
            .onSuccess { psychology.addAll(it) }
            .onFailure { trySend(Result.failure(it)) }

        getAllThrillerBooks()
            .onSuccess { thriller.addAll(it) }
            .onFailure { trySend(Result.failure(it)) }

        val categoryList = ArrayList<CategoryData>()
        categoryList.add(CategoryData(id = "", title = "Fantasy", books = fantasy))
        categoryList.add(CategoryData(id = "", title = "Psychology", books = psychology))
        categoryList.add(CategoryData(id = "", title = "Thriller", books = thriller))
        categoryList.add(CategoryData(id = "", title = "Literature", books = literature))

        trySend(Result.success(categoryList))

        awaitClose()
    }

    override fun downloadBookByUrl(context: Context, book: BookData): Flow<Result<BookData>> = callbackFlow {
        if (File(context.filesDir, book.title).exists()) {
            trySend(Result.success(book))
        } else {
            val path = "books/${book.reference}"
            storage.reference.child(path)
                .getFile(File(context.filesDir, book.title))

                .addOnSuccessListener { trySend(Result.success(book)) }
                .addOnFailureListener { trySend(Result.failure(it)) }

                .addOnProgressListener {
                    val progress = it.bytesTransferred * 100 / it.totalByteCount
                    logging("progress = $progress")
                }
        }

        awaitClose()
    }

    override fun getSavedBooks(context: Context): Flow<Result<List<BookData>>> = callbackFlow {
        fireStore
            .collection("books")
            .get()
            .addOnSuccessListener { query ->
                val list = arrayListOf<BookData>()
                query.forEach { document ->
                    val book = File(context.filesDir, document.get("title").toString())
                    if (book.exists()) {
                        val temp = BookData(
                            id = document.get("id").toString(),
                            title = document.get("title") as String,
                            author = document.get("author") as String,
                            genre = document.get("genre") as String,
                            page = document.get("page").toString(),
                            rate = document.get("rate").toString(),
                            coverUrl = document.get("cover_url").toString(),
                            bookUrl = document.get("book_url").toString(),
                            year = document.get("year") as String,
                            reference = document.get("reference") as String
                        )
                        list.add(temp)
                    }
                }
                trySend(Result.success(list))
            }
            .addOnFailureListener { trySend(Result.failure(it)) }
        awaitClose()
    }

    override fun getCategories(): Flow<Result<List<CategoryItemData>>> = callbackFlow {

        awaitClose()
    }

    override fun getRecommendedBooks(): Flow<Result<List<BookData>>> = callbackFlow {
        fireStore
            .collection("books")
            .whereGreaterThanOrEqualTo("rate", 4)
            .orderBy("rate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val bookList = ArrayList<BookData>()
                snapshot.forEach {
                    bookList.add(
                        BookData(
                            author = it.get("author").toString(),
                            bookUrl = it.get("book_url").toString(),
                            coverUrl = it.get("cover_url").toString(),
                            genre = it.get("genre").toString(),
                            id = it.id,
                            page = it.get("page").toString(),
                            rate = it.get("rate").toString(),
                            reference = it.get("reference").toString(),
                            title = it.get("title").toString(),
                            year = it.get("year").toString()
                        )
                    )
                }
                trySend(Result.success(bookList))
            }
            .addOnFailureListener {
                trySend(Result.failure(it))
            }
        awaitClose()
    }

    override fun searchForBooks(query: String): Flow<Result<List<BookData>>> = callbackFlow {
        val uppercaseQuery = query.replaceFirstChar { it.uppercase() }
        fireStore.collection("books")
            .whereGreaterThanOrEqualTo("title", uppercaseQuery)
            .whereLessThanOrEqualTo("title", uppercaseQuery + "\uf8ff")
            .get()
            .addOnSuccessListener { query ->
                val list = ArrayList<BookData>()
                query.forEach { document ->
                    list.add(
                        BookData(
                            id = document.get("id").toString(),
                            title = document.get("title") as String,
                            author = document.get("author") as String,
                            genre = document.get("genre") as String,
                            page = document.get("page").toString(),
                            rate = document.get("rate").toString(),
                            coverUrl = document.get("cover_url").toString(),
                            bookUrl = document.get("book_url").toString(),
                            year = document.get("year") as String,
                            reference = document.get("reference") as String
                        )
                    )
                }
                trySend(Result.success(list))
            }
            .addOnFailureListener {
                trySend(Result.failure(it))
            }
        awaitClose()
    }

    override fun getBooksByCategory(category: String): Flow<Result<List<BookData>>> = callbackFlow {
        fireStore
            .collection("books")
            .whereEqualTo("genre", category.lowercase())
            .get()
            .addOnSuccessListener { query ->
                val list = ArrayList<BookData>()
                query.forEach { document ->
                    list.add(
                        BookData(
                            id = document.get("id").toString(),
                            title = document.get("title") as String,
                            author = document.get("author") as String,
                            genre = document.get("genre") as String,
                            page = document.get("page").toString(),
                            rate = document.get("rate").toString(),
                            coverUrl = document.get("cover_url").toString(),
                            bookUrl = document.get("book_url").toString(),
                            year = document.get("year") as String,
                            reference = document.get("reference") as String
                        )
                    )
                }

                trySend(Result.success(list))
            }
            .addOnFailureListener {
                trySend(Result.failure(it))
            }
        awaitClose()
    }

    private suspend fun getAllLiteratureBooks(): Result<List<BookData>> {
        val deferred = CompletableDeferred<Result<List<BookData>>>()
        fireStore
            .collection("books")
            .whereEqualTo("genre", "literature")
            .get()
            .addOnSuccessListener { snapshot ->
                val list = ArrayList<BookData>()
                snapshot.forEach { document ->
                    list.add(
                        BookData(
                            id = document.get("id").toString(),
                            title = document.get("title") as String,
                            author = document.get("author") as String,
                            genre = document.get("genre") as String,
                            page = document.get("page").toString(),
                            rate = document.get("rate").toString(),
                            coverUrl = document.get("cover_url").toString(),
                            bookUrl = document.get("book_url").toString(),
                            year = document.get("year") as String,
                            reference = document.get("reference") as String
                        )
                    )
                }
                deferred.complete(Result.success(list))
            }.addOnFailureListener {
                deferred.complete(Result.failure(it))
            }
        return deferred.await()
    }

    private suspend fun getAllFantasyBooks(): Result<List<BookData>> {
        val deferred = CompletableDeferred<Result<List<BookData>>>()
        fireStore
            .collection("books")
            .whereEqualTo("genre", "fantasy")
            .get()
            .addOnSuccessListener { snapshot ->
                val list = ArrayList<BookData>()
                snapshot.forEach { document ->
                    list.add(
                        BookData(
                            id = document.get("id").toString(),
                            title = document.get("title") as String,
                            author = document.get("author") as String,
                            genre = document.get("genre") as String,
                            page = document.get("page").toString(),
                            rate = document.get("rate").toString(),
                            coverUrl = document.get("cover_url").toString(),
                            bookUrl = document.get("book_url").toString(),
                            year = document.get("year") as String,
                            reference = document.get("reference") as String
                        )
                    )
                }
                deferred.complete(Result.success(list))
            }.addOnFailureListener {
                deferred.complete(Result.failure(it))
            }
        return deferred.await()
    }

    private suspend fun getAllPsychologyBooks(): Result<List<BookData>> {
        val deferred = CompletableDeferred<Result<List<BookData>>>()
        fireStore
            .collection("books")
            .whereEqualTo("genre", "psychology")
            .get()
            .addOnSuccessListener { snapshot ->
                val list = ArrayList<BookData>()
                snapshot.forEach { document ->
                    list.add(
                        BookData(
                            id = document.get("id").toString(),
                            title = document.get("title") as String,
                            author = document.get("author") as String,
                            genre = document.get("genre") as String,
                            page = document.get("page").toString(),
                            rate = document.get("rate").toString(),
                            coverUrl = document.get("cover_url").toString(),
                            bookUrl = document.get("book_url").toString(),
                            year = document.get("year") as String,
                            reference = document.get("reference") as String
                        )
                    )
                }
                deferred.complete(Result.success(list))
            }.addOnFailureListener {
                deferred.complete(Result.failure(it))
            }
        return deferred.await()
    }

    private suspend fun getAllThrillerBooks(): Result<List<BookData>> {
        val deferred = CompletableDeferred<Result<List<BookData>>>()
        fireStore
            .collection("books")
            .whereEqualTo("genre", "thriller")
            .get()
            .addOnSuccessListener { snapshot ->
                val list = ArrayList<BookData>()
                snapshot.forEach { document ->
                    list.add(
                        BookData(
                            id = document.get("id").toString(),
                            title = document.get("title") as String,
                            author = document.get("author") as String,
                            genre = document.get("genre") as String,
                            page = document.get("page").toString(),
                            rate = document.get("rate").toString(),
                            coverUrl = document.get("cover_url").toString(),
                            bookUrl = document.get("book_url").toString(),
                            year = document.get("year") as String,
                            reference = document.get("reference") as String
                        )
                    )
                }
                deferred.complete(Result.success(list))
            }.addOnFailureListener {
                deferred.complete(Result.failure(it))
            }
        return deferred.await()
    }
}