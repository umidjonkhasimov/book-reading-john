package uz.gita.bookreading_john.presentation.screens.about

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.bookreading_john.R
import uz.gita.bookreading_john.databinding.ScreenAboutBookBinding
import uz.gita.bookreading_john.domain.impl.AppRepositoryImpl
import uz.gita.bookreading_john.utils.logging
import uz.gita.bookreading_john.utils.toasting
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class AboutBookScreen : Fragment(R.layout.screen_about_book) {

    private val binding by viewBinding(ScreenAboutBookBinding::bind)
    private val viewModel by viewModels<AboutBookViewModelImpl>()
    private val args by navArgs<AboutBookScreenArgs>()

    @Inject
    lateinit var repository: AppRepositoryImpl

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookData = args.bookData

        val book = File(requireContext().filesDir, bookData.title)

        binding.apply {
            txtName.text = bookData.title
            txtAuthor.text = "Author: ${bookData.author}"
            txtGenre.text = "Genre: ${bookData.genre}"
            txtPage.text = "Pages: ${bookData.page}"
            txtYear.text = "Year: ${bookData.year}"
//            txtAbout.text = bookData.about

            Glide.with(requireContext()).load(bookData.coverUrl).into(imgBook)

            if (book.exists()) {
                btnDownload.setImageResource(R.drawable.ic_saved)
                btnDownload.isClickable = false
            } else {
                btnDownload.setImageResource(R.drawable.ic_download)
                btnDownload.isClickable = true
            }

            btnBack.setOnClickListener {
                viewModel.popBackStack()
            }

            btnDownload.setOnClickListener {
                binding.progressBar.visibility = View.VISIBLE
                repository.downloadBookByUrl(requireContext(), bookData)
                    .onEach { data ->
                        data.onSuccess {
                            binding.progressBar.visibility = View.GONE
                            toasting(requireContext(), "Book saved")
                            btnDownload.setImageResource(R.drawable.ic_saved)
                        }
                        data.onFailure { e ->
                            binding.progressBar.visibility = View.GONE
                            logging("AboutBookScreen error = ${e.message}")
                            toasting(requireContext(), e.message ?: "Error")
                        }
                    }.launchIn(lifecycleScope)
                it.isClickable = false
            }
        }
    }
}