package com.example.signuploginfirebase;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    DBHelper DB;
    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, signUpConfirmPassword, signUpPhone, signUpAddress;
    private Button signupButton;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        DB = new DBHelper(this);

        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signUpConfirmPassword = findViewById(R.id.signup_confirm_password);
        signUpPhone = findViewById(R.id.signup_phone);
        signUpAddress = findViewById(R.id.signup_address);

        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                String confirmPass = signUpConfirmPassword.getText().toString().trim();
                String phone = signUpPhone.getText().toString().trim();
                String address = signUpAddress.getText().toString().trim();

                boolean checkConfirmPasswordValidation = validateInputtedConfirmPassword(pass, confirmPass);
                boolean checkPhoneValidation = validateInputtedPhone(phone);

                if (_user.isEmpty()){
                    signupEmail.setError("Email cannot be empty");
                }
                else if (pass.isEmpty()){
                    signupPassword.setError("Password cannot be empty");
                }
                else if (!checkConfirmPasswordValidation){
                    Toast.makeText(SignUpActivity.this, "Confirm password does not match the enterred password", Toast.LENGTH_SHORT).show();
                }
                else if (!checkPhoneValidation){
                    Toast.makeText(SignUpActivity.this, "Phone must only contain numbers.", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.createUserWithEmailAndPassword(_user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();

                                if (user != null){
                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(SignUpActivity.this, "SignUp Successful. Please Vefify your email to log in", Toast.LENGTH_SHORT).show();

                                                Boolean checkInsertData = DB.insertuserdata(_user, _user, phone, pass, address);
                                                if (checkInsertData){
                                                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                                } else {
                                                    Toast.makeText(SignUpActivity.this, "Registered Email does not exist!", Toast.LENGTH_SHORT).show();
                                                }


                                            } else {
                                                Toast.makeText(SignUpActivity.this, "Registered Email does not exist!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(SignUpActivity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    public boolean isEmailDuplicate(String inputtedEmailAddress) {
        Cursor cursor = DB.getUserByEmailAddress(inputtedEmailAddress);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    private boolean validateInputtedConfirmPassword(String inputtedConfirmPassword, String inputtedPassword) {
        if (!inputtedConfirmPassword.equals(inputtedPassword)) {
            return false;
        }

        return true;
    }

    private boolean validateInputtedPhone(String inputtedPhone) {
        for (char c : inputtedPhone.toCharArray()){
            if (Character.isAlphabetic(c)){
                return false;
            }
        }

        return true;
    }

}