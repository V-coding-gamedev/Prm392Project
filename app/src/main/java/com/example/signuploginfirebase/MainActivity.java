package com.example.signuploginfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    TextView userName;
    Button logout;

    // là một lớp trong Google Sign-In API cho Android, cung cấp các phương thức để thực hiện các thao tác đăng nhập và đăng xuất tài khoản Google
    GoogleSignInClient gClient;

    // một lớp trong Google Sign-In API cho Android, cho phép bạn cấu hình các thông tin và yêu cầu cần thiết khi sử dụng tính năng đăng nhập bằng Google.
    // GoogleSignInOptions xác định các loại dữ liệu và quyền mà ứng dụng của bạn yêu cầu từ người dùng khi họ đăng nhập bằng tài khoản Google của mình.
    GoogleSignInOptions gOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        logout = findViewById(R.id.logout);
        userName = findViewById(R.id.userName);

        // Tạo một đối tượng GoogleSignInOptions với cấu hình mặc định và yêu cầu quyền truy cập email của người dùng.
        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        // Tạo một đối tượng GoogleSignInClient sử dụng cấu hình đã được xây dựng (gOptions).
        gClient = GoogleSignIn.getClient(this, gOptions);

        // Kiểm tra người dùng đã đăng nhập hay chưaa
        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (gAccount != null){
            String gName = gAccount.getDisplayName();
            userName.setText(gName);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
            }
        });
    }
}