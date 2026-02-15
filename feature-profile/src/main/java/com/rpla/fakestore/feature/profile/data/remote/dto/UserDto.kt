package com.rpla.fakestore.feature.profile.data.remote.dto

import com.squareup.moshi.Json

data class UserDto(
    @Json(name = "id") val id: Int,
    @Json(name = "username") val username: String,
    @Json(name = "name") val name: NameDto,
    @Json(name = "email") val email: String,
)

data class NameDto(
    @Json(name = "firstname") val firstname: String,
    @Json(name = "lastname") val lastname: String,
)
