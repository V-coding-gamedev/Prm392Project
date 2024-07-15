package com.example.signuploginfirebase;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signuploginfirebase.Cart.CardActivity;
import com.example.signuploginfirebase.Models.Category;
import com.example.signuploginfirebase.Models.Product;
import com.example.signuploginfirebase.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.signuploginfirebase.Models.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements ProductAdapter.OnAddToCartClickListener {

    EditText search;
    Button searchButton;
    private BroadcastReceiver addToCartReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract product information from the intent
            int productId = intent.getIntExtra("product_id", -1);
            if (productId != -1) {
                // Assuming you have a method to find a product by ID and add it to the cart
                DBHelper db = new DBHelper(context);
                Product product = db.getProductByID(productId);
                if (product != null) {
                    onAddToCartClick(product);
                }
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(addToCartReceiver);
    }
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
        // Register the broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(addToCartReceiver,
                new IntentFilter("com.example.signuploginfirebase.ACTION_ADD_TO_CART"));
//------------------------------------------------------ Insert sample data
        DBHelper db = new DBHelper(this);
//        db.insertSampleDataIntoCategories();
//        db.getAllUsers();
//        db.insertSampleProducts();
//
//        boolean isInserted = db.insertUser(4488, "Dam");
//        if (isInserted) {
//            Log.d("DBHelper", "User inserted successfully");
//        } else {
//            Log.d("DBHelper", "Error inserting user");
//        }


        RecyclerView recyclerView = findViewById(R.id.productRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Số 2 là số cột trong grid
        List<Product> productList = db.getListProduct();
        ProductAdapter adapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(adapter);

//------------------------------------------------------ search
        search = findViewById(R.id.textSearch);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            String searchValue = search.getText().toString();
            List<Product> searchList = db.searchProductsByName(searchValue);
            ProductAdapter newAdapter = new ProductAdapter(searchList, this); // Use a new adapter instance
            recyclerView.setAdapter(newAdapter);
        });
        Button cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CardActivity.class);
            startActivity(intent);
        });
        //------------------------------------------------------ Category


        Spinner spinner = findViewById(R.id.spinerCategory);
        List<Category> liscate = db.getListCategory();
        String[] items = new String[liscate.size() + 1];
        items[0] = "All";
        for (int i = 1; i < liscate.size() + 1; i++) {
            items[i] = liscate.get(i - 1).getName();
        }
        ArrayAdapter<String> adapterCate = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapterCate);
        //------------------------------------------------------
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    List<Product> productList = db.getListProduct();
                    ProductAdapter adapter = new ProductAdapter(productList, HomeActivity.this);
                    recyclerView.setAdapter(adapter);
                    return;
                }
                String selectedItem = parent.getItemAtPosition(position).toString();
                List<Product> searchList = db.getProductByCateID(position + 1);
                ProductAdapter newAdapter = new ProductAdapter(searchList, HomeActivity.this); // Corrected the missing argument
                recyclerView.setAdapter(newAdapter);
                Toast.makeText(HomeActivity.this, "Selected: " + selectedItem + " post : " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }


    User getUserFromStore() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String pass = sharedPreferences.getString("pass", "");
        DBHelper db = new DBHelper(this);
        return db.getUserByEmailAndPasswordRE(email, pass);
    }

    String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public Order createNewOrder() {
        Order newOrder = new Order();
        // Get the current user
        User user = getUserFromStore();
        Log.d("DAM", "User: " + user.toString());
        if (user != null) {
            // Create a new Order object
            Log.d("DAM", "User Name: " + user.getUsername());
            // Save the Order object to SQLite database
            DBHelper dbHelper = new DBHelper(this);

            Order o = null;

            int uid = user.user_id;
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
                newOrder.user_id = uid; // Set the user_id to the current user's ID
                newOrder.orderDate = getTodayDate(); // Set the order date (example)
                newOrder.status = "Pending"; // Set the order status (example)
                newOrder.totalAmount = 1.0f; // Set the total amount (example)
                newOrder.startDate = getTodayDate(); // Set the start date (example)
                newOrder.endDate = getTodayDate(); // Set the end date (example)
                newOrder.shipAddress = user.getAddress(); // Set the shipping address (example)
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
        holder.productImage.setOnClickListener(v -> {
            Integer productId = product.getProduct_id(); // Assuming getProduct_id() returns Integer
            if (productId != null) {
                Intent intent = new Intent(v.getContext(), ProductDetail.class);
                intent.putExtra("product_id", String.valueOf(productId)); // Convert to String
                v.getContext().startActivity(intent);
            } else {
                // Handle the case where product ID is null
                Toast.makeText(v.getContext(), "Product ID is null", Toast.LENGTH_SHORT).show();
            }
        });
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
            productImage = itemView.findViewById(R.id.imageProduct);
            productName = itemView.findViewById(R.id.textProduct);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
}
