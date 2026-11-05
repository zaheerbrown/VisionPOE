package com.example.vision

import com.example.vision.data.Product
import com.example.vision.data.Store
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class StoreTest {
    @Before fun reset() { Store.cart.clear(); Store.favourites.clear(); Store.products = listOf(Product(1,"Test","Desc","Training",10.0,"")) }
    @Test fun addingProductIncreasesCartCount() { Store.addToCart(1); Store.addToCart(1); assertEquals(2, Store.cartCount()) }
    @Test fun favouriteCanBeToggled() { assertTrue(Store.toggleFavourite(1)); assertTrue(Store.favourites.contains(1)); assertFalse(Store.toggleFavourite(1)) }
    @Test fun totalUsesQuantity() { Store.addToCart(1); Store.addToCart(1); assertEquals(20.0, Store.cartTotal(), 0.001) }
}
