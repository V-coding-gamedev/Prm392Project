package com.example.signuploginfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.signuploginfirebase.Models.OrderDetail;

import java.util.List;

public class OrderDetail1 extends AppCompatActivity {
    private TableLayout tableLayout;
    private DBHelper dbHelper;
    private int orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.orderDetail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Retrieve orderId from Intent extras
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("order_id")) {
            orderId = intent.getIntExtra("order_id", -1); // -1 is a default value if no orderId is found
        } else {
            // Handle case where orderId is not passed properly
            finish(); // Close activity if orderId is missing
            return;
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.orderDetail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton back = findViewById(R.id.left_arrowButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetail1.this, OrderListActivity.class);
                startActivity(intent);
            }
        });
        tableLayout = findViewById(R.id.main_table);
        dbHelper = new DBHelper(this);

//        addSampleProducts();
        addSampleOrderDetail();
        addProductToTable();
    }
//    public void addSampleProducts() {
//        // Thêm sản phẩm 1
//        dbHelper.insertProduct("Sản phẩm 1", "Mô tả 1", "L", 100.0f, 10, 5, 1,2);
//
//        // Thêm sản phẩm 2
//        dbHelper.insertProduct("Sản phẩm 2", "Mô tả 2", "M", 150.0f, 8, 3, 2,1);
//
//        dbHelper.insertProduct("Sản phẩm 3", "Mô tả 3", "M", 150.0f, 8, 3, 2,3);
//    }

    public void addSampleOrderDetail() {
        // Thêm sản phẩm 1
        dbHelper.createOrderDetail(1, 2, 1, 100.0f);

        // Thêm sản phẩm 2
        dbHelper.createOrderDetail(2, 1, 2, 200.0f);
        dbHelper.createOrderDetail(3, 1, 3, 190.0f);    }

    private void addProductToTable() {
//        dbHelper.deleteAllProducts();
        List<OrderDetail> productListList = dbHelper.getAllOrderDetail(orderId);

        // Clear all rows except the header
        int childCount = tableLayout.getChildCount();
        if (childCount > 1) {
            tableLayout.removeViews(1, childCount - 1);
        }

        for (OrderDetail product1 : productListList) {
            TableRow tableRow = new TableRow(this);
            tableRow.setClickable(true);

            TextView nameTextView = new TextView(this);
            nameTextView.setText(String.valueOf(product1.getOrder_id()));
            nameTextView.setGravity(Gravity.CENTER);
            nameTextView.setPadding(8, 8, 8, 8);
            tableRow.addView(nameTextView);

            TextView quantityTextView = new TextView(this);
            quantityTextView.setText(String.valueOf(product1.getProduct_id()));
            quantityTextView.setGravity(Gravity.CENTER);
            quantityTextView.setPadding(8, 8, 8, 8);
            tableRow.addView(quantityTextView);

            TextView qualityTextView = new TextView(this);
            qualityTextView.setText(String.valueOf(product1.getQuantity()));
            qualityTextView.setGravity(Gravity.CENTER);
            qualityTextView.setPadding(8, 8, 8, 8);
            tableRow.addView(qualityTextView);

            TextView priceTextView = new TextView(this);
            priceTextView.setText(String.valueOf(product1.getUnit_price()));
            priceTextView.setGravity(Gravity.CENTER);
            priceTextView.setPadding(8, 8, 8, 8);
            tableRow.addView(priceTextView);
            tableLayout.addView(tableRow);
        }
    }
}