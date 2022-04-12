package com.migulyaev.tindercards.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.migulyaev.tindercards.R
import com.migulyaev.tindercards.data.GithubRepositoriesRepository
import com.migulyaev.tindercards.utils.extensions.isNotEmptyOrBlank
import com.migulyaev.tindercards.presentation.base.BaseViewModel
import com.migulyaev.tindercards.utils.extensions.addTo
import com.migulyaev.tindercards.utils.extensions.toLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class SearchViewModel(
    private val githubRepositoriesRepository: GithubRepositoriesRepository
): BaseViewModel() {

    private var searchText: CharSequence = ""
    private val isSearchButtonEnabled = MutableLiveData(searchText.isNotEmptyOrBlank())
    private val showLoadingMutable = MutableLiveData(false)
    private val isSearchCompleted = PublishSubject.create<Unit>()

    fun onSearchTextChanged(text: CharSequence) {
        searchText = text
        isSearchButtonEnabled.value = text.isNotEmptyOrBlank()
    }

    fun onSearchButtonClick() {
        githubRepositoriesRepository.searchRepositories(searchText)
            .doOnSubscribe {
                showLoadingMutable.postValue(true)
            }
            .doFinally {
                showLoadingMutable.postValue(false)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                if (result) {
                    isSearchCompleted.onNext(Unit)
                } else {
                    errorsStream.onNext(R.string.error)
                }
            }, {
                errorsStream.onNext(R.string.error)
            })
            .addTo(disposables)
    }

    fun isSearchButtonEnabled(): LiveData<Boolean> {
        return isSearchButtonEnabled
    }

    fun isShowLoading(): LiveData<Boolean> {
        return showLoadingMutable
    }

    fun isSearchCompleted(): LiveData<Unit> = isSearchCompleted.toLiveData()

}