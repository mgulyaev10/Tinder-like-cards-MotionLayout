package com.migulyaev.tindercards.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Owner(
    val id: Long,
    @Json(name = "avatar_url") val avatarUrl: String
)