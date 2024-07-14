package com.example.signuploginfirebase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class MyAccountFragment extends Fragment {

    DBHelper DB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DB = new DBHelper(getContext());

        View view = inflater.inflate(R.layout.fragment_my_accout, container, false);

        Button saveBtn = view.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newUsername = view.findViewById(R.id.registeredUsername);
                EditText registeredEmail = view.findViewById(R.id.registeredEmail);
                EditText newPhone = view.findViewById(R.id.registeredPhoneNo);
                EditText newAddress = view.findViewById(R.id.registeredAddress);

                saveChangedData(newUsername, registeredEmail, newPhone, newAddress);
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    public void updateTextView(String username, String email, String phone, String address){
        EditText retrievedUsername = getView().findViewById(R.id.registeredUsername);
        retrievedUsername.setText(username);

        EditText retrievedEmail = getView().findViewById(R.id.registeredEmail);
        retrievedEmail.setText(email);

        EditText retrievedPhone = getView().findViewById(R.id.registeredPhoneNo);
        retrievedPhone.setText(phone);

        EditText retrievedAddress = getView().findViewById(R.id.registeredAddress);
        retrievedAddress.setText(address);
    }

    public void saveChangedData(EditText newUsername, EditText registeredEmail, EditText newPhone, EditText newAddress ){
        String _newUsername = newUsername.getText().toString();
        String _registeredEmail = registeredEmail.getText().toString();
        String _newPhone = newPhone.getText().toString();
        String _newAddress = newAddress.getText().toString();

        Boolean checkupdatedata = DB.updateuserdata(_newUsername, _registeredEmail, _newPhone, _newAddress);
        if (checkupdatedata == true){
            Toast.makeText(getContext(), "Đã cập nhật", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Chưa cập nhật", Toast.LENGTH_SHORT).show();
        }
    }
}