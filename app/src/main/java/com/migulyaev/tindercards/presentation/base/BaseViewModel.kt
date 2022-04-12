package com.migulyaev.tindercards.presentation.base

import androidx.lifecycle.ViewModel
import com.migulyaev.tindercards.utils.log.Logger
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject

abstract class BaseViewModel(
    protected val logger: Logger = Logger()
): ViewModel() {

    protected val disposables = CompositeDisposable()
    protected val errorsStream: PublishSubject<Int> = PublishSubject.create<Int>()

    fun errorsStream(): Observable<Int> = errorsStream

    override fun onCleared() {
        disposables.clear()
    }

}