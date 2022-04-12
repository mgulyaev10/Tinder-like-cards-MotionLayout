package com.migulyaev.tindercards.di

import com.migulyaev.tindercards.data.GithubRepositoriesRepository
import com.migulyaev.tindercards.presentation.cards.RepositoriesCardsViewModel
import com.migulyaev.tindercards.presentation.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    viewModel { SearchViewModel(get()) }
    single { GithubRepositoriesRepository(get()) }

    viewModel { RepositoriesCardsViewModel(get()) }
}