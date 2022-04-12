package com.migulyaev.tindercards.utils.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun<T> LiveData<T?>.observeNotNull(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner) {
        if (it != null) {
            observer.onChanged(it)
        }
    }
}