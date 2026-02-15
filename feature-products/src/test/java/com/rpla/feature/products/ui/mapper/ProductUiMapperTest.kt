package com.rpla.fakestore.feature.products.ui.mapper

import com.rpla.fakestore.feature.products.fixtures.ProductFixtures.product1
import com.rpla.fakestore.feature.products.fixtures.ProductFixtures.product3
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ProductUiMapperTest {
    @Test
    fun `toUiListItem maps fields and formats price with 2 decimals and euro symbol`() {
        val product = product1(isFavorite = true)

        // When
        val ui = product.toUiListItem()

        ui.id shouldBe 1
        ui.title shouldBe "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops"
        ui.category shouldBe "men's clothing"
        ui.imageUrl shouldBe "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg"
        ui.isFavorite shouldBe true
        ui.price shouldBe "109.95 €"
    }

    @Test
    fun `toUiListItem formats integer prices with two decimals`() {
        // Given
        val product = product3(isFavorite = false)

        // When
        val ui = product.toUiListItem()

        ui.price shouldBe "168.00 €"
        ui.isFavorite shouldBe false
    }
}
