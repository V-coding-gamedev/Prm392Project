package com.example.signuploginfirebase.Cart;

import android.os.Bundle;
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

import com.example.signuploginfirebase.R;

import java.util.List;

public class CardActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    CartAdapter cartAdapter;
    Button bt3;

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
        itemCarts.add(new ItemCart(R.drawable.ic_launcher_background, "Sữa rửa mặt cao cấp", "10"));
        itemCarts.add(new ItemCart(R.drawable.ic_launcher_background, "Túi sách mặt cao cấp", "15"));
        itemCarts.add(new ItemCart(R.drawable.ic_launcher_background, "Bát ăn cơm cao cấp", "11"));
        itemCarts.add(new ItemCart(R.drawable.ic_launcher_background, "Bát ăn cơm cao cấp", "11"));
        itemCarts.add(new ItemCart(R.drawable.ic_launcher_background, "Bát ăn cơm cao cấp", "11"));
        itemCarts.add(new ItemCart(R.drawable.ic_launcher_background, "Bát ăn cơm cao cấp", "11"));
        cartAdapter = new CartAdapter(itemCarts);
        recyclerView.setAdapter(cartAdapter);

        bt3 = findViewById(R.id.thanhtoan_button);

    }
}


class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    //các data tương ứng bên view
    List<ItemCart> listItemCarts;
    public CartAdapter(List<ItemCart> data) {
        listItemCarts = data;
    }

    //1 class để ref đến các item trong view xml
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView description;
        public TextView numberOfItem;

        public ImageView imageItem;


        public MyViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.descriptionItem);
            numberOfItem = itemView.findViewById(R.id.numbItem);
            imageItem = itemView.findViewById(R.id.imageCartItem);
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
        holder.imageItem.setImageResource(listItemCarts.get(position).image);
    }

    @Override
    public int getItemCount() {
        return listItemCarts.size();
    }
}