package com.example.yantra

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private var items: List<CartItem>,
    private val onQuantityChanged: () -> Unit
) : RecyclerView.Adapter<CartAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.cart_item_image)
        val name: TextView = view.findViewById(R.id.cart_item_name)
        val price: TextView = view.findViewById(R.id.cart_item_price)
        val quantity: TextView = view.findViewById(R.id.cart_item_quantity)
        val btnPlus: TextView = view.findViewById(R.id.cart_btn_plus)
        val btnMinus: TextView = view.findViewById(R.id.cart_btn_minus)
        val btnRemove: ImageView = view.findViewById(R.id.cart_btn_remove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.image.setImageResource(item.product.imageRes)
        holder.name.text = item.product.name
        holder.price.text = item.product.price
        holder.quantity.text = item.quantity.toString()

        holder.btnPlus.setOnClickListener {
            CartManager.updateQuantity(item.product.id, item.quantity + 1)
            onQuantityChanged()
        }

        holder.btnMinus.setOnClickListener {
            CartManager.updateQuantity(item.product.id, item.quantity - 1)
            onQuantityChanged()
        }

        holder.btnRemove.setOnClickListener {
            CartManager.removeProduct(item.product.id)
            onQuantityChanged()
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
