package com.migulyaev.tindercards.utils.extensions

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

fun Disposable.addTo(disposables: CompositeDisposable) {
    disposables.add(this)
}