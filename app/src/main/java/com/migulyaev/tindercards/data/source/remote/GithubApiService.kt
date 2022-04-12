package com.migulyaev.tindercards.data.source.remote

import com.migulyaev.tindercards.domain.response.SearchRepositoriesResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApiService {

    @GET("/search/repositories")
    fun searchRepositories(
        @Query("q") query: CharSequence,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = DEFAULT_PER_PAGE
    ): Single<SearchRepositoriesResponse>

    private companion object {
        // For easy testing the test assignment
        private const val DEFAULT_PER_PAGE = 10
    }

}