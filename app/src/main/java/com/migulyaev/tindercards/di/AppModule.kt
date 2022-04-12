package com.migulyaev.tindercards.di

import com.migulyaev.tindercards.utils.log.Logger
import org.koin.dsl.module

val appModule = module {
    single { Logger() }
}