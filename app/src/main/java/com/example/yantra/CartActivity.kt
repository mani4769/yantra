package com.example.yantra

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerCart: RecyclerView
    private lateinit var emptyCart: LinearLayout
    private lateinit var cartBottom: LinearLayout
    private lateinit var txtTotal: TextView
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        recyclerCart = findViewById(R.id.recycler_cart)
        emptyCart = findViewById(R.id.empty_cart)
        cartBottom = findViewById(R.id.cart_bottom)
        txtTotal = findViewById(R.id.txt_total)

        btnBack.setOnClickListener { finish() }

        recyclerCart.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(CartManager.getCartItems()) {
            updateUI()
        }
        recyclerCart.adapter = cartAdapter

        updateUI()
    }

    private fun updateUI() {
        val items = CartManager.getCartItems()
        if (items.isEmpty()) {
            recyclerCart.visibility = View.GONE
            cartBottom.visibility = View.GONE
            emptyCart.visibility = View.VISIBLE
        } else {
            recyclerCart.visibility = View.VISIBLE
            cartBottom.visibility = View.VISIBLE
            emptyCart.visibility = View.GONE
            cartAdapter.updateItems(items)
            txtTotal.text = CartManager.getTotalPrice()
        }
    }
}
