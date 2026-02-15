package com.rpla.fakestore.feature.products.usecase

import com.rpla.fakestore.core.domain.entity.ErrorResult
import com.rpla.fakestore.core.domain.entity.ResultBundle
import com.rpla.fakestore.core.model.Product
import com.rpla.fakestore.feature.products.domain.GetProductListRequest
import com.rpla.fakestore.feature.products.domain.repository.ProductsRepository
import com.rpla.fakestore.feature.products.domain.usecase.GetProductsListUseCase
import com.rpla.fakestore.feature.products.fixtures.ProductFixtures.product1
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class GetProductsListUseCaseTest {
    private val repository: ProductsRepository = mockk()
    private val useCase = GetProductsListUseCase(repository)

    @AfterEach
    fun tearDown() {
        confirmVerified(repository)
    }

    @Test
    fun `when repository returns data then usecase returns same ResultBundle`() =
        runTest {
            // Given
            val expected =
                ResultBundle(
                    data = listOf(product1(isFavorite = false)),
                    error = null,
                )

            coEvery { repository.getProducts() } returns expected

            // When
            val result = useCase(GetProductListRequest())

            // Then
            result shouldBe expected
            coVerify(exactly = 1) { repository.getProducts() }
        }

    @Test
    fun `when repository returns error then usecase returns same error ResultBundle`() =
        runTest {
            // Given
            val expected =
                ResultBundle<List<Product>>(
                    data = null,
                    error = ErrorResult.NetworkError,
                )

            coEvery { repository.getProducts() } returns expected

            // When
            val result = useCase(GetProductListRequest())

            // Then
            result shouldBe expected
            coVerify(exactly = 1) { repository.getProducts() }
        }
}
