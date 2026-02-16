package com.rpla.fakestore.feature.profile.data

import com.rpla.fakestore.core.domain.entity.ErrorResult
import com.rpla.fakestore.core.network.exception.RemoteException
import com.rpla.fakestore.core.network.mapper.ErrorMapper
import com.rpla.fakestore.feature.profile.data.remote.ProfileService
import com.rpla.fakestore.feature.profile.data.remote.dto.NameDto
import com.rpla.fakestore.feature.profile.data.remote.dto.UserDto
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProfileRepositoryImplTest {
    private val service: ProfileService = mockk()
    private lateinit var repository: ProfileRepositoryImpl

    @BeforeEach
    fun setUp() {
        repository =
            ProfileRepositoryImpl(
                profileService = service,
                errorMapper = ErrorMapper(),
            )
    }

    @Test
    fun `getProfile returns data when api succeeds`() =
        runTest {
            coEvery { service.getUser(8) } returns
                UserDto(
                    id = 8,
                    username = "user8",
                    name = NameDto(firstname = "John", lastname = "Doe"),
                    email = "user8@mail.com",
                )

            val result = repository.getProfile(8)

            result.error shouldBe null
            result.data?.id shouldBe 8
            result.data?.username shouldBe "user8"
            result.data?.email shouldBe "user8@mail.com"
            result.data?.fullName shouldBe "John Doe"
        }

    @Test
    fun `getProfile returns NetworkError when RemoteException NoNetworkError is thrown`() =
        runTest {
            coEvery { service.getUser(8) } throws RemoteException.NoNetworkError("no network")

            val result = repository.getProfile(8)

            result.data shouldBe null
            result.error shouldBe ErrorResult.NetworkError
        }

    @Test
    fun `getProfile returns ClientError when RemoteException ClientError is thrown`() =
        runTest {
            coEvery { service.getUser(8) } throws RemoteException.ClientError("400")

            val result = repository.getProfile(8)

            result.data shouldBe null
            result.error shouldBe ErrorResult.ClientError
        }

    @Test
    fun `getProfile returns GenericError when unexpected Throwable is thrown`() =
        runTest {
            coEvery { service.getUser(8) } throws RuntimeException("boom")

            val result = repository.getProfile(8)

            result.data shouldBe null
            result.error shouldBe ErrorResult.GenericError
        }
}
