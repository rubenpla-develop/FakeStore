package com.rpla.fakestore.feature.profile.domain.repository

import com.rpla.fakestore.core.domain.entity.ResultBundle
import com.rpla.fakestore.feature.profile.domain.model.Profile

interface ProfileRepository {
    suspend fun getProfile(profileId: Int): ResultBundle<Profile>
}
