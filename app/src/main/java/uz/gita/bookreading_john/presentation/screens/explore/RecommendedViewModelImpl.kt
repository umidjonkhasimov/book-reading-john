package uz.gita.bookreading_john.presentation.screens.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.gita.bookreading_john.data.model.BookData
import uz.gita.bookreading_john.data.model.CategoryItemData
import uz.gita.bookreading_john.domain.AppRepository
import uz.gita.bookreading_john.utils.logging
import javax.inject.Inject

@HiltViewModel
class RecommendedViewModelImpl @Inject constructor(
    private val repository: AppRepository,
    private val direction: RecommendedDirection
) : RecommendedViewModel, ViewModel() {

    override val recommendedBooks = MutableLiveData<List<BookData>>()
    override val errorData = MutableLiveData<String>()
    override val loadingData = MutableLiveData<Boolean>()

    init {
        getRecommendedBooks()
    }

    override fun getRecommendedBooks() {
        loadingData.value = true
        repository.getRecommendedBooks().onEach {
            it.onSuccess { list ->
                logging(list.toString())
                loadingData.value = false
                recommendedBooks.value = list
            }
            it.onFailure {
                loadingData.value = false
                errorData.value = it.message
            }
        }.launchIn(viewModelScope)
    }

    override fun navigateToAboutScreen(bookData: BookData) {
        viewModelScope.launch {
            direction.navigateToAboutScreen(bookData)
        }
    }
}