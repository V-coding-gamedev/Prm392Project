package com.example.signuploginfirebase.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signuploginfirebase.DBHelper;
import com.example.signuploginfirebase.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>  {

    DBHelper DB;
    Context context;
    List<UserModel> user_list;

    public UserAdapter(Context context, List<UserModel> user_list) {
        this.context = context;
        this.user_list = user_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);

        DB = new DBHelper(view.getContext());

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (user_list != null && user_list.size() > 0){
            UserModel model = user_list.get(position);

            holder.id_tv.setText(model.getUserId().toString());
            holder.username_tv.setText(model.getUsername());
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(context.getApplicationContext(), "Delete btn is clicked", Toast.LENGTH_SHORT).show();
                    Boolean checkDelete =  DB.deletedata(model.getUserId());

                    if (checkDelete){
                        Toast.makeText(context.getApplicationContext(), "Delete success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context.getApplicationContext(), "Delete failed", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return user_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView id_tv, username_tv, email_tv, phone_tv, address_tv;
        Button deleteBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id_tv = itemView.findViewById(R.id.id_tv);
            username_tv = itemView.findViewById(R.id.username_tv);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
