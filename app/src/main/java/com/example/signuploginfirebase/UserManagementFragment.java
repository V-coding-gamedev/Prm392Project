package com.example.signuploginfirebase;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.signuploginfirebase.User.UserAdapter;
import com.example.signuploginfirebase.User.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserManagementFragment extends Fragment {
    DBHelper DB;
    RecyclerView recycler_view;
    UserAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_management, container, false);

        recycler_view = view.findViewById(R.id.recycler_view);

        DB = new DBHelper(getActivity());

        setRecyclerView();

        return view;
    }

    private void setRecyclerView() {
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserAdapter(getContext(), getList());
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