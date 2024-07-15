package com.example.signuploginfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.signuploginfirebase.Models.Product;

import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private ListView listViewProducts;
    private Button buttonAddProduct;
    private ArrayAdapter<String> adapter; // Đổi từ ProductAdapter sang ArrayAdapter<String>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        dbHelper = new DBHelper(this);

        listViewProducts = findViewById(R.id.listViewProducts);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);

        loadProducts();

        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Product> productList = dbHelper.getAllProducts();
                Product selectedProduct = productList.get(position); // Lấy sản phẩm tại vị trí position
                Intent intent = new Intent(ProductActivity.this, EditProductActivity.class);
                intent.putExtra("product_id", selectedProduct.getProduct_id());
                startActivity(intent);
            }
        });

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, CreateProductActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadProducts() {
        List<Product> productList = dbHelper.getAllProducts();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1); // Sử dụng ArrayAdapter mặc định
        for (Product product : productList) {
            adapter.add(product.getName()); // Thêm tên sản phẩm vào adapter
        }
        listViewProducts.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }
}
