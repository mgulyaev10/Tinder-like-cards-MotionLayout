package com.migulyaev.tindercards.presentation.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.migulyaev.tindercards.utils.extensions.addTo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseMvvmFragment<V: BaseViewModel>(
    layoutId: Int
): Fragment(layoutId) {

    private val disposables = CompositeDisposable()

    protected abstract val viewModel: V

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.errorsStream()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::showError)
            .addTo(disposables)
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }

    protected open fun showError(@StringRes resId: Int) {
        Toast.makeText(requireContext(), resId, Toast.LENGTH_SHORT).show()
    }

}