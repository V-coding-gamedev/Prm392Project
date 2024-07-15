package com.example.signuploginfirebase;

import android.database.Cursor;
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

public class EditProductActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private EditText editTextProductName, editTextProductDescription, editTextProductSize, editTextProductPrice, editTextUnitsInStock, editTextUnitsOnOrder, editTextCategoryId;
    private Button buttonEditProduct, buttonDeleteProduct;
    private int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        dbHelper = new DBHelper(this);

        editTextProductName = findViewById(R.id.editTextProductName);
        editTextProductDescription = findViewById(R.id.editTextProductDescription);
        editTextProductSize = findViewById(R.id.editTextProductSize);
        editTextProductPrice = findViewById(R.id.editTextProductPrice);
        editTextUnitsInStock = findViewById(R.id.editTextUnitsInStock);
        editTextUnitsOnOrder = findViewById(R.id.editTextUnitsOnOrder);
        editTextCategoryId = findViewById(R.id.editTextCategoryId);

        buttonEditProduct = findViewById(R.id.buttonEditProduct);
        buttonDeleteProduct = findViewById(R.id.buttonDeleteProduct);

        productId = getIntent().getIntExtra("product_id", -1);

        loadProductDetails(productId);

        buttonEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct();
            }
        });

        buttonDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });
    }

    private void loadProductDetails(int productId) {
        Cursor cursor = dbHelper.getProductById(productId);
        if (cursor != null && cursor.moveToFirst()) {
            editTextProductName.setText(cursor.getString(cursor.getColumnIndex("name")));
            editTextProductDescription.setText(cursor.getString(cursor.getColumnIndex("description")));
            editTextProductSize.setText(cursor.getString(cursor.getColumnIndex("size")));
            editTextProductPrice.setText(cursor.getString(cursor.getColumnIndex("unitPrice")));
            editTextUnitsInStock.setText(cursor.getString(cursor.getColumnIndex("unitsInStock")));
            editTextUnitsOnOrder.setText(cursor.getString(cursor.getColumnIndex("unitsOnOrder")));
            editTextCategoryId.setText(cursor.getString(cursor.getColumnIndex("category_id")));
        }
    }

    private void updateProduct() {
        String name = editTextProductName.getText().toString();
        String description = editTextProductDescription.getText().toString();
        String size = editTextProductSize.getText().toString();
        double price = Double.parseDouble(editTextProductPrice.getText().toString());
        int unitsInStock = Integer.parseInt(editTextUnitsInStock.getText().toString());
        int unitsOnOrder = Integer.parseInt(editTextUnitsOnOrder.getText().toString());
        int categoryId = Integer.parseInt(editTextCategoryId.getText().toString());

        int result = dbHelper.updateProduct(productId, name, description, size, price, unitsInStock, unitsOnOrder, categoryId);
        if (result > 0) {
            Toast.makeText(this, "Product Updated", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteProduct() {
        dbHelper.deleteProduct(productId);
        Toast.makeText(this, "Product Deleted", Toast.LENGTH_SHORT).show();
        finish();
    }
}
