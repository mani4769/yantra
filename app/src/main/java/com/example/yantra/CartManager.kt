package com.example.yantra

data class CartItem(
    val product: Product,
    var quantity: Int = 1
)

object CartManager {
    private val cartItems = mutableListOf<CartItem>()
    private val listeners = mutableListOf<() -> Unit>()

    fun addProduct(product: Product) {
        val existing = cartItems.find { it.product.id == product.id }
        if (existing != null) {
            existing.quantity++
        } else {
            cartItems.add(CartItem(product, 1))
        }
        notifyListeners()
    }

    fun removeProduct(productId: Int) {
        cartItems.removeAll { it.product.id == productId }
        notifyListeners()
    }

    fun updateQuantity(productId: Int, quantity: Int) {
        if (quantity <= 0) {
            removeProduct(productId)
        } else {
            cartItems.find { it.product.id == productId }?.quantity = quantity
            notifyListeners()
        }
    }

    fun getCartItems(): List<CartItem> = cartItems.toList()

    fun getCartItemCount(): Int = cartItems.sumOf { it.quantity }

    fun getTotalPrice(): String {
        val total = cartItems.sumOf { item ->
            val price = item.product.price.replace("$", "").toIntOrNull() ?: 0
            price * item.quantity
        }
        return "$$total"
    }

    fun clearCart() {
        cartItems.clear()
        notifyListeners()
    }

    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: () -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it() }
    }
}
