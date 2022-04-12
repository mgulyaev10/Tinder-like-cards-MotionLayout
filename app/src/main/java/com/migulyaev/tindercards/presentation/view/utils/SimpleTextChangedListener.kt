package com.migulyaev.tindercards.presentation.view.utils

import android.text.Editable
import android.text.TextWatcher

class SimpleTextChangedListener(
    private val onNext: (CharSequence) -> Unit
): TextWatcher {

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =
        onNext(p0 ?: "")

    override fun afterTextChanged(p0: Editable?) = Unit
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

}