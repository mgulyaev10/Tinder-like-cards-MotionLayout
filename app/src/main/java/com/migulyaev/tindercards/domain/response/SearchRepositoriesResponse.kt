package com.migulyaev.tindercards.domain.response

import com.migulyaev.tindercards.domain.model.GithubRepositoryModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchRepositoriesResponse(
    @Json(name = "total_count") val totalCount: Int,
    val items: List<GithubRepositoryModel>
)