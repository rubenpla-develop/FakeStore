package com.rpla.feature_products.viewmodel

import com.rpla.core_domain.entity.ErrorResult
import com.rpla.core_domain.entity.ResultBundle
import com.rpla.feature_products.domain.GetProductListRequest
import com.rpla.feature_products.domain.usecase.GetProductsListUseCase
import com.rpla.feature_products.fixtures.ProductFixtures.product1
import com.rpla.feature_products.fixtures.ProductFixtures.product4
import com.rpla.feature_products.ui.viewmodel.ProductListIntent
import com.rpla.feature_products.ui.viewmodel.ProductListState
import com.rpla.feature_products.ui.viewmodel.ProductsListViewModel
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private val useCase: GetProductsListUseCase = mockk()

    private lateinit var viewModel: ProductsListViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainDispatcher)
        viewModel = ProductsListViewModel(
            getProductsListUseCase = useCase,
            dispatcher = mainDispatcher,
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when ProductList intent then emits Initial - Loading - ProductsListData`() =
        runTest(mainDispatcher) {
            // Given
            val products = listOf(
                product1(isFavorite = false),
                product4(isFavorite = true),
            )

            coEvery { useCase(any<GetProductListRequest>()) } returns ResultBundle(
                data = products,
                error = null,
            )

            val states = mutableListOf<ProductListState>()

            val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
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

            coVerify(exactly = 1) { useCase(any<GetProductListRequest>()) }
            confirmVerified(useCase)

            collectJob.cancel()
        }

    @Test
    fun `when usecase returns error then emits Initial - Loading - ErrorState`() =
        runTest(mainDispatcher) {
            // Given
            coEvery { useCase(any<GetProductListRequest>()) } returns ResultBundle(
                data = null,
                error = ErrorResult.NetworkError,
            )

            val states = mutableListOf<ProductListState>()

            val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
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

            coVerify(exactly = 1) { useCase(any<GetProductListRequest>()) }
            confirmVerified(useCase)

            collectJob.cancel()
        }
}
