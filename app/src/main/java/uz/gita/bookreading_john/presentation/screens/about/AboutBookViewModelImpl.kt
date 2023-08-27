package uz.gita.bookreading_john.presentation.screens.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.gita.bookreading_john.presentation.screens.about.AboutBookDirection
import uz.gita.bookreading_john.presentation.screens.about.AboutBookViewModel
import javax.inject.Inject

@HiltViewModel
class AboutBookViewModelImpl @Inject constructor(
    private val direction: AboutBookDirection
) : AboutBookViewModel, ViewModel() {

    override fun popBackStack() {
        viewModelScope.launch {
            direction.popBackStack()
        }
    }
}