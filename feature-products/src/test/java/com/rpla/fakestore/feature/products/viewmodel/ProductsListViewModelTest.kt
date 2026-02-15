package com.rpla.fakestore.feature.products.viewmodel

import com.rpla.fakestore.core.domain.entity.ErrorResult
import com.rpla.fakestore.core.domain.entity.ResultBundle
import com.rpla.fakestore.core.domain.usecase.favorites.ObserveFavoriteIdsUseCase
import com.rpla.fakestore.core.domain.usecase.favorites.ToggleFavoriteUseCase
import com.rpla.fakestore.feature.products.domain.GetProductListRequest
import com.rpla.fakestore.feature.products.domain.usecase.GetProductsListUseCase
import com.rpla.fakestore.feature.products.fixtures.ProductFixtures.product1
import com.rpla.fakestore.feature.products.fixtures.ProductFixtures.product4
import com.rpla.fakestore.feature.products.ui.viewmodel.ProductListIntent
import com.rpla.fakestore.feature.products.ui.viewmodel.ProductListState
import com.rpla.fakestore.feature.products.ui.viewmodel.ProductsListViewModel
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
import kotlinx.coroutines.flow.flowOf
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
class ProductsListViewModelTest {
    private val mainDispatcher = StandardTestDispatcher()

    private val getProductsListUseCase: GetProductsListUseCase = mockk()
    private val observeFavoriteIdsUseCase: ObserveFavoriteIdsUseCase = mockk()
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk()

    private lateinit var viewModel: ProductsListViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainDispatcher)

        // Default: no favorites.
        every { observeFavoriteIdsUseCase.invoke() } returns flowOf(emptySet())

        viewModel =
            ProductsListViewModel(
                getProductsListUseCase = getProductsListUseCase,
                observeFavoriteIdsUseCase = observeFavoriteIdsUseCase,
                toggleFavoriteUseCase = toggleFavoriteUseCase,
                dispatcher = mainDispatcher,
            )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when ProductList intent then emits Initial - Loading - ProductsListData with favorite flags from observeFavoriteIds`() =
        runTest(mainDispatcher) {
            // Given
            val products =
                listOf(
                    product1(isFavorite = false),
                    product4(isFavorite = true),
                )

            // IMPORTANT: UI favorite state is driven by ObserveFavoriteIdsUseCase (not Product.isFavorite)
            val favoriteIdsFlow = MutableSharedFlow<Set<Int>>(replay = 1)
            favoriteIdsFlow.emit(setOf(4))
            every { observeFavoriteIdsUseCase.invoke() } returns favoriteIdsFlow

            coEvery { getProductsListUseCase(any<GetProductListRequest>()) } returns
                ResultBundle(
                    data = products,
                    error = null,
                )

            val states = mutableListOf<ProductListState>()

            val collectJob =
                launch(UnconfinedTestDispatcher(testScheduler)) {
                    viewModel.state.take(3).toList(states)
                }

            runCurrent()

            // When
            viewModel.dispatchIntent(ProductListIntent.ProductList)
            advanceUntilIdle()

            // Then
            states.size shouldBe 3
            states[0] shouldBe ProductListState.InitialState
            states[1] shouldBe ProductListState.LoadingState

            val success = states[2] as ProductListState.ProductsListData
            success.items.size shouldBe 2

            success.items[0].id shouldBe 1
            success.items[0].category shouldBe "men's clothing"
            success.items[0].isFavorite shouldBe false
            success.items[0].price shouldBe "109.95 €"

            success.items[1].id shouldBe 4
            success.items[1].category shouldBe "electronics"
            success.items[1].isFavorite shouldBe true
            success.items[1].price shouldBe "64.00 €"

            coVerify(exactly = 1) { getProductsListUseCase(any<GetProductListRequest>()) }
            confirmVerified(getProductsListUseCase)

            collectJob.cancel()
        }

    @Test
    fun `when usecase returns error then emits Initial - Loading - ErrorState`() =
        runTest(mainDispatcher) {
            // Given
            coEvery { getProductsListUseCase(any<GetProductListRequest>()) } returns
                ResultBundle(
                    data = null,
                    error = ErrorResult.NetworkError,
                )

            val states = mutableListOf<ProductListState>()

            val collectJob =
                launch(UnconfinedTestDispatcher(testScheduler)) {
                    viewModel.state.take(3).toList(states)
                }

            runCurrent()

            // When
            viewModel.dispatchIntent(ProductListIntent.ProductList)
            advanceUntilIdle()

            // Then
            states.size shouldBe 3
            states[0] shouldBe ProductListState.InitialState
            states[1] shouldBe ProductListState.LoadingState

            val error = states[2] as ProductListState.ErrorState
            error.message shouldBe ErrorResult.NetworkError.toString()

            coVerify(exactly = 1) { getProductsListUseCase(any<GetProductListRequest>()) }
            confirmVerified(getProductsListUseCase)

            collectJob.cancel()
        }

    @Test
    fun `when ToggleFavorite intent then updates UI optimistically and calls usecase`() =
        runTest(mainDispatcher) {
            // Given
            val favoriteIdsFlow = MutableSharedFlow<Set<Int>>(replay = 1)
            favoriteIdsFlow.emit(emptySet())
            every { observeFavoriteIdsUseCase.invoke() } returns favoriteIdsFlow

            val products = listOf(product1(isFavorite = false))

            coEvery { getProductsListUseCase(any<GetProductListRequest>()) } returns
                ResultBundle(
                    data = products,
                    error = null,
                )

            coEvery { toggleFavoriteUseCase(any()) } returns
                ResultBundle(
                    data = Unit,
                    error = null,
                )

            // Load list
            viewModel.dispatchIntent(ProductListIntent.ProductList)
            advanceUntilIdle()

            val beforeState = viewModel.state.value
            beforeState.shouldBeInstanceOf<ProductListState.ProductsListData>()
            beforeState.items[0].id shouldBe 1
            beforeState.items[0].isFavorite shouldBe false

            // When
            viewModel.dispatchIntent(ProductListIntent.ToggleFavorite(1))

            // Then (optimistic UI)
            val optimisticState = viewModel.state.value as ProductListState.ProductsListData
            optimisticState.items[0].isFavorite shouldBe true

            advanceUntilIdle()

            coVerify(exactly = 1) { toggleFavoriteUseCase(match { it.id == 1 }) }
            confirmVerified(toggleFavoriteUseCase)
        }

    @Test
    fun `when ToggleFavorite usecase fails then optimistic UI is reverted`() =
        runTest(mainDispatcher) {
            // Given
            val favoriteIdsFlow = MutableSharedFlow<Set<Int>>(replay = 1)
            favoriteIdsFlow.emit(emptySet())
            every { observeFavoriteIdsUseCase.invoke() } returns favoriteIdsFlow

            val products = listOf(product1(isFavorite = false))

            coEvery { getProductsListUseCase(any<GetProductListRequest>()) } returns
                ResultBundle(
                    data = products,
                    error = null,
                )

            coEvery { toggleFavoriteUseCase(any()) } returns
                ResultBundle(
                    data = null,
                    error = ErrorResult.GenericError,
                )

            // Load list
            viewModel.dispatchIntent(ProductListIntent.ProductList)
            advanceUntilIdle()

            // When
            viewModel.dispatchIntent(ProductListIntent.ToggleFavorite(1))

            // optimistic
            (viewModel.state.value as ProductListState.ProductsListData).items[0].isFavorite shouldBe true

            advanceUntilIdle()

            // Then: reverted
            (viewModel.state.value as ProductListState.ProductsListData).items[0].isFavorite shouldBe false
        }
}
