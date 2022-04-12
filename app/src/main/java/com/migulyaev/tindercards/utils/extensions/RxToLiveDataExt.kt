package com.migulyaev.tindercards.utils.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Suppress("unused")
fun <T> Observable<T>.toLiveData(): LiveData<T> =
    Flowable.fromObservable(this, BackpressureStrategy.LATEST).toLiveData()

@Suppress("unused")
fun <T> Single<T>.toLiveData(): LiveData<T> =
    Flowable.fromSingle(this).toLiveData()

@Suppress("unused")
fun <T> Flowable<T>.toLiveData(): LiveData<T> =
    LiveDataReactiveStreams.fromPublisher(this)