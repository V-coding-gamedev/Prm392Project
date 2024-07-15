package com.example.signuploginfirebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signuploginfirebase.Models.Product;

import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private RecyclerView recyclerViewProducts;
    private Button buttonAddProduct;
    private ProductAdapterAdmin adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        dbHelper = new DBHelper(this);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);

        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 2)); // Số 3 là số cột trong grid

        loadProducts();

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProductActivity.this, CreateProductActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadProducts() {
        List<Product> productList = dbHelper.getAllProducts();
        adapter = new ProductAdapterAdmin(productList, this);
        recyclerViewProducts.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }
}

class ProductAdapterAdmin extends RecyclerView.Adapter<ProductAdapterAdmin.ProductViewHolderAdmin> {

    private List<Product> productList;
    private Context context;

    public ProductAdapterAdmin(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolderAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_admin, parent, false);
        return new ProductViewHolderAdmin(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolderAdmin holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        String priceString = String.format("%.0f", product.getUnitPrice());
        holder.price.setText("Price: "+priceString + " VND");
        holder.size.setText("Size: " + product.getSize());
        String stockString = String.format("%2d", product.getUnitsInStock());
        holder.stock.setText("UnitStock: " + stockString);
        String unitOrderString = String.format("%2d", product.getUnitsOnOrder());
        holder.unitOrder.setText("UnitOnOrder: " + unitOrderString);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditProductActivity.class);
            intent.putExtra("product_id", product.getProduct_id());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolderAdmin extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView price;
        TextView size;
        TextView stock;
        TextView unitOrder;
        public ProductViewHolderAdmin(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imageProduct);
            productName = itemView.findViewById(R.id.textProduct);
            size = itemView.findViewById(R.id.textSize);
            price = itemView.findViewById(R.id.textPrice);
            stock = itemView.findViewById(R.id.textStock);
            unitOrder = itemView.findViewById(R.id.textUnitOrder);
        }
    }
}