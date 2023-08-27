package uz.gita.bookreading_john.presentation.screens.bookread

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.github.barteksc.pdfviewer.util.FitPolicy
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.bookreading_john.R
import uz.gita.bookreading_john.data.source.local.sharedpref.MySharedPref
import uz.gita.bookreading_john.databinding.ScreenBookReadBinding
import uz.gita.bookreading_john.utils.logging
import uz.gita.bookreading_john.utils.toasting
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class BookReadScreen : Fragment(R.layout.screen_book_read), OnPageChangeListener,
    OnPageErrorListener {

    private val binding by viewBinding(ScreenBookReadBinding::bind)
    private val viewModel by viewModels<BookReadViewModelImpl>()
    private val args by navArgs<BookReadScreenArgs>()

    @Inject
    lateinit var sharedPref: MySharedPref

    private var bookName = ""
    private var pageNumber = 0
    private var totalPage = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookName = args.bookName
        val savedPage = args.savedPage
        totalPage = args.totalPage
        pageNumber = savedPage
        val file = File(requireContext().filesDir, bookName)

        if (file.exists()) {
            binding.pdfView.useBestQuality(true)

            binding.pdfView.fromFile(file)
                .enableSwipe(true)
                .defaultPage(pageNumber)
                .swipeHorizontal(true)
                .pageSnap(true)
                .autoSpacing(true)
                .pageFling(true)
                .enableDoubletap(true)
                .enableAnnotationRendering(false)
                .scrollHandle(DefaultScrollHandle(requireContext()))
                .onPageChange(this)
                .onPageError(this)
                .enableAntialiasing(true)
                .spacing(10)
                .nightMode(false)
                .pageFitPolicy(FitPolicy.BOTH)
                .load()
        } else {
            toasting(requireContext(), "Book is not downloaded")
        }

        binding.apply {
            btnBack.setOnClickListener {
                viewModel.popBackStack()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (pageNumber > 0 && pageNumber + 1 != totalPage) {
            sharedPref.bookName = bookName
            sharedPref.savedPage = pageNumber
            sharedPref.totalPage = totalPage
            val percentage: Double = ((pageNumber + 1).toDouble() / totalPage) * 100
            sharedPref.percentage = percentage.toInt()

        } else {
            logging("Book is deleted from recent")
            sharedPref.deleteCurrentBook()
        }
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        pageNumber = page
        totalPage = pageCount
//        binding.txtPages.text = String.format("%s / %s", page + 1, pageCount)
    }

    override fun onPageError(page: Int, t: Throwable?) {
        toasting(requireContext(), "Cannot load page = $page")
    }
}