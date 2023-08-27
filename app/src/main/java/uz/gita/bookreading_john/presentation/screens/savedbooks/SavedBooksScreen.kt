package uz.gita.bookreading_john.presentation.screens.savedbooks

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.bookreading_john.R
import uz.gita.bookreading_john.data.source.local.sharedpref.MySharedPref
import uz.gita.bookreading_john.databinding.ScreenSavedBinding
import uz.gita.bookreading_john.presentation.adapters.BookAdapter
import uz.gita.bookreading_john.utils.logging
import uz.gita.bookreading_john.utils.toasting
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class SavedBooksScreen : Fragment(R.layout.screen_saved) {

    private val binding by viewBinding(ScreenSavedBinding::bind)
    private val viewModel by viewModels<SavedViewModelImpl>()
    private lateinit var adapter: BookAdapter

    @Inject
    lateinit var sharedPref: MySharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllData(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = BookAdapter()
        if (sharedPref.bookName.isEmpty()) {
            binding.view.visibility = View.GONE
        } else {
            binding.apply {
                binding.view.visibility = View.VISIBLE
                txtBookName.text = sharedPref.bookName
                percentageView.text = "${sharedPref.percentage}%"
            }
        }

        adapter.setOnClickListener { bookData ->
            viewModel.navigateToReadBookScreen(bookData.title, 0, bookData.page.toInt())
        }

        adapter.setOnLongClickListener { bookData ->
            val file = File(requireContext().filesDir, bookData.title)


            AlertDialog.Builder(requireContext())
                .setTitle("Are you sure?")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton("Yes") { dialog, p1 ->
                    val deleted = if (file.exists()) file.delete() else false

                    if (deleted) {
                        viewModel.getAllData(requireContext())
                        toasting(requireContext(), "Book deleted")
                    } else {
                        toasting(requireContext(), "File not found")
                    }
                    if (sharedPref.bookName == bookData.title) sharedPref.deleteCurrentBook()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }

        binding.apply {
            recycler.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            recycler.adapter = adapter

            // Last Read book
            btnLastBook.setOnClickListener {
                viewModel.navigateToReadBookScreen(
                    sharedPref.bookName,
                    sharedPref.savedPage,
                    sharedPref.totalPage
                )
            }
        }

        viewModel.booksData.observe(viewLifecycleOwner) {
            logging("SavedScreen Data = ${it.size}")
            if (it.isEmpty()) {
                binding.apply {
                    imgNoBooks.visibility = View.VISIBLE
                    txtNoBookTitle.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    imgNoBooks.visibility = View.GONE
                    txtNoBookTitle.visibility = View.GONE
                }
            }
            adapter.submitList(it)
        }

        viewModel.errorData.observe(viewLifecycleOwner) {
            toasting(requireContext(), it)
            logging("SavedScreen error = $it")
        }
    }
}