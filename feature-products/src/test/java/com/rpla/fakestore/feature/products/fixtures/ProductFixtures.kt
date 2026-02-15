package com.rpla.fakestore.feature.products.fixtures

import com.rpla.fakestore.core.model.Product

object ProductFixtures {
    fun product1(isFavorite: Boolean = false) =
        Product(
            id = 1,
            title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
            price = 109.95,
            description =
                "Your perfect pack for everyday use and walks in the forest. Stash your laptop" +
                    " (up to 15\") in the padded sleeve, plus plenty of room for essentials.",
            category = "men's clothing",
            imageUrl = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
            isFavorite = isFavorite,
        )

    fun product2(isFavorite: Boolean = false) =
        Product(
            id = 2,
            title = "Mens Casual Premium Slim Fit T-Shirts (Pack of 1)",
            price = 22.30,
            description =
                "Slim-fitting style, contrast raglan long sleeve, three-button henley placket." +
                    " Lightweight & soft fabric for breathable comfort.",
            category = "men's clothing",
            imageUrl = "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879_.jpg",
            isFavorite = isFavorite,
        )

    fun product3(isFavorite: Boolean = false) =
        Product(
            id = 3,
            title = "Solid Gold Petite Micropave Diamond Ring",
            price = 168.00,
            description =
                "Satisfaction guaranteed. Return or exchange within 30 days. Designed for an" +
                    " elegant minimal look with a premium finish.",
            category = "jewelery",
            imageUrl = "https://fakestoreapi.com/img/71YAIFU48IL._AC_UL640_QL65_ML3_.jpg",
            isFavorite = isFavorite,
        )

    fun product4(isFavorite: Boolean = false) =
        Product(
            id = 4,
            title = "WD 2TB Elements Portable External Hard Drive - USB 3.0",
            price = 64.00,
            description =
                "High capacity in a small enclosure. Fast data transfers with USB 3.0 " +
                    "compatibility. Ideal for backups and portable storage.",
            category = "electronics",
            imageUrl = "https://fakestoreapi.com/img/61IBBVJvSDL._AC_SY879_.jpg",
            isFavorite = isFavorite,
        )
}
