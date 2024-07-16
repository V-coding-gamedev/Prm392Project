package com.example.signuploginfirebase;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.signuploginfirebase.Models.Order;

import java.util.Calendar;
import java.util.List;

public class OrderListActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private TableLayout tableLayout;
    private EditText startDateEditText ,endDateEditText;

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
        ImageButton back = findViewById(R.id.left_arrowButtonOrderList);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderListActivity.this, HomeFragment.class);
                startActivity(intent);
            }
        });
        Button createButton = findViewById(R.id.createOrderButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderListActivity.this, HomeFragment.class);
                startActivity(intent);
            }
        });
        Button filterButton = findViewById(R.id.searchButton);
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(startDateEditText);
            }
        });

        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(endDateEditText);
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterOrdersByDate();
            }
        });
        dbHelper = new DBHelper(this);
        tableLayout = findViewById(R.id.main_table);
        loadOrderList();
    }

    private void loadOrderList() {
        List<Order> orderList = dbHelper.getAllOrder();

        // Clear all rows except the header
        int childCount = tableLayout.getChildCount();
        if (childCount > 1) {
            tableLayout.removeViews(1, childCount - 1);
        }

        for (Order order : orderList) {
            TableRow tableRow = new TableRow(this);
            tableRow.setTag(order.getOrder_id()); // Set order ID as tag
            tableRow.setClickable(true);
            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int orderId = (int) v.getTag(); // Get order ID from tag
                    showBookingDetail(orderId);
                }
            });

            TextView sttTextView = new TextView(this);
            sttTextView.setText(String.valueOf(order.getOrder_id()));
            sttTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
            sttTextView.setGravity(Gravity.CENTER);
            tableRow.addView(sttTextView);

            TextView startDateTextView = new TextView(this);
            startDateTextView.setText(order.getStartDate());
            startDateTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.6f));
            startDateTextView.setGravity(Gravity.CENTER);
            tableRow.addView(startDateTextView);

            TextView endDateTextView = new TextView(this);
            endDateTextView.setText(order.getEndDate());
            endDateTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.4f));
            endDateTextView.setGravity(Gravity.CENTER);
            tableRow.addView(endDateTextView);

            TextView totalAmountTextView = new TextView(this);
            totalAmountTextView.setText(String.valueOf(order.getTotalAmount()));
            totalAmountTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
            totalAmountTextView.setGravity(Gravity.CENTER);
            tableRow.addView(totalAmountTextView);

            TextView addressTextView = new TextView(this);
            addressTextView.setText(String.valueOf(order.getShipAddress()));
            addressTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
            addressTextView.setGravity(Gravity.CENTER);
            tableRow.addView(addressTextView);

            TextView statusTextView = new TextView(this);
            statusTextView.setText(String.valueOf(order.getStatus()));
            statusTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
            statusTextView.setGravity(Gravity.CENTER);
            statusTextView.setTag(order.getOrder_id()); // Store order ID in the tag
            if (statusTextView.getText().toString().equalsIgnoreCase("Conplete")) {
                statusTextView.setTextColor(Color.RED);
            }
            statusTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStatusClick(v);
                }
            });
            tableRow.addView(statusTextView);

            tableLayout.addView(tableRow);
        }
    }
    private void filterOrdersByDate() {

        String startDateStr = startDateEditText.getText().toString();
        String endDateStr = endDateEditText.getText().toString();

        List<Order> filteredOrderList = dbHelper.getOrdersByDateRange(startDateStr, endDateStr);

        // Clear all rows except the header
        int childCount = tableLayout.getChildCount();
        if (childCount > 1) {
            tableLayout.removeViews(1, childCount - 1);
        }

        for (Order order : filteredOrderList) {
            TableRow tableRow = new TableRow(this);
            tableRow.setTag(order.getOrder_id()); // Set order ID as tag
            tableRow.setClickable(true);
            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int orderId = (int) v.getTag(); // Get order ID from tag
                    showBookingDetail(orderId);
                }
            });

            TextView sttTextView = new TextView(this);
            sttTextView.setText(String.valueOf(order.getOrder_id()));
            sttTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
            sttTextView.setGravity(Gravity.CENTER);
            tableRow.addView(sttTextView);

            TextView startDateTextView = new TextView(this);
            startDateTextView.setText(order.getStartDate());
            startDateTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.6f));
            startDateTextView.setGravity(Gravity.CENTER);
            tableRow.addView(startDateTextView);

            TextView endDateTextView = new TextView(this);
            endDateTextView.setText(order.getEndDate());
            endDateTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.4f));
            endDateTextView.setGravity(Gravity.CENTER);
            tableRow.addView(endDateTextView);

            TextView totalAmountTextView = new TextView(this);
            totalAmountTextView.setText(String.valueOf(order.getTotalAmount()));
            totalAmountTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
            totalAmountTextView.setGravity(Gravity.CENTER);
            tableRow.addView(totalAmountTextView);

            TextView addressTextView = new TextView(this);
            addressTextView.setText(String.valueOf(order.getShipAddress()));
            addressTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
            addressTextView.setGravity(Gravity.CENTER);
            tableRow.addView(addressTextView);

            TextView statusTextView = new TextView(this);
            statusTextView.setText(String.valueOf(order.getStatus()));
            statusTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
            statusTextView.setGravity(Gravity.CENTER);
            statusTextView.setTag(order.getOrder_id()); // Store order ID in the tag
            if (statusTextView.getText().toString().equalsIgnoreCase("Conplete")) {
                statusTextView.setTextColor(Color.RED);
            }
            statusTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStatusClick(v);
                }
            });
            tableRow.addView(statusTextView);

            tableLayout.addView(tableRow);
        }
    }
    private void showDatePickerDialog(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                editText.setText(String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth));
                editText.setText(String.format("%02d-%02d-%04d", dayOfMonth, monthOfYear + 1, year));

            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void showBookingDetail(int orderId) {
        Intent intent = new Intent(OrderListActivity.this, OrderDetail1.class);
        intent.putExtra("order_id", orderId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrderList();
    }

    public void onStatusClick(View view) {
        TextView textView = (TextView) view;
        String status = textView.getText().toString();
        int orderId = (int) textView.getTag();

        if ("Processing".equals(status)) {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận hủy đơn")
                    .setMessage("Bạn có chắc chắn muốn hủy đơn hàng này không?")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            textView.setText("Conplete");
                            textView.setTextColor(Color.RED);                            // Thực hiện hành động hủy đơn hàng tại đây
                            dbHelper.updateOrderStatus(orderId, "Conplete");
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