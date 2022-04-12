package com.migulyaev.tindercards.presentation.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.migulyaev.tindercards.R
import com.migulyaev.tindercards.presentation.view.utils.SimpleTextChangedListener
import com.migulyaev.tindercards.databinding.FragmentSearchBinding
import com.migulyaev.tindercards.presentation.base.BaseMvvmFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment: BaseMvvmFragment<SearchViewModel>(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding

    override val viewModel: SearchViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        configureViews()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isSearchButtonEnabled().observe(viewLifecycleOwner) {
            binding.searchButton.isEnabled = it
        }

        viewModel.isShowLoading().observe(viewLifecycleOwner, ::showLoading)

        viewModel.isSearchCompleted().observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.repositoriesFragment)
        }
    }

    private fun configureViews() {
        binding.searchEditText.addTextChangedListener(SimpleTextChangedListener(viewModel::onSearchTextChanged))
        binding.searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                viewModel.onSearchButtonClick()
                true
            } else {
                false
            }
        }
        binding.searchButton.setOnClickListener {
            viewModel.onSearchButtonClick()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.searchButton.isVisible = !isLoading
        binding.progressBar.isVisible = isLoading
    }

}