package uz.gita.bookreading_john.presentation.screens.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.gita.bookreading_john.data.model.BookData
import uz.gita.bookreading_john.data.model.CategoryData
import uz.gita.bookreading_john.domain.AppRepository
import uz.gita.bookreading_john.utils.logging
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val repository: AppRepository,
    private val direction: HomeDirection
) : HomeViewModel, ViewModel() {

    override val allBooksData = MutableLiveData<List<CategoryData>>()
    override val bookByCategory = MutableLiveData<List<BookData>>()
    override val searchedBooks = MutableLiveData<List<BookData>>()
    override val errorData = MutableLiveData<String>()
    override val loadingData = MutableLiveData<Boolean>()
    private var searchJob: Job? = null

    init {
        getAllBooks()
    }

    override fun navigateToAboutBookScreen(bookData: BookData) {
        viewModelScope.launch {
            direction.navigateToAboutBookScreen(bookData)
        }
    }

    override fun getAllBooks() {
        loadingData.value = true
        repository.getAllBooks().onEach { result ->
            result.onSuccess {
                loadingData.value = false
                allBooksData.value = it
            }
            result.onFailure {
                loadingData.value = false
                errorData.value = it.message
            }
        }.launchIn(viewModelScope)
    }

    override fun getBooksByCategory(categoryTitle: String) {
        loadingData.value = true
        repository.getBooksByCategory(categoryTitle).onEach {
            it.onSuccess { list ->
                loadingData.value = false
                bookByCategory.value = list
            }

            it.onFailure { error ->
                loadingData.value = false
                errorData.value = error.message
            }
        }.launchIn(viewModelScope)
    }

    override fun searchForBook(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            loadingData.value = true
            repository.searchForBooks(query).onEach {
                it.onSuccess { list ->
                    loadingData.value = false
                    searchedBooks.value = list
                }
                it.onFailure {
                    loadingData.value = false
                    errorData.value = it.message
                }
            }.launchIn(this)
        }
    }
}