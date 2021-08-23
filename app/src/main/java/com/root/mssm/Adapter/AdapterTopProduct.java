package com.root.mssm.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.root.mssm.List.List.home.Topprod;
import com.root.mssm.R;
import com.root.mssm.databinding.RowBestProductBinding;
import com.root.mssm.databinding.RowTopProductsBinding;
import com.root.mssm.service.Config;

import java.util.ArrayList;
import java.util.List;

public class AdapterTopProduct  extends RecyclerView.Adapter<AdapterTopProduct.MyViewHolder> {
    Context context;
    List<Topprod> categories = new ArrayList<>();

    public AdapterTopProduct(List<Topprod> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public AdapterTopProduct.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MyViewHolder(RowTopProductsBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context)
                .load(Config.Image_URL + categories.get(position).getPhoto())
                .fitCenter()
                .placeholder(R.drawable.image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.image);

        holder.binding.name.setText(categories.get(position).getName());
        holder.binding.price.setText(context.getResources().getString(R.string.Rs)+categories.get(position).getPprice());
        holder.binding.discountPrice.setText(context.getResources().getString(R.string.Rs)+categories.get(position).getCprice());
        holder.binding.discountPrice.setPaintFlags(holder.binding.discountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.binding.view.setOnClickListener(v -> {
            NavController navController= Navigation.findNavController(v);
            Bundle bundle= new Bundle();
            bundle.putString("id", String.valueOf(categories.get(position).getId()));
            navController.navigate(R.id.productDetailFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowTopProductsBinding binding;

        public MyViewHolder(@NonNull RowTopProductsBinding itemView) {
            super(itemView.getRoot());
            this.binding= itemView;
        }
    }
}