package com.example.signuploginfirebase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class MyPasswordFragment extends Fragment {

    DBHelper DB;

    String _email = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DB = new DBHelper(getContext());

        View view = inflater.inflate(R.layout.fragment_my_password, container, false);

        Button saveBtn = view.findViewById(R.id.saveNewPasswordBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newPassword = view.findViewById(R.id.newPassword);
                EditText confirmNewPassword = view.findViewById(R.id.confirmNewPassword);

                savedNewPassword(newPassword, confirmNewPassword);

                // Toast.makeText(getContext(), "HEllo wORLD", Toast.LENGTH_SHORT).show();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void savedNewPassword(EditText newPassword, EditText confirmNewPassword) {
        String _newPassword = newPassword.getText().toString();
        String _confirmNewPassword = confirmNewPassword.getText().toString();

        // Toast.makeText(getContext(), _newPassword + " " + _confirmNewPassword, Toast.LENGTH_SHORT).show();

        boolean checkConfirmPasswordValidation = _newPassword.equals(_confirmNewPassword);
//        if (checkConfirmPasswordValidation){
//            Toast.makeText(getContext(), "match", Toast.LENGTH_SHORT).show();
//        }

        // Toast.makeText(getContext(), "email: " + _email, Toast.LENGTH_SHORT).show();

        boolean checkUpdatePassword = DB.resetUserPassword(_email,_newPassword);
        if (checkUpdatePassword && checkConfirmPasswordValidation){
            Toast.makeText(getContext(), "Đã cập nhật mật khẩu", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Chưa cập nhật mật khẩu", Toast.LENGTH_SHORT).show();
        }
    }

    public void retrieveEmail(String email){
        _email = email;

        // Toast.makeText(getContext(), "email: " + _email, Toast.LENGTH_SHORT).show();
    }
}