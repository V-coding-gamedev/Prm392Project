package com.example.signuploginfirebase;

import android.content.ContentValues;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.signuploginfirebase.Models.Order;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    Button addToCartButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addToCartButton = findViewById(R.id.addtocart);
//        addToCartButton.setOnClickListener();
    }


    String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }
    public void createNewOrder() {
    // Get the current user
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    if (currentUser != null) {
        // Create a new Order object
        Order newOrder = new Order();
        newOrder.user_id = Integer.parseInt(currentUser.getUid()); // Set the user_id to the current user's ID
        newOrder.orderDate = getTodayDate(); // Set the order date (example)
        newOrder.status = "Pending"; // Set the order status (example)
        newOrder.totalAmount = 100.0f; // Set the total amount (example)
        newOrder.startDate = "2023-10-01"; // Set the start date (example)
        newOrder.endDate = "2023-10-05"; // Set the end date (example)
        newOrder.shipAddress = "123 Main St"; // Set the shipping address (example)

        // Save the Order object to SQLite database
        DBHelper dbHelper = new DBHelper(this);
        ContentValues values = new ContentValues();
        values.put("user_id", newOrder.user_id);
        values.put("order_date", newOrder.orderDate);
        values.put("status", newOrder.status);
        values.put("total_amount", newOrder.totalAmount);
        values.put("start_date", newOrder.startDate);
        values.put("end_date", newOrder.endDate);
        values.put("ship_address", newOrder.shipAddress);

        long result = dbHelper.getWritableDatabase().insert("Orders", null, values);
        if (result != -1) {
            // Order successfully created
            System.out.println("Order created with ID: " + result);
        } else {
            // Failed to create order
            System.err.println("Error creating order.");
        }
    } else {
        // No user is signed in
        System.err.println("No user is signed in.");
    }
}
}