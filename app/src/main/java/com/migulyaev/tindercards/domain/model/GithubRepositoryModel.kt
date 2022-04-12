package com.migulyaev.tindercards.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GithubRepositoryModel(
    val id: Long,
    val name: String,
    val owner: Owner,
    val watchers: Int,
    val forks: Int
) {
}