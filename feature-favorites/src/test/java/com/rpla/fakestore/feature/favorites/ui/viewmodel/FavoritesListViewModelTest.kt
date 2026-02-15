package com.rpla.fakestore.feature.favorites.ui.viewmodel

import com.rpla.fakestore.core.domain.entity.ResultBundle
import com.rpla.fakestore.core.domain.usecase.favorites.ObserveFavoritesUseCase
import com.rpla.fakestore.core.domain.usecase.favorites.RemoveFavoriteUseCase
import com.rpla.fakestore.core.model.Product
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
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
class FavoritesListViewModelTest {
    private val mainDispatcher = StandardTestDispatcher()

    private val observeFavoritesUseCase: ObserveFavoritesUseCase = mockk()
    private val removeFavoriteUseCase: RemoveFavoriteUseCase = mockk()

    private lateinit var viewModel: FavoritesListViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainDispatcher)

        // Default flow (empty)
        every { observeFavoritesUseCase.invoke() } returns MutableSharedFlow(replay = 1)

        viewModel =
            FavoritesListViewModel(
                observeFavoritesUseCase = observeFavoritesUseCase,
                removeFavoriteUseCase = removeFavoriteUseCase,
                dispatcher = mainDispatcher,
            )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when FavoritesList intent then emits Initial - Loading - FavoritesListData`() =
        runTest(mainDispatcher) {
            // Given
            val flow = MutableSharedFlow<List<Product>>(replay = 1)
            every { observeFavoritesUseCase.invoke() } returns flow

            flow.emit(
                listOf(
                    Product(
                        id = 1,
                        title = "Product 1",
                        price = 10.0,
                        description = "desc",
                        category = "cat",
                        imageUrl = "url",
                    ),
                ),
            )

            val states = mutableListOf<FavoritesListState>()
            val collectJob =
                launch(UnconfinedTestDispatcher(testScheduler)) {
                    viewModel.state.take(3).toList(states)
                }

            runCurrent()

            // When
            viewModel.dispatchIntent(FavoritesListIntent.FavoritesList)
            advanceUntilIdle()

            // Then
            states.size shouldBe 3
            states[0] shouldBe FavoritesListState.InitialState
            states[1] shouldBe FavoritesListState.LoadingState

            val data = states[2]
            data.shouldBeInstanceOf<FavoritesListState.FavoritesListData>()
            data.items.size shouldBe 1
            data.items[0].id shouldBe 1
            data.items[0].isFavorite shouldBe true // forceFavorite = true

            collectJob.cancel()
        }

    @Test
    fun `when RemoveFavorite intent then calls usecase`() =
        runTest(mainDispatcher) {
            // Given
            coEvery { removeFavoriteUseCase(10) } returns ResultBundle(data = Unit, error = null)

            // When
            viewModel.dispatchIntent(FavoritesListIntent.RemoveFavorite(10))
            advanceUntilIdle()

            // Then
            coVerify(exactly = 1) { removeFavoriteUseCase(10) }
        }

    @Test
    fun `when observeFavorites throws then emits ErrorState`() =
        runTest(mainDispatcher) {
            // Given
            every {
                observeFavoritesUseCase.invoke()
            } returns
                flow {
                    throw RuntimeException("boom")
                }

            val states = mutableListOf<FavoritesListState>()
            val collectJob =
                launch(UnconfinedTestDispatcher(testScheduler)) {
                    viewModel.state.take(3).toList(states)
                }

            runCurrent()

            // When
            viewModel.dispatchIntent(FavoritesListIntent.FavoritesList)
            advanceUntilIdle()

            // Then
            states.size shouldBe 3
            states[0] shouldBe FavoritesListState.InitialState
            states[1] shouldBe FavoritesListState.LoadingState

            val error = states[2]
            error.shouldBeInstanceOf<FavoritesListState.ErrorState>()
            error.message shouldBe "boom"

            collectJob.cancel()
        }
}
