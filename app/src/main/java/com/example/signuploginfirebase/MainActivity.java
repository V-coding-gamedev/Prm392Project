package com.example.signuploginfirebase;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
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
    Integer roleId;
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

        cursor = DB.getRoleIdByEmail(email);
        if (cursor.moveToFirst()){
            roleId = cursor.getInt(cursor.getColumnIndexOrThrow("role_id"));
            cursor.close();
        }

        Toast.makeText(getApplicationContext(), "Role ID: " + roleId, Toast.LENGTH_SHORT).show();

        Menu menu = navView.getMenu();
        MenuItem userManagementMenu = menu.findItem(R.id.nav_user_management);

        if (roleId == 1){
            userManagementMenu.setVisible(true);
        }
        else if (roleId == 2){
            userManagementMenu.setVisible(false);
        }
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
        }
        else if (id == R.id.nav_user_management){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserManagementFragment()).commit();
        }
//        else if (id == R.id.nav_settings){
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
//        }
//        else if (id == R.id.nav_share){
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareFragment()).commit();
//        }
//        else if (id == R.id.nav_about){
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
//        }
        else if (id == R.id.nav_logout){
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
}