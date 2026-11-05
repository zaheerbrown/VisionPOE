package com.example.vision.data

object Store {
    val favourites = linkedSetOf<Int>()
    val cart = linkedMapOf<Int, Int>()
    var products: List<Product> = emptyList()

    fun toggleFavourite(id: Int): Boolean = if (favourites.remove(id)) false else { favourites.add(id); true }
    fun addToCart(id: Int) { cart[id] = (cart[id] ?: 0) + 1 }
    fun removeFromCart(id: Int) { cart.remove(id) }
    fun cartCount() = cart.values.sum()
    fun cartTotal(): Double = cart.entries.sumOf { (id, qty) -> (products.find { it.id == id }?.price ?: 0.0) * qty }
}
