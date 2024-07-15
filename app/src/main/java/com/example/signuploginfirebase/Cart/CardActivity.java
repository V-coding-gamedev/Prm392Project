package com.example.signuploginfirebase.Cart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signuploginfirebase.DBHelper;
import com.example.signuploginfirebase.Models.Order;
import com.example.signuploginfirebase.Models.OrderDetail;
import com.example.signuploginfirebase.Models.Product;
import com.example.signuploginfirebase.R;

import java.util.List;

public class CardActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    CartAdapter cartAdapter;
    Button bt3;
    TextView numberOfItem;
    TextView tongtien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        List<ItemCart> itemCarts = new java.util.ArrayList<>();
        DBHelper db = new DBHelper(this);
        //đọc order
        List<Order> orders = db.getOrderByUserID(4488);
        float tongtiengia = 0;
        //đọc order detail từ cái order trên cái order nào đang có status là Pendding, add vào
        for (Order order : orders) {
            if (order.getStatus().equals("Pending")) {
                List<OrderDetail> orderDetails = db.getOrderDetailByOrderID(order.getOrder_id());
                for (OrderDetail orderDetail : orderDetails) {
                    Product pro = db.getProductByID(orderDetail.getProduct_id());
                    tongtiengia += pro.getUnitPrice() * orderDetail.getQuantity();
                    itemCarts.add(new ItemCart(orderDetail.getOrder_id(), pro.getProduct_id(), R.drawable.ic_launcher_background,pro.getName(), String.valueOf(orderDetail.getQuantity()), "Giá:" + String.valueOf(pro.getUnitPrice())));
                }
            }
        }
        tongtien = findViewById(R.id.tonggiatien);
        tongtien.setText(tongtiengia + "");
        Log.d("CartActivity", "itemCarts: " + itemCarts.size());
        cartAdapter = new CartAdapter(itemCarts);
        recyclerView.setAdapter(cartAdapter);
        bt3 = findViewById(R.id.thanhtoan_button);

    }
}

class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    public interface OnClickManuCartListener {
        void onUpClick(Product product);
    }

    //các data tương ứng bên view
    List<ItemCart> listItemCarts;

    public CartAdapter(List<ItemCart> data) {
        listItemCarts = data;
    }

    //1 class để ref đến các item trong view xml
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView description;
        public TextView numberOfItem;
        public TextView pricePro;

        public ImageView imageItem;

        public Button upButton;
        public Button downButton;
        public Button deleteButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.descriptionItem);
            numberOfItem = itemView.findViewById(R.id.numbItem);
            pricePro = itemView.findViewById(R.id.giaProduct);
            imageItem = itemView.findViewById(R.id.imageCartItem);
            upButton = itemView.findViewById(R.id.buttonUp);
            downButton = itemView.findViewById(R.id.buttonDown);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    //gắn view xml vào viewholder
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new MyViewHolder(view);
    }

    //bind data vào viewholder
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.description.setText(listItemCarts.get(position).descripttion);
        holder.numberOfItem.setText(listItemCarts.get(position).numberOfItem);
        holder.pricePro.setText(listItemCarts.get(position).pricePro);
        holder.imageItem.setImageResource(listItemCarts.get(position).image);
        // Update the onClickListener in onBindViewHolder
        holder.upButton.setOnClickListener(v -> {
            DBHelper db = new DBHelper(holder.itemView.getContext());
            int numberUpdate = Integer.parseInt(listItemCarts.get(position).numberOfItem) + 1;
            try {
                db.updateOrderDetail(listItemCarts.get(position).id, listItemCarts.get(position).Product_id,numberUpdate);
            } catch (Exception e) {
                Log.d("DAM", "Lỗi khi update order detail");
            }
            listItemCarts.get(position).numberOfItem = numberUpdate + "";
            notifyItemChanged(position); // Notify the adapter that the item has changed
        });

        holder.downButton.setOnClickListener(v -> {
            DBHelper db = new DBHelper(holder.itemView.getContext());
            int numberUpdate = Integer.parseInt(listItemCarts.get(position).numberOfItem) - 1;
            if (numberUpdate < 0) {
                numberUpdate = 0;
            }
            try {
                db.updateOrderDetail(listItemCarts.get(position).id, listItemCarts.get(position).Product_id, numberUpdate);
            } catch (Exception e) {
                Log.d("DAM", "Lỗi khi update order detail");
            }
            listItemCarts.get(position).numberOfItem = numberUpdate + "";
            notifyItemChanged(position); // Notify the adapter that the item has changed
        });

        holder.deleteButton.setOnClickListener(v -> {
            DBHelper db = new DBHelper(holder.itemView.getContext());
            try {
                db.deleteOrderDetail(listItemCarts.get(position).id,listItemCarts.get(position).Product_id);
            } catch (Exception e) {
                Log.d("DAM", "Lỗi khi xóa order detail");
            }
            listItemCarts.remove(position);
            notifyItemRemoved(position); // Notify the adapter that the item has been removed
        });
    }

    @Override
    public int getItemCount() {
        return listItemCarts.size();
    }
}