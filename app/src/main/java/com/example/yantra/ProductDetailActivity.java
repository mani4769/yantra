package com.example.yantra;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    private int quantity = 1;
    private Product currentProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ImageView imageView = findViewById(R.id.detail_image);
        TextView nameView = findViewById(R.id.detail_name);
        TextView priceView = findViewById(R.id.detail_price);
        TextView descView = findViewById(R.id.detail_description);
        ImageView btnBack = findViewById(R.id.btn_back);
        
        // Dynamic Add/Quantity controls
        TextView btnAdd = findViewById(R.id.btn_add);
        LinearLayout quantityControls = findViewById(R.id.quantity_controls);
        TextView txtQuantity = findViewById(R.id.txt_quantity);
        TextView btnPlus = findViewById(R.id.btn_plus);
        TextView btnMinus = findViewById(R.id.btn_minus);
        TextView btnAddToCart = findViewById(R.id.btn_add_to_cart);

        if (getIntent() != null) {
            String name = getIntent().getStringExtra("name");
            String price = getIntent().getStringExtra("price");
            String desc = getIntent().getStringExtra("description");
            int imageRes = getIntent().getIntExtra("imageRes", R.drawable.ic_honey);
            int productId = getIntent().getIntExtra("productId", 0);

            currentProduct = new Product(productId, name, price, desc, imageRes);

            imageView.setImageResource(imageRes);
            nameView.setText(name);
            priceView.setText(price);
            descView.setText(desc);
        }

        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Add button click - show quantity controls
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAdd.setVisibility(View.GONE);
                quantityControls.setVisibility(View.VISIBLE);
                btnAddToCart.setVisibility(View.VISIBLE);
                quantity = 1;
                txtQuantity.setText("1");
            }
        });

        // Plus button
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                txtQuantity.setText(String.valueOf(quantity));
            }
        });

        // Minus button
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                    txtQuantity.setText(String.valueOf(quantity));
                } else {
                    // Back to Add button
                    quantityControls.setVisibility(View.GONE);
                    btnAddToCart.setVisibility(View.GONE);
                    btnAdd.setVisibility(View.VISIBLE);
                }
            }
        });

        // Add to Cart button
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < quantity; i++) {
                    CartManager.INSTANCE.addProduct(currentProduct);
                }
                Toast.makeText(ProductDetailActivity.this, 
                    "Added to cart!", Toast.LENGTH_SHORT).show();
                
                // Navigate to cart
                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }
}
