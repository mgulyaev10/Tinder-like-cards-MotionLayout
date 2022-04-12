package com.migulyaev.tindercards.utils.log

import android.util.Log

class Logger {

    fun logError(caller: Any, error: Throwable) {
        Log.e(caller.toString(), "Error: ", error)
    }

    fun i(caller: Any, message: String) {
        Log.i(caller.toString(), message)
    }

}