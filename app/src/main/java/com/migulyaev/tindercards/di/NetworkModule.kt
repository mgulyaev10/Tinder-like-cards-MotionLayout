package com.migulyaev.tindercards.di

import com.migulyaev.tindercards.BuildConfig
import com.migulyaev.tindercards.data.source.remote.GithubApiService
import com.squareup.moshi.Moshi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {

    single { createService(get()) }

    single { createRetrofit(BuildConfig.BASE_URL) }

    single { MoshiConverterFactory.create() }

    single { Moshi.Builder().build() }

}

fun createRetrofit(url: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(url)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
}

fun createService(retrofit: Retrofit): GithubApiService {
    return retrofit.create(GithubApiService::class.java)
}