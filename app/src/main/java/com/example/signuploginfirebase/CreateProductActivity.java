package com.example.signuploginfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.signuploginfirebase.R;

public class CreateProductActivity extends AppCompatActivity {

    private EditText editTextProductName, editTextProductDescription, editTextProductSize, editTextProductPrice, editTextUnitsInStock, editTextUnitsOnOrder, editTextCategoryId;
    private Button buttonAddProduct;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        dbHelper = new DBHelper(this);

        editTextProductName = findViewById(R.id.editTextProductName);
        editTextProductDescription = findViewById(R.id.editTextProductDescription);
        editTextProductSize = findViewById(R.id.editTextProductSize);
        editTextProductPrice = findViewById(R.id.editTextProductPrice);
        editTextUnitsInStock = findViewById(R.id.editTextUnitsInStock);
        editTextUnitsOnOrder = findViewById(R.id.editTextUnitsOnOrder);
        editTextCategoryId = findViewById(R.id.editTextCategoryId);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
                Intent intent = new Intent(CreateProductActivity.this, ProductActivity.class);
                startActivity(intent);

            }
        });
    }

    private void addProduct() {
        String name = editTextProductName.getText().toString();
        String description = editTextProductDescription.getText().toString();
        String size = editTextProductSize.getText().toString();
        double price = Double.parseDouble(editTextProductPrice.getText().toString());
        int unitsInStock = Integer.parseInt(editTextUnitsInStock.getText().toString());
        int unitsOnOrder = Integer.parseInt(editTextUnitsOnOrder.getText().toString());
        int categoryId = Integer.parseInt(editTextCategoryId.getText().toString());

        boolean isInserted = dbHelper.insertProduct(name, description, size, price, unitsInStock, unitsOnOrder, categoryId);

        if (isInserted) {
            Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Product Not Added", Toast.LENGTH_SHORT).show();
        }
    }
}