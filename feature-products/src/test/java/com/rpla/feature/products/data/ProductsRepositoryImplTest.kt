package com.rpla.feature.products.data

import com.rpla.core.domain.entity.ErrorResult
import com.rpla.core.domain.entity.ResultBundle
import com.rpla.feature.products.data.mapper.ErrorMapper
import com.rpla.feature.products.data.remote.ProductsService
import com.rpla.feature.products.fixtures.ProductDtoFixtures.dto1
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

class ProductsRepositoryImplTest {
    private val service: ProductsService = mockk()
    private val errorMapper: ErrorMapper = mockk()

    private val repository =
        ProductsRepositoryImpl(
            productsService = service,
            errorMapper = errorMapper,
        )

    @AfterEach
    fun tearDown() {
        confirmVerified(service, errorMapper)
    }

    @Test
    fun `when service returns dto list then repository returns mapped Product list`() =
        runTest {
            val dto = listOf(dto1())
            coEvery { service.getProducts() } returns dto

            // When
            val result = repository.getProducts()

            // Then
            result.error shouldBe null
            result.data!!.size shouldBe 1

            val product = result.data!![0]
            product.id shouldBe 1
            product.title shouldBe "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops"
            product.price shouldBe 109.95
            product.description shouldBe
                "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15\") in the padded sleeve, plus plenty of room for essentials."
            product.category shouldBe "men's clothing"
            product.imageUrl shouldBe "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg"
            product.isFavorite shouldBe false

            coVerify(exactly = 1) { service.getProducts() }
            coVerify(exactly = 0) { errorMapper.mapRemoteErrorRecord<List<Nothing>>(any()) }
            coVerify(exactly = 0) { errorMapper.mapUnexpectedThrowableErrorResult<List<Nothing>>(any()) }
        }

    @Test
    fun `when service throws HttpException 404 then repository maps as ClientError`() =
        runTest {
            // Given
            coEvery { service.getProducts() } throws httpException(404)

            coEvery { errorMapper.mapRemoteErrorRecord<List<Nothing>>(any()) } returns
                ResultBundle(data = null, error = ErrorResult.ClientError)

            // When
            val result = repository.getProducts()

            // Then
            result.data shouldBe null
            result.error shouldBe ErrorResult.ClientError

            coVerify(exactly = 1) { service.getProducts() }
            coVerify(exactly = 1) { errorMapper.mapRemoteErrorRecord<List<Nothing>>(any()) }
            coVerify(exactly = 0) { errorMapper.mapUnexpectedThrowableErrorResult<List<Nothing>>(any()) }
        }

    @Test
    fun `when service throws HttpException 500 then repository maps as ServerError`() =
        runTest {
            // Given
            coEvery { service.getProducts() } throws httpException(500)

            coEvery { errorMapper.mapRemoteErrorRecord<List<Nothing>>(any()) } returns
                ResultBundle(data = null, error = ErrorResult.ServerError)

            // When
            val result = repository.getProducts()

            // Then
            result.data shouldBe null
            result.error shouldBe ErrorResult.ServerError

            coVerify(exactly = 1) { service.getProducts() }
            coVerify(exactly = 1) { errorMapper.mapRemoteErrorRecord<List<Nothing>>(any()) }
            coVerify(exactly = 0) { errorMapper.mapUnexpectedThrowableErrorResult<List<Nothing>>(any()) }
        }

    @Test
    fun `when service throws UnknownHostException then repository maps as NetworkError`() =
        runTest {
            // Given
            coEvery { service.getProducts() } throws UnknownHostException("no network")

            coEvery { errorMapper.mapRemoteErrorRecord<List<Nothing>>(any()) } returns
                ResultBundle(data = null, error = ErrorResult.NetworkError)

            // When
            val result = repository.getProducts()

            // Then
            result.data shouldBe null
            result.error shouldBe ErrorResult.NetworkError

            coVerify(exactly = 1) { service.getProducts() }
            coVerify(exactly = 1) { errorMapper.mapRemoteErrorRecord<List<Nothing>>(any()) }
            coVerify(exactly = 0) { errorMapper.mapUnexpectedThrowableErrorResult<List<Nothing>>(any()) }
        }

    private fun httpException(code: Int): HttpException {
        val mediaType = "application/json".toMediaType()
        val body = """{"error":"x"}""".toResponseBody(mediaType)
        val response: Response<Any> = Response.error(code, body)
        return HttpException(response)
    }
}
