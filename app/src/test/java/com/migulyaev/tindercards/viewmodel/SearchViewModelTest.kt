package com.migulyaev.tindercards.viewmodel

import com.migulyaev.tindercards.data.GithubRepositoriesRepository
import com.migulyaev.tindercards.getOrAwaitValue
import com.migulyaev.tindercards.presentation.search.SearchViewModel
import com.migulyaev.tindercards.rule.InstantLiveDataRule
import com.migulyaev.tindercards.rule.InstantRxSchedulersRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Single
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class SearchViewModelTest {

    @get:Rule
    val instantLiveDataRule = InstantLiveDataRule()

    @get:Rule
    val instantRxRule = InstantRxSchedulersRule()

    private val repository: GithubRepositoriesRepository = mock {}

    @Test
    fun `Search button is disabled when there is no text`() {
        val vm = SearchViewModel(repository)
        val expectedValue = false
        val actualValue = vm.isSearchButtonEnabled().value
        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Search button is disabled when there is blank`() {
        val vm = SearchViewModel(repository)
        vm.onSearchTextChanged("      ")
        val expectedValue = false
        val actualValue = vm.isSearchButtonEnabled().value
        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Search button is enabled when there is a text`() {
        val vm = SearchViewModel(repository)
        vm.onSearchTextChanged("   sad   ")
        val expectedValue = true
        val actualValue = vm.isSearchButtonEnabled().value
        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Search button is enabled when there is a text and then disabled when there is no text`() {
        val vm = SearchViewModel(repository)
        val expectedValue = false
        vm.onSearchTextChanged("test")
        vm.onSearchTextChanged("")
        val actualValue = vm.isSearchButtonEnabled().value
        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Test search request to repository after button click`() {
        val vm = SearchViewModel(repository)
        whenever(repository.searchRepositories("Q")).thenReturn(Single.just(true))
        vm.onSearchTextChanged("Q")
        vm.onSearchButtonClick()
        verify(repository).searchRepositories("Q")
    }

    @Test
    fun `Search is completed after getting a result`() { // Seems like it can flack, but I'm almost sure it's gonna be ok
        val vm = SearchViewModel(repository)
        whenever(repository.searchRepositories(any())).thenReturn(Single.just(true))
        ScheduledThreadPoolExecutor(1).schedule({
            vm.onSearchTextChanged("asd")
            vm.onSearchButtonClick()
        }, 5, TimeUnit.SECONDS)

        val value = vm.isSearchCompleted().getOrAwaitValue(15, TimeUnit.SECONDS)
        assertEquals(value, Unit)
    }

}