package com.rpla.fakestore.feature.profile.data.mapper

import com.rpla.fakestore.feature.profile.data.remote.dto.UserDto
import com.rpla.fakestore.feature.profile.domain.model.Profile

fun UserDto.toModel(): Profile =
    Profile(
        id = id,
        username = username,
        fullName = name.firstname + " " + name.lastname,
        email = email,
    )
