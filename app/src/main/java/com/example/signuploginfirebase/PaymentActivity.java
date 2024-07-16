package com.example.signuploginfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PaymentActivity extends AppCompatActivity {

    private TextView subTotalTextView;
    private TextView discountTextView;
    private TextView shippingTextView;
    private TextView totalAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);

        subTotalTextView = findViewById(R.id.sub_total);
        discountTextView = findViewById(R.id.textView17);  // Assuming textView17 is for discount
        shippingTextView = findViewById(R.id.textView18);  // Assuming textView18 is for shipping
        totalAmountTextView = findViewById(R.id.total_amt);

        // Calculate the total amount
        calculateTotalAmount();

        findViewById(R.id.pay_btn).setOnClickListener(v -> {
            // Navigate to the thank you activity
            Intent intent = new Intent(PaymentActivity.this, HomeFragment.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void calculateTotalAmount() {
        // Assuming values are stored as text with "$ " prefix
        double subTotal = Double.parseDouble(subTotalTextView.getText().toString().replace("$ ", ""));
        double discount = Double.parseDouble(discountTextView.getText().toString().replace("$ ", ""));
        double shipping = Double.parseDouble(shippingTextView.getText().toString().replace("$ ", ""));
        double total = subTotal - discount + shipping;

        totalAmountTextView.setText("$ " + String.format("%.2f", total));
    }
}
