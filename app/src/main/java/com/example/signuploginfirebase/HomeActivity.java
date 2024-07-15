package com.example.signuploginfirebase;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signuploginfirebase.Cart.CardActivity;
import com.example.signuploginfirebase.Models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.signuploginfirebase.Models.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements ProductAdapter.OnAddToCartClickListener {


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


//        db.insertSampleProducts();
//        boolean isInserted = db.insertUser(4488, "Dam");
//        if (isInserted) {
//            Log.d("DBHelper", "User inserted successfully");
//        } else {
//            Log.d("DBHelper", "Error inserting user");
//        }
        db.getAllUsers();

        RecyclerView recyclerView = findViewById(R.id.productRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Số 2 là số cột trong grid
        List<Product> productList = db.getListProduct();
        // Thêm dữ liệu sản phẩm vào productList
        ProductAdapter adapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(adapter);

        Button cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CardActivity.class);
            startActivity(intent);
        });
    }


    String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public Order createNewOrder() {
        Order newOrder = new Order();
        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Create a new Order object
            Log.d("DAM", "User Name: " + currentUser.getDisplayName());
            // Save the Order object to SQLite database
            DBHelper dbHelper = new DBHelper(this);

            Order o = null;
            int uid = 4488;
            Log.d("DAM", "UID: " + uid);
            try {
                o = dbHelper.getPendingOrder(uid);
            } catch (Exception e) {
                Log.d("DAM", "Lỗi DB");
            }
            //nếu có order nào đang ở status là Pending thì không tạo mới
            if (o != null) {
                Log.d("DAM", "Đã có order");
                return o;
            } else {
//                newOrder.user_id = Integer.parseInt(currentUser.getUid()); // Set the user_id to the current user's ID
                newOrder.user_id = 4488; // Set the user_id to the current user's ID
                newOrder.orderDate = getTodayDate(); // Set the order date (example)
                newOrder.status = "Pending"; // Set the order status (example)
                newOrder.totalAmount = 1.0f; // Set the total amount (example)
                newOrder.startDate = "2023-10-01"; // Set the start date (example)
                newOrder.endDate = "2023-10-05"; // Set the end date (example)
                newOrder.shipAddress = "123 Main St"; // Set the shipping address (example)
                long result = dbHelper.createOrder(newOrder);

                if (result != -1) {
                    // Order successfully created
                    System.out.println("Order created with ID: " + result);
                } else {
                    // Failed to create order
                    System.err.println("Error creating order.");
                }
            }
        } else {
            // No user is signed in
            Intent intentToLogin = new Intent(this, LoginActivity.class);
            startActivity(intentToLogin);
            return null;
        }
        return newOrder;
    }

    @Override
    public void onAddToCartClick(Product product) {
        Order order = createNewOrder();
        Log.d("ORDER", order.toString());
        DBHelper dbHelper = new DBHelper(this);
        try {
            dbHelper.createOrderDetail(order.order_id, product.getProduct_id(), 1, product.getUnitPrice());
        } catch (Exception e) {
            Log.d("DAM", "Lỗi khi thêm order detail");
        }
        Toast.makeText(this, product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
        Log.d("ADD", "add to cart");
    }

}


class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnAddToCartClickListener listener;

    public interface OnAddToCartClickListener {
        void onAddToCartClick(Product product);
    }

    public ProductAdapter(List<Product> productList, OnAddToCartClickListener listener) {
        this.productList = productList;
        this.listener = listener;
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

        holder.addToCartButton.setOnClickListener(v -> listener.onAddToCartClick(product));

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
