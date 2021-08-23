package com.root.mssm.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.root.mssm.List.List.home.Bestprod;
import com.root.mssm.R;
import com.root.mssm.databinding.RowBestProductBinding;
import com.root.mssm.databinding.RowLayoutMyorderBinding;

import java.util.ArrayList;
import java.util.List;

public class AdapterMyOrder extends RecyclerView.Adapter<AdapterMyOrder.MyViewHolder> {
    Context context;
    List<Bestprod> categories = new ArrayList<>();

    public AdapterMyOrder() {
//        this.categories = categories;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MyViewHolder(RowLayoutMyorderBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        Glide.with(context)
//                .load(Config.Image_URL + categories.get(position).getPhoto())
//                .fitCenter()
//                .placeholder(R.drawable.image_placeholder)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.binding.image);

        holder.binding.view.setOnClickListener(v -> {
            NavController navController= Navigation.findNavController(v);
            Bundle bundle= new Bundle();
            bundle.putString("id", "String.valueOf(categories.get(position).getId())");
            navController.navigate(R.id.orderDetailFragment, bundle);
        });

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowLayoutMyorderBinding binding;

        public MyViewHolder(@NonNull RowLayoutMyorderBinding itemView) {
            super(itemView.getRoot());
            this.binding= itemView;
        }
    }
}