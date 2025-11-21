package com.example.yantra

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var allProducts: List<Product>
    private lateinit var productAdapter: ProductAdapter
    private lateinit var searchContainer: LinearLayout
    private lateinit var searchInput: EditText
    private lateinit var appTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appTitle = findViewById(R.id.app_title)
        searchContainer = findViewById(R.id.search_container)
        searchInput = findViewById(R.id.search_input)
        val btnSearch = findViewById<ImageView>(R.id.btn_search)
        val btnCloseSearch = findViewById<ImageView>(R.id.btn_close_search)

        // Search functionality
        btnSearch.setOnClickListener {
            appTitle.visibility = View.GONE
            btnSearch.visibility = View.GONE
            searchContainer.visibility = View.VISIBLE
            searchInput.requestFocus()
        }

        btnCloseSearch.setOnClickListener {
            searchContainer.visibility = View.GONE
            appTitle.visibility = View.VISIBLE
            btnSearch.visibility = View.VISIBLE
            searchInput.setText("")
            productAdapter.updateProducts(allProducts)
        }

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase()
                val filtered = if (query.isEmpty()) {
                    allProducts
                } else {
                    allProducts.filter { it.name.lowercase().contains(query) }
                }
                productAdapter.updateProducts(filtered)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Banner carousel (3 images)
        val bannerRecycler = findViewById<RecyclerView>(R.id.recycler_banner)
        bannerRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val bannerAdapter = BannerAdapter(listOf(
            R.drawable.banner_honey_1,
            R.drawable.banner_honey_2,
            R.drawable.banner_honey_3
        ))
        bannerRecycler.adapter = bannerAdapter

        // Banner indicator dots
        val dot1 = findViewById<View>(R.id.dot1)
        val dot2 = findViewById<View>(R.id.dot2)
        val dot3 = findViewById<View>(R.id.dot3)
        val dots = arrayOf(dot1, dot2, dot3)

        // Update dots on scroll
        bannerRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val position = layoutManager.findFirstVisibleItemPosition()
                
                // Update dots
                dots.forEachIndexed { index, dot ->
                    dot.setBackgroundResource(
                        if (index == position) R.drawable.dot_active
                        else R.drawable.dot_inactive
                    )
                }
            }
        })

        // Categories (6 horizontal)
        val categoryRecycler = findViewById<RecyclerView>(R.id.recycler_categories)
        categoryRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val categories = listOf(
            "Honey", "Honeycomb", "Mixtures", "Supplements", "Honey Jar", "More"
        )
        val categoryAdapter = CategoryAdapter(categories)
        categoryRecycler.adapter = categoryAdapter

        // Featured products (4 products)
        val featuredRecycler = findViewById<RecyclerView>(R.id.recycler_featured)
        featuredRecycler.layoutManager = GridLayoutManager(this, 2)

        allProducts = sampleProducts()
        productAdapter = ProductAdapter(allProducts) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("name", product.name)
            intent.putExtra("price", product.price)
            intent.putExtra("description", product.description)
            intent.putExtra("imageRes", product.imageRes)
            intent.putExtra("productId", product.id)
            startActivity(intent)
        }
        featuredRecycler.adapter = productAdapter

        // Bottom navigation
        val navCart = findViewById<LinearLayout>(R.id.nav_cart)
        val navAccount = findViewById<LinearLayout>(R.id.nav_account)
        val navMore = findViewById<LinearLayout>(R.id.nav_more)

        navCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        navAccount.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }

        navMore.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun sampleProducts(): List<Product> {
        val desc = "Pure organic honey. Lorem ipsum dolor sit amet, consectetur adipiscing elit."
        return listOf(
            Product(1, "Honey Gel", "$180", desc, R.drawable.product_honey_gel),
            Product(2, "Honey Shampoo", "$35", desc, R.drawable.product_honey_shampoo),
            Product(3, "Honey Cider (1L)", "$180", desc, R.drawable.product_honey_cider),
            Product(4, "Manuka Honey", "$210", desc, R.drawable.product_manuka_honey)
        )
    }
}

