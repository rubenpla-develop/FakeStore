package com.rpla.fakestore.feature.profile.data

import com.rpla.fakestore.core.domain.entity.ResultBundle
import com.rpla.fakestore.core.network.exception.RemoteException
import com.rpla.fakestore.core.network.mapper.ErrorMapper
import com.rpla.fakestore.core.network.retrofit.safeApiCall
import com.rpla.fakestore.feature.profile.data.mapper.toModel
import com.rpla.fakestore.feature.profile.data.remote.ProfileService
import com.rpla.fakestore.feature.profile.domain.model.Profile
import com.rpla.fakestore.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl
    @Inject
    constructor(
        private val profileService: ProfileService,
        private val errorMapper: ErrorMapper,
    ) : ProfileRepository {
        override suspend fun getProfile(profileId: Int): ResultBundle<Profile> =
            try {
                val profile = safeApiCall { profileService.getUser(profileId) }
                ResultBundle(
                    data = profile.toModel(),
                    error = null,
                )
            } catch (e: RemoteException) {
                errorMapper.mapRemoteErrorRecord(e)
            } catch (t: Throwable) {
                errorMapper.mapUnexpectedThrowableErrorResult(t)
            }
    }
