package com.example.signuploginfirebase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OrderListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.orderListAndroid), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void onStatusClick(View view) {
        TextView textView = (TextView) view;
        String status = textView.getText().toString();

        if ("Đang giao".equals(status)) {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận hủy đơn")
                    .setMessage("Bạn có chắc chắn muốn hủy đơn hàng này không?")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            textView.setText("Đơn hủy");
                            textView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                            // Thực hiện hành động hủy đơn hàng tại đây
                            Toast.makeText(OrderListActivity.this, "Đơn hàng đã được hủy", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Không", null)
                    .show();
        } else {
            Toast.makeText(this, "Không thể hủy đơn hàng ở trạng thái này", Toast.LENGTH_SHORT).show();
        }
    }
}