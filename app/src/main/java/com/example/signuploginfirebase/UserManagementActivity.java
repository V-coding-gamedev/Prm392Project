package com.example.signuploginfirebase;

import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signuploginfirebase.User.UserAdapter;
import com.example.signuploginfirebase.User.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserManagementActivity extends AppCompatActivity {
    DBHelper DB;
    RecyclerView recycler_view;
    UserAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_management);

        recycler_view = findViewById(R.id.recycler_view);

        DB = new DBHelper(this);

        setRecyclerView();
    }

    private void setRecyclerView() {
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(this, getList());
        recycler_view.setAdapter(adapter);
    }

    private List<UserModel> getList() {
        Cursor res = DB.getData();

        List<UserModel> booking_list = new ArrayList<>();

        if (res.moveToFirst()){
            do {
                Integer userId = res.getInt(res.getColumnIndexOrThrow("user_id"));
                String username = res.getString(res.getColumnIndexOrThrow("username"));
                String password = res.getString(res.getColumnIndexOrThrow("password"));
                String email = res.getString(res.getColumnIndexOrThrow("email"));
                String phone = res.getString(res.getColumnIndexOrThrow("phone"));
                String address = res.getString(res.getColumnIndexOrThrow("address"));

                UserModel user = new UserModel(userId, username, password, email, phone, address);
                booking_list.add(user);
            } while (res.moveToNext());
        }

        res.close();

        return booking_list;
    }
}