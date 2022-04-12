package com.migulyaev.tindercards.viewmodel

import com.migulyaev.tindercards.data.GithubRepositoriesRepository
import com.migulyaev.tindercards.data.source.remote.GithubApiService
import com.migulyaev.tindercards.domain.model.GithubRepositoryModel
import com.migulyaev.tindercards.domain.model.Owner
import com.migulyaev.tindercards.domain.response.SearchRepositoriesResponse
import com.migulyaev.tindercards.presentation.cards.RepositoriesCardsViewModel
import com.migulyaev.tindercards.presentation.cards.RepositoriesCardsViewState
import com.migulyaev.tindercards.presentation.search.SearchViewModel
import com.migulyaev.tindercards.rule.InstantLiveDataRule
import com.migulyaev.tindercards.rule.InstantRxSchedulersRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Single
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.util.ArrayDeque

class RepositoriesCardsViewModelTest {

    @get:Rule
    val instantLiveDataRule = InstantLiveDataRule()

    @get:Rule
    val instantRxRule = InstantRxSchedulersRule()

    private val apiService: GithubApiService = mock {  }

    @Test
    fun `Test if there is an EMPTY state initially`() {
        val vm = RepositoriesCardsViewModel(GithubRepositoriesRepository(apiService))
        val expectedValue = RepositoriesCardsViewState.EMPTY
        val actualValue = vm.viewState().value
        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Test if there is EMPTY state after loading 0 items`() {
        testViewState(
            apiResponse = SearchRepositoriesResponse(
                totalCount = 0,
                emptyList()
            ),
            expectedState = RepositoriesCardsViewState.EMPTY
        )
    }

    @Test
    fun `Test if there is DEFAULT state after loading 2 items`() {
        testViewState(
            apiResponse = SearchRepositoriesResponse(
                totalCount = 2,
                listOf(
                    DUMMY, DUMMY
                )
            ),
            expectedState = RepositoriesCardsViewState.DEFAULT
        )
    }

    @Test
    fun `Test if there is ONLY_TOP_CARD state after loading 2 items`() {
        testViewState(
            apiResponse = SearchRepositoriesResponse(
                totalCount = 1,
                listOf(
                    DUMMY
                )
            ),
            expectedState = RepositoriesCardsViewState.ONLY_TOP_CARD
        )
    }

    @Test
    fun `Test that card was thrown after like and dislike`() {
        whenever(apiService.searchRepositories(any(), any(), any())).thenReturn(
            Single.just(
                SearchRepositoriesResponse(
                    totalCount = 3,
                    listOf(DUMMY, DUMMY, DUMMY)
                )
            )
        )

        val repository = createRepositoryAndLoadItems()
        var lastValue: ArrayDeque<GithubRepositoryModel>? = null
        var disposable = repository.getRepositories().subscribe {
            lastValue = it
        }
        repository.likeRepository(DUMMY).test().assertComplete()
        assertEquals(2, lastValue?.size)

        repository.dislikeRepository(DUMMY).test().assertComplete()
        assertEquals(1, lastValue?.size)
        disposable.dispose()
    }

    private fun testViewState(
        apiResponse: SearchRepositoriesResponse,
        @RepositoriesCardsViewState expectedState: Int
    ) {
        whenever(apiService.searchRepositories(any(), any(), any())).thenReturn(
            Single.just(apiResponse)
        )

        val repository = createRepositoryAndLoadItems()

        val vm = RepositoriesCardsViewModel(repository)
        val actualValue = vm.viewState().value

        assertEquals(expectedState, actualValue)
    }

    private fun createRepositoryAndLoadItems(): GithubRepositoriesRepository {
        val repository = GithubRepositoriesRepository(apiService)

        val searchViewModel = SearchViewModel(repository)
        searchViewModel.onSearchTextChanged("Test")
        searchViewModel.onSearchButtonClick()
        return repository
    }

    private companion object {
        private val DUMMY = GithubRepositoryModel(
            1, "Test", Owner(1, "https://google.com"), 1, 1
        )
    }
}