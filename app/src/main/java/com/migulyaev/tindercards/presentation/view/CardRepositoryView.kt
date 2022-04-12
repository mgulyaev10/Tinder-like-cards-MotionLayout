package com.migulyaev.tindercards.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.card.MaterialCardView
import com.migulyaev.tindercards.R
import com.migulyaev.tindercards.databinding.CardRepositoryViewBinding
import com.migulyaev.tindercards.domain.model.GithubRepositoryModel

class CardRepositoryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: CardRepositoryViewBinding

    init {
        LayoutInflater.from(context).inflate(R.layout.card_repository_view, this, true)
        binding = CardRepositoryViewBinding.bind(this)
    }

    fun setLikeIconAlpha(value: Float) {
        binding.cardViewLikeIcon.alpha = value
    }

    fun setDislikeIconAlpha(value: Float) {
        binding.cardViewDislikeIcon.alpha = value
    }

    fun setRepository(repository: GithubRepositoryModel) {
        binding.cardName.text = repository.name
        binding.cardWatchersText.text = repository.watchers.toString()
        binding.cardForkText.text = repository.forks.toString()
        binding.cardImage.setImageURI(repository.owner.avatarUrl)
    }

}