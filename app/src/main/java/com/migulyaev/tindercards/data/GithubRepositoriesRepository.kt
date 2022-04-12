package com.migulyaev.tindercards.data

import com.migulyaev.tindercards.data.source.remote.GithubApiService
import com.migulyaev.tindercards.domain.model.GithubRepositoryModel
import com.migulyaev.tindercards.domain.response.SearchRepositoriesResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*

class GithubRepositoriesRepository(
    private val apiService: GithubApiService // Actually, we can pass here 2 layers: network and local. Since now we have only network data source, repo have access straight to api
) {

    private var totalCount: Int = 0
    private var alreadyLoaded: Int = 0
    private var repositories = ArrayDeque<GithubRepositoryModel>()
    private var currentQuery: CharSequence? = null
    private var currentPage = 0
    private val repositoriesSubj = BehaviorSubject.createDefault(repositories)

    fun searchRepositories(query: CharSequence): Single<Boolean> {
        repositories.clear()
        alreadyLoaded = 0
        repositoriesSubj.onNext(repositories)
        currentQuery = query
        currentPage = 1
        return apiService.searchRepositories(query.trim(), currentPage)
            .doOnSuccess(::handleResponse)
            .flatMap { Single.just(true) }
    }

    fun loadMoreRepositories(): Single<Boolean> {
        val query = currentQuery ?: return Single.just(false)
        if (alreadyLoaded >= totalCount) {
            return Single.just(false)
        }
        return apiService.searchRepositories(query.trim(), currentPage)
            .doOnSuccess(::handleResponse)
            .flatMap { Single.just(true) }
    }

    fun likeRepository(repository: GithubRepositoryModel): Single<Boolean> {
        return Single.just(true)
            .doOnSuccess {
                repositories.pollFirst()
                repositoriesSubj.onNext(repositories.clone())
            }
    }

    fun dislikeRepository(repository: GithubRepositoryModel): Single<Boolean> {
        return Single.just(true)
            .doOnSuccess {
                repositories.pollFirst()
                repositoriesSubj.onNext(repositories.clone())
            }
    }

    fun getRepositories(): Observable<ArrayDeque<GithubRepositoryModel>> = repositoriesSubj

    private fun handleResponse(response: SearchRepositoriesResponse) {
        totalCount = response.totalCount
        currentPage++
        alreadyLoaded += response.items.size
        repositories.addAll(response.items)
        repositoriesSubj.onNext(repositories.clone())
    }

}