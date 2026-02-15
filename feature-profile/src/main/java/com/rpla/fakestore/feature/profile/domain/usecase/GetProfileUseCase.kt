package com.rpla.fakestore.feature.profile.domain.usecase

import com.rpla.fakestore.core.domain.base.BaseUseCase
import com.rpla.fakestore.core.domain.entity.ResultBundle
import com.rpla.fakestore.feature.profile.domain.GetProfileRequest
import com.rpla.fakestore.feature.profile.domain.model.Profile
import com.rpla.fakestore.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase
    @Inject
    constructor(
        private val repository: ProfileRepository,
    ) : BaseUseCase<GetProfileRequest, Profile>() {
        override suspend fun run(request: GetProfileRequest): ResultBundle<Profile> = repository.getProfile(request.id)
    }
