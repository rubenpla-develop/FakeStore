package com.rpla.fakestore.feature.profile.data.remote

import com.rpla.fakestore.feature.profile.data.remote.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileService {
    @GET("users/{id}")
    suspend fun getUser(
        @Path("id") id: Int = 1,
    ): UserDto
}
