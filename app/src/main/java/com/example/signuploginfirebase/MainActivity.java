package com.example.signuploginfirebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signuploginfirebase.Cart.CardActivity;
import com.example.signuploginfirebase.Models.Category;
import com.example.signuploginfirebase.Models.Order;
import com.example.signuploginfirebase.Models.Product;
import com.example.signuploginfirebase.Models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ProductAdapter.OnAddToCartClickListener  {

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
    DBHelper DB;
    private DrawerLayout drawerLayout;
    TextView userName;
    Button logout;

    // là một lớp trong Google Sign-In API cho Android, cung cấp các phương thức để thực hiện các thao tác đăng nhập và đăng xuất tài khoản Google
    GoogleSignInClient gClient;

    // một lớp trong Google Sign-In API cho Android, cho phép bạn cấu hình các thông tin và yêu cầu cần thiết khi sử dụng tính năng đăng nhập bằng Google.
    // GoogleSignInOptions xác định các loại dữ liệu và quyền mà ứng dụng của bạn yêu cầu từ người dùng khi họ đăng nhập bằng tài khoản Google của mình.
    GoogleSignInOptions gOptions;
    FirebaseUser user;
    private MyAccountFragment myAccountFragment;
    private MyPasswordFragment myPasswordFragment;
    String email, username, phone, address;
    TextView navHeaderUsername, navHeaderEmail;
    NavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        DB = new DBHelper(this);

        // Tạo một đối tượng GoogleSignInOptions với cấu hình mặc định và yêu cầu quyền truy cập email của người dùng.
        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        // Tạo một đối tượng GoogleSignInClient sử dụng cấu hình đã được xây dựng (gOptions).
        gClient = GoogleSignIn.getClient(this, gOptions);

        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        username = email.substring(0, email.indexOf('@'));
        phone = "";
        address = "";

        Cursor cursor = DB.getAddressByEmail(email);
        if (cursor.moveToFirst()){
            address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            cursor.close();
        }

        cursor = DB.getPhoneByEmail(email);
        if (cursor.moveToFirst()){
            phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            cursor.close();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // navigationView.setNavigationItemSelectedListener(this);
        // navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) getApplicationContext());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        navView = findViewById(R.id.nav_view);
        View navHeader = navView.getHeaderView(0);
        navHeaderUsername = navHeader.findViewById(R.id.navHeaderUsername);
        navHeaderEmail = navHeader.findViewById(R.id.navHeaderEmail);



        ///-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Register the broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(addToCartReceiver,
                new IntentFilter("com.example.signuploginfirebase.ACTION_ADD_TO_CART"));
//------------------------------------------------------ Insert sample data
        DBHelper db = new DBHelper(this);
//        boolean success = db.insertUser("dam", "12345678", "dazsingapore48@gmail.com", "1234567890", "123 Main St", 1);
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
        EditText search = findViewById(R.id.textSearch);
        Button searchButton = findViewById(R.id.searchButton);
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
                    ProductAdapter adapter = new ProductAdapter(productList, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    return;
                }
                String selectedItem = parent.getItemAtPosition(position).toString();
                List<Product> searchList = db.getProductByCateID(position + 1);
                ProductAdapter newAdapter = new ProductAdapter(searchList, MainActivity.this); // Corrected the missing argument
                recyclerView.setAdapter(newAdapter);
                Toast.makeText(MainActivity.this, "Selected: " + selectedItem + " post : " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_home){
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_my_account){
            myAccountFragment = new MyAccountFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, myAccountFragment)
                    .commit();

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (myAccountFragment != null && myAccountFragment.getView() != null){
                        myAccountFragment.updateTextView(username, email, phone, address);
                    }
                }
            });
        } else if (id == R.id.nav_my_password){
            myPasswordFragment = new MyPasswordFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myPasswordFragment).commit();

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (myPasswordFragment != null && myPasswordFragment.getView() != null){
                        myPasswordFragment.retrieveEmail(email);
                    }
                }
            });
        } else if (id == R.id.nav_settings){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
        } else if (id == R.id.nav_share){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareFragment()).commit();
        } else if (id == R.id.nav_about){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
        } else if (id == R.id.nav_logout){
            gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    finish();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    User getUserFromStore() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String pass = sharedPreferences.getString("pass", "");
        DBHelper db = new DBHelper(this);
        User user = db.getUserByEmailAndPasswordRE(email, pass);
        return user;
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
