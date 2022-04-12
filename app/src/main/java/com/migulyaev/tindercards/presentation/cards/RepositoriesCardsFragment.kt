package com.migulyaev.tindercards.presentation.cards

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.migulyaev.tindercards.R
import com.migulyaev.tindercards.databinding.FragmentRepositoriesCardsBinding
import com.migulyaev.tindercards.presentation.base.BaseMvvmFragment
import com.migulyaev.tindercards.presentation.view.utils.TransitionListener
import com.migulyaev.tindercards.utils.extensions.observeNotNull
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoriesCardsFragment: BaseMvvmFragment<RepositoriesCardsViewModel>(R.layout.fragment_repositories_cards) {

    private lateinit var binding: FragmentRepositoriesCardsBinding

    override val viewModel: RepositoriesCardsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRepositoriesCardsBinding.bind(view)
        configureViews()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.currentRepository().observeNotNull(viewLifecycleOwner) {
            binding.cardViewTop.setRepository(it)
        }

        viewModel.nextRepository().observeNotNull(viewLifecycleOwner) {
            binding.cardViewBottom.setRepository(it)
        }

        viewModel.viewState().observe(viewLifecycleOwner) {
            changeViewState(it)
        }

        viewModel.backSignal().observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun configureViews() {
        binding.likeButton.setOnClickListener {
            binding.cardsMotionLayout.transitionToState(R.id.like_position)
        }
        binding.dislikeButton.setOnClickListener {
            binding.cardsMotionLayout.transitionToState(R.id.dislike_position)
        }
        binding.backButton.setOnClickListener {
            viewModel.onBackClick()
        }
        binding.retryButton.setOnClickListener {
            viewModel.onRetryClick()
        }

        binding.cardsMotionLayout.setTransitionListener(TransitionListener(
            onTransitionStarted = { _, _ , _ -> handleStartTransition() },
            onTransitionChange = { _, _, endId, progress -> handleTransitionChange(endId, progress) },
            onTransitionCompleted = { _, currentId -> handleTransitionCompleted(currentId) }
        ))

    }

    private fun handleStartTransition() {
        binding.cardViewTop.setLikeIconAlpha(0f)
        binding.cardViewTop.setDislikeIconAlpha(0f)
        binding.likeButton.isEnabled = false
        binding.dislikeButton.isEnabled = false
    }

    private fun handleTransitionChange(endId: Int, progress: Float) {
        when (endId) {
            R.id.like_position -> {
                binding.cardViewTop.setLikeIconAlpha(progress)
            }
            R.id.dislike_position -> {
                binding.cardViewTop.setDislikeIconAlpha(progress)
            }
        }
    }

    private fun handleTransitionCompleted(id: Int) {
        when (id) {
            R.id.after_dislike_position -> {
                binding.cardsMotionLayout.progress = 0f
                binding.cardsMotionLayout.setTransition(
                    R.id.start_position,
                    R.id.like_position
                )
                viewModel.onDislikeClick()
            }
            R.id.after_like_position -> {
                binding.cardsMotionLayout.progress = 0f
                binding.cardsMotionLayout.setTransition(
                    R.id.start_position,
                    R.id.like_position
                )
                viewModel.onLikeClick()
            }
        }
        binding.likeButton.isEnabled = true
        binding.dislikeButton.isEnabled = true
    }

    private fun changeViewState(@RepositoriesCardsViewState state: Int) {
        binding.cardViewTop.isVisible = state == RepositoriesCardsViewState.DEFAULT || state == RepositoriesCardsViewState.ONLY_TOP_CARD
        binding.cardViewBottom.isVisible = state == RepositoriesCardsViewState.DEFAULT
        binding.buttonsContainer.isVisible = state == RepositoriesCardsViewState.DEFAULT || state == RepositoriesCardsViewState.ONLY_TOP_CARD
        binding.emptyContainer.isVisible = state == RepositoriesCardsViewState.EMPTY
        binding.errorContainer.isVisible = state == RepositoriesCardsViewState.ERROR
        binding.progressView.isVisible = state == RepositoriesCardsViewState.LOADING
    }

}