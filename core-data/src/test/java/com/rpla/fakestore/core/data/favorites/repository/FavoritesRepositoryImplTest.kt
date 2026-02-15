package com.rpla.fakestore.core.data.favorites.repository

import com.rpla.fakestore.core.database.dao.FavoriteProductDao
import com.rpla.fakestore.core.database.entity.FavoriteProductEntity
import com.rpla.fakestore.core.domain.entity.ErrorResult
import com.rpla.fakestore.core.model.Product
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesRepositoryImplTest {
    private val dispatcher = StandardTestDispatcher()

    private lateinit var dao: FakeFavoriteProductDao
    private lateinit var repo: FavoritesRepositoryImpl

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        dao = FakeFavoriteProductDao()
        repo = FavoritesRepositoryImpl(dao)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `observeFavoriteIds maps List to Set (deduplicates)`() =
        runTest(dispatcher) {
            dao.idsFlow.value = listOf(1, 1, 2, 3, 3)

            val ids = repo.observeFavoriteIds().first()

            ids.shouldContainExactlyInAnyOrder(setOf(1, 2, 3))
        }

    @Test
    fun `observeFavorites maps entities to domain products (mapper coverage)`() =
        runTest(dispatcher) {
            dao.favoritesFlow.value =
                listOf(
                    FavoriteProductEntity(
                        id = 7,
                        title = "Title",
                        price = 10.5,
                        description = "Desc",
                        category = "Cat",
                        imageUrl = "img",
                    ),
                )

            val products = repo.observeFavorites().first()

            products.size shouldBe 1
            products[0].id shouldBe 7
            products[0].title shouldBe "Title"
            products[0].price shouldBe 10.5
            products[0].description shouldBe "Desc"
            products[0].category shouldBe "Cat"
            products[0].imageUrl shouldBe "img"
        }

    @Test
    fun `addFavorite returns ResultBundle data=Unit on success (adapter coverage)`() =
        runTest(dispatcher) {
            val result =
                repo.addFavorite(
                    Product(
                        id = 1,
                        title = "A",
                        price = 1.0,
                        description = "D",
                        category = "C",
                        imageUrl = "img",
                    ),
                )

            result.data shouldBe Unit
            result.error shouldBe null
            dao.upsertCalls shouldBe 1
        }

    @Test
    fun `removeFavorite returns ResultBundle data=Unit on success (adapter coverage)`() =
        runTest(dispatcher) {
            val result = repo.removeFavorite(10)

            result.data shouldBe Unit
            result.error shouldBe null
            dao.deleteCalls shouldBe 1
            dao.lastDeletedId shouldBe 10
        }

    @Test
    fun `toggleFavorite returns GenericError when dao throws (adapter error mapping)`() =
        runTest(dispatcher) {
            dao.throwOnToggle = RuntimeException("db fail")

            val result =
                repo.toggleFavorite(
                    Product(
                        id = 3,
                        title = "A",
                        price = 1.0,
                        description = "D",
                        category = "C",
                        imageUrl = "img",
                    ),
                )

            result.data shouldBe null
            result.error shouldBe ErrorResult.GenericError
            dao.toggleCalls shouldBe 1
        }

    /**
     * Fake DAO unit-test friendly:
     * - exposes Flow state
     * - counts calls
     * - supports forcing failures to test ResultBundleAdapter
     */
    private class FakeFavoriteProductDao : FavoriteProductDao {
        val favoritesFlow = MutableStateFlow<List<FavoriteProductEntity>>(emptyList())
        val idsFlow = MutableStateFlow<List<Int>>(emptyList())

        var throwOnUpsert: Throwable? = null
        var throwOnDelete: Throwable? = null
        var throwOnToggle: Throwable? = null

        var upsertCalls: Int = 0
        var deleteCalls: Int = 0
        var toggleCalls: Int = 0
        var lastDeletedId: Int? = null

        override fun observeFavorites(): Flow<List<FavoriteProductEntity>> = favoritesFlow

        override fun observeFavoriteIds(): Flow<List<Int>> = idsFlow

        override suspend fun upsertFavorite(entity: FavoriteProductEntity) {
            upsertCalls++
            throwOnUpsert?.let { throw it }

            // Keep flows consistent (basic fake behavior)
            val current = favoritesFlow.value.toMutableList()
            val idx = current.indexOfFirst { it.id == entity.id }
            if (idx >= 0) current[idx] = entity else current.add(entity)
            favoritesFlow.value = current.sortedBy { it.id }

            idsFlow.value = favoritesFlow.value.map { it.id }
        }

        override suspend fun deleteFavoriteById(productId: Int) {
            deleteCalls++
            lastDeletedId = productId
            throwOnDelete?.let { throw it }

            favoritesFlow.value = favoritesFlow.value.filterNot { it.id == productId }
            idsFlow.value = favoritesFlow.value.map { it.id }
        }

        override suspend fun exists(productId: Int): Boolean = favoritesFlow.value.any { it.id == productId }

        override suspend fun toggleFavorite(entity: FavoriteProductEntity) {
            toggleCalls++
            throwOnToggle?.let { throw it }

            // Use same semantics as real DAO transaction
            if (exists(entity.id)) deleteFavoriteById(entity.id) else upsertFavorite(entity)
        }
    }
}
