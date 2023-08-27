package uz.gita.bookreading_john.presentation.screens.home

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.bookreading_john.R
import uz.gita.bookreading_john.data.model.CategoryData
import uz.gita.bookreading_john.data.model.CategoryItemData
import uz.gita.bookreading_john.databinding.ScreenHomeBinding
import uz.gita.bookreading_john.presentation.adapters.HomeAdapter
import uz.gita.bookreading_john.presentation.adapters.SearchAdapter
import uz.gita.bookreading_john.presentation.adapters.TabAdapter
import uz.gita.bookreading_john.utils.logging
import uz.gita.bookreading_john.utils.toasting
import javax.inject.Inject

@AndroidEntryPoint
class HomeScreen : Fragment(R.layout.screen_home) {
    private val binding by viewBinding(ScreenHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModelImpl>()
    private lateinit var adapter: HomeAdapter
    private lateinit var tabAdapter: TabAdapter
    private lateinit var searchAdapter: SearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = HomeAdapter()
        tabAdapter = TabAdapter()
        searchAdapter = SearchAdapter()

        tabAdapter.setOnItemClickListener { categoryTitle ->
            logging(categoryTitle)
            if (categoryTitle == "All books") {
                viewModel.getAllBooks()
                binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
                binding.rvHome.adapter = adapter
            } else {
                viewModel.getBooksByCategory(categoryTitle)
                searchAdapter.setOnItemClickListener {
                    viewModel.navigateToAboutBookScreen(it)
                }
                binding.rvHome.adapter = searchAdapter
                binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
            }
        }

        adapter.setOnItemClickListener { bookData ->
            viewModel.navigateToAboutBookScreen(bookData)
        }

        setUpSearch()

        viewModel.allBooksData.observe(viewLifecycleOwner) { categoryData ->
            adapter.submitList(categoryData)

            val list = ArrayList<CategoryItemData>()
            list.add(CategoryItemData("All books", true))
            list.addAll(categoryData.map { it.toTabItem() })
            tabAdapter.submitList(list)
        }

        viewModel.bookByCategory.observe(viewLifecycleOwner) {
            searchAdapter.submitList(it)
            logging(it.toString())
        }

        viewModel.searchedBooks.observe(viewLifecycleOwner) {
            searchAdapter.submitList(it)
            logging(it.toString())
        }

        viewModel.errorData.observe(viewLifecycleOwner) { errorMessage ->
            toasting(requireContext(), errorMessage)
            logging("Home screen = $errorMessage")
        }

        viewModel.loadingData.observe(viewLifecycleOwner) {
            val isLoad = if (it) View.VISIBLE else View.GONE
            binding.progressBar.visibility = isLoad
        }

        binding.apply {
            rvHome.layoutManager = LinearLayoutManager(requireContext())
            rvHome.adapter = adapter

            rvCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvCategory.adapter = tabAdapter
        }
    }

    private fun setUpSearch() {
        binding.apply {
            etSearchHome.doAfterTextChanged { text ->
                if (text.isNullOrBlank()) {
                    viewModel.getAllBooks()
                    binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvHome.adapter = adapter
                } else {
                    viewModel.searchForBook(text.toString())
                    searchAdapter = SearchAdapter()
                    searchAdapter.setOnItemClickListener {
                        viewModel.navigateToAboutBookScreen(it)
                    }
                    binding.rvHome.adapter = searchAdapter
                    binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
                }
            }
        }
    }
}