package com.rpla.fakestore.feature.profile.ui.viewmodel

import com.rpla.fakestore.core.domain.entity.ErrorResult
import com.rpla.fakestore.core.domain.entity.ResultBundle
import com.rpla.fakestore.core.domain.usecase.favorites.ObserveFavoritesCountUseCase
import com.rpla.fakestore.feature.profile.domain.GetProfileRequest
import com.rpla.fakestore.feature.profile.domain.model.Profile
import com.rpla.fakestore.feature.profile.domain.usecase.GetProfileUseCase
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {
    private val mainDispatcher = StandardTestDispatcher()

    private val getProfileUseCase: GetProfileUseCase = mockk()
    private val observeFavoritesCountUseCase: ObserveFavoritesCountUseCase = mockk()

    private lateinit var favoritesCountFlow: MutableSharedFlow<Int>
    private lateinit var viewModel: ProfileViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainDispatcher)

        favoritesCountFlow = MutableSharedFlow(replay = 1)
        favoritesCountFlow.tryEmit(0)

        every { observeFavoritesCountUseCase.invoke() } returns favoritesCountFlow

        viewModel =
            ProfileViewModel(
                getProfileUseCase = getProfileUseCase,
                observeFavoritesCountUseCase = observeFavoritesCountUseCase,
                dispatcher = mainDispatcher,
            )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when LoadProfile then emits Initial - Loading - Data with favoritesCount`() =
        runTest(mainDispatcher) {
            val profile =
                Profile(
                    id = 8,
                    fullName = "John Doe",
                    username = "user8",
                    email = "user8@mail.com",
                )

            favoritesCountFlow.emit(3)

            coEvery { getProfileUseCase(any<GetProfileRequest>()) } returns
                ResultBundle(
                    data = profile,
                    error = null,
                )

            val states = mutableListOf<ProfileState>()
            val collectJob =
                launch(UnconfinedTestDispatcher(testScheduler)) {
                    viewModel.state.take(3).toList(states)
                }

            runCurrent()

            viewModel.dispatchIntent(ProfileIntent.LoadProfile)
            advanceUntilIdle()

            states.size shouldBe 3
            states[0] shouldBe ProfileState.Initial
            states[1] shouldBe ProfileState.Loading

            val data = states[2].shouldBeInstanceOf<ProfileState.Data>()
            data.profile.fullName shouldBe "John Doe"
            data.favoritesCount shouldBe 3

            collectJob.cancel()
        }

    @Test
    fun `when favoritesCount changes after data then state updates`() =
        runTest(mainDispatcher) {
            val profile =
                Profile(
                    id = 8,
                    fullName = "John Doe",
                    username = "user8",
                    email = "user8@mail.com",
                )

            coEvery { getProfileUseCase(any<GetProfileRequest>()) } returns ResultBundle(profile, null)

            favoritesCountFlow.emit(1)

            viewModel.dispatchIntent(ProfileIntent.LoadProfile)
            advanceUntilIdle()

            (viewModel.state.value as ProfileState.Data).favoritesCount shouldBe 1

            favoritesCountFlow.emit(7)
            advanceUntilIdle()

            (viewModel.state.value as ProfileState.Data).favoritesCount shouldBe 7
        }

    @Test
    fun `loadedOnce prevents a second api call`() =
        runTest(mainDispatcher) {
            val profile =
                Profile(
                    id = 8,
                    fullName = "John Doe",
                    username = "user8",
                    email = "user8@mail.com",
                )

            coEvery { getProfileUseCase(any<GetProfileRequest>()) } returns ResultBundle(profile, null)

            viewModel.dispatchIntent(ProfileIntent.LoadProfile)
            advanceUntilIdle()

            viewModel.dispatchIntent(ProfileIntent.LoadProfile)
            advanceUntilIdle()

            coVerify(exactly = 1) { getProfileUseCase(match { it.id == 8 }) }
            confirmVerified(getProfileUseCase)
        }

    @Test
    fun `when usecase fails then emits Error`() =
        runTest(mainDispatcher) {
            coEvery { getProfileUseCase(any<GetProfileRequest>()) } returns
                ResultBundle(
                    data = null,
                    error = ErrorResult.NetworkError,
                )

            viewModel.dispatchIntent(ProfileIntent.LoadProfile)
            advanceUntilIdle()

            viewModel.state.value.shouldBeInstanceOf<ProfileState.Error>()
        }
}
