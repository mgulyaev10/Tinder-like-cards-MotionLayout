package com.migulyaev.tindercards.utils.extensions

fun CharSequence.isNotEmptyOrBlank() =
    !(isEmpty() || isBlank())