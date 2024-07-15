package com.example.signuploginfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.signuploginfirebase.Cart.CardActivity;
import com.example.signuploginfirebase.Models.Product;

public class ProductDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String product_id = intent.getStringExtra("product_id");
        DBHelper db = new DBHelper(this);
        Product product = db.getProductByID(Integer.parseInt(product_id));

        TextView name = findViewById(R.id.namePro);
        TextView description = findViewById(R.id.descriptionPro);
        TextView price = findViewById(R.id.pricePro);
        name.setText(product.getName());
        description.setText(product.getDescription());
        price.setText(String.valueOf(product.getUnitPrice()));



        Button addToCart = findViewById(R.id.addCart);
        addToCart.setOnClickListener(v -> {
            Intent intentc = new Intent("com.example.signuploginfirebase.ACTION_ADD_TO_CART");
            intentc.putExtra("product_id", product.getProduct_id());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intentc);
        });


        Button cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(v -> {
            Intent intentc = new Intent(this, CardActivity.class);
            startActivity(intentc);
        });

    }
}