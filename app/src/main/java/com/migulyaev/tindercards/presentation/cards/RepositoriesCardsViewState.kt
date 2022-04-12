package com.migulyaev.tindercards.presentation.cards

import androidx.annotation.IntDef
import com.migulyaev.tindercards.presentation.cards.RepositoriesCardsViewState.Companion.DEFAULT
import com.migulyaev.tindercards.presentation.cards.RepositoriesCardsViewState.Companion.EMPTY
import com.migulyaev.tindercards.presentation.cards.RepositoriesCardsViewState.Companion.ERROR
import com.migulyaev.tindercards.presentation.cards.RepositoriesCardsViewState.Companion.ONLY_TOP_CARD

@IntDef(DEFAULT, ONLY_TOP_CARD, EMPTY, ERROR)
@Retention(AnnotationRetention.SOURCE)
annotation class RepositoriesCardsViewState {
    companion object {
        const val DEFAULT = 100
        const val ONLY_TOP_CARD = 101
        const val EMPTY = 102
        const val ERROR = 103
        const val LOADING = 104
    }
}