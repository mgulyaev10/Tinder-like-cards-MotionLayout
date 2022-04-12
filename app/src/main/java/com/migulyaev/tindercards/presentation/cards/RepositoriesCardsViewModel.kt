package com.migulyaev.tindercards.presentation.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.migulyaev.tindercards.utils.extensions.addTo
import com.migulyaev.tindercards.data.GithubRepositoriesRepository
import com.migulyaev.tindercards.domain.model.GithubRepositoryModel
import com.migulyaev.tindercards.presentation.base.BaseViewModel
import com.migulyaev.tindercards.utils.extensions.toLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*

class RepositoriesCardsViewModel(
    private val githubRepositoriesRepository: GithubRepositoriesRepository
): BaseViewModel() {

    private var nonActiveRepositoriesStack: ArrayDeque<GithubRepositoryModel>? = null
    private val currentRepositoryMutable = MutableLiveData<GithubRepositoryModel?>()
    private val nextRepositoryMutable = MutableLiveData<GithubRepositoryModel?>()
    private val viewStateMutable = MutableLiveData(RepositoriesCardsViewState.DEFAULT)
    private val backSignal = PublishSubject.create<Unit>()

    init {
        githubRepositoriesRepository.getRepositories()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::updateRepositories)
            .addTo(disposables)
    }

    fun onLikeClick() {
        val repository = currentRepositoryMutable.value ?: return // can throw exception in debug build if repository is null to prevent inconsistent UI
        githubRepositoriesRepository.likeRepository(repository)
            .doOnSubscribe {
                logger.i(this, "Calling repository's Like method to perform API request: $repository...")
            }
            .transformLikeDislikeStreamsAndHandleResult()
    }

    fun onDislikeClick() {
        val repository = currentRepositoryMutable.value ?: return // can throw exception in debug build if repository is null to prevent inconsistent UI
        githubRepositoriesRepository.dislikeRepository(repository)
            .doOnSubscribe {
                logger.i(this, "Calling repository's Dislike method to perform API request $repository...")
            }
            .transformLikeDislikeStreamsAndHandleResult()
    }

    fun onRetryClick() {
        loadMoreRepositories()
            .subscribeOnRepositoriesLoading()
            .addTo(disposables)
    }

    fun onBackClick() {
        backSignal.onNext(Unit)
    }

    fun currentRepository(): LiveData<GithubRepositoryModel?> {
        return currentRepositoryMutable
    }

    fun nextRepository(): LiveData<GithubRepositoryModel?> {
        return nextRepositoryMutable
    }

    fun viewState(): LiveData<Int> {
        return viewStateMutable
    }

    fun backSignal(): LiveData<Unit> {
        return backSignal.toLiveData()
    }

    private fun loadMoreRepositories(): Single<Boolean> {
        return githubRepositoriesRepository.loadMoreRepositories()
            .doOnSubscribe {
                viewStateMutable.postValue(RepositoriesCardsViewState.LOADING)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun Single<Boolean>.transformLikeDislikeStreamsAndHandleResult() {
        flatMap {
            if (nonActiveRepositoriesStack.isNullOrEmpty()) {
                loadMoreRepositories()
            } else {
                Single.just(true)
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOnRepositoriesLoading()
            .addTo(disposables)
    }

    private fun Single<Boolean>.subscribeOnRepositoriesLoading(): Disposable {
        return subscribe({ result ->
            if (!result) {
                viewStateMutable.postValue(getViewState())
            }
        } , {
            viewStateMutable.postValue(RepositoriesCardsViewState.ERROR)
        })
    }

    private fun updateRepositories(repositories: ArrayDeque<GithubRepositoryModel>) {
        val copy = repositories.clone()
        currentRepositoryMutable.value = copy.pollFirst()
        nextRepositoryMutable.value = copy.peekFirst()
        viewStateMutable.value = getViewState()
        this.nonActiveRepositoriesStack = copy
    }

    @RepositoriesCardsViewState
    private fun getViewState(): Int {
        return when {
            currentRepositoryMutable.value != null && nextRepositoryMutable.value != null -> RepositoriesCardsViewState.DEFAULT
            currentRepositoryMutable.value != null && nextRepositoryMutable.value == null -> RepositoriesCardsViewState.ONLY_TOP_CARD
            else -> RepositoriesCardsViewState.EMPTY
        }
    }

}