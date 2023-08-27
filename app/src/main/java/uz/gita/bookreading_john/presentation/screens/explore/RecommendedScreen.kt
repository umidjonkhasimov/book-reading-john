package uz.gita.bookreading_john.presentation.screens.explore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.bookreading_john.R
import uz.gita.bookreading_john.databinding.ScreenRecommendedBinding
import uz.gita.bookreading_john.presentation.adapters.SearchAdapter

@AndroidEntryPoint
class RecommendedScreen : Fragment(R.layout.screen_recommended) {

    private val binding by viewBinding(ScreenRecommendedBinding::bind)
    private val viewModel by viewModels<RecommendedViewModelImpl>()
    private lateinit var adapter: SearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SearchAdapter()
        adapter.setOnItemClickListener { bookData ->
            val action = RecommendedScreenDirections.actionRecommendedScreenToAboutBookScreen(bookData)
            findNavController().navigate(action)
        }

        binding.apply {
            rvRecommended.layoutManager =
                LinearLayoutManager(requireContext())
            rvRecommended.adapter = adapter
        }

        viewModel.recommendedBooks.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.loadingData.observe(viewLifecycleOwner) {
            val isLoad = if (it) View.VISIBLE else View.GONE
            binding.progressBar.visibility = isLoad
        }
    }
}