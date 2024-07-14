package com.example.signuploginfirebase;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signuploginfirebase.Models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.signuploginfirebase.Models.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


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

        DBHelper db = new DBHelper(this);
        db.insertSampleProducts();
        RecyclerView recyclerView = findViewById(R.id.productRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Số 2 là số cột trong grid


        List<Product> productList = db.getListProduct();


        // Thêm dữ liệu sản phẩm vào productList
        Log.d("Com", "complete");
        ProductAdapter adapter = new ProductAdapter(productList);
        recyclerView.setAdapter(adapter);
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


class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.name);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;

        Button addToCartButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.textProduct);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
}
