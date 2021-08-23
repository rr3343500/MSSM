package com.root.mssm.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.root.mssm.List.List.home.Subcategory;
import com.root.mssm.R;
import com.root.mssm.databinding.RowProductCategoryBinding;
import com.root.mssm.service.Config;

import java.util.ArrayList;
import java.util.List;

public class AdapterSubCategory extends RecyclerView.Adapter<AdapterSubCategory.MyViewHolder> {
    Context context;
    List<Subcategory> categories = new ArrayList<>();

    public AdapterSubCategory(List<Subcategory> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        context = parent.getContext();
        return new MyViewHolder(RowProductCategoryBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context)
                .load(Config.Image_URL + categories.get(position).getPhoto())
                .centerCrop()
                .override(300, 300)
                .placeholder(R.drawable.image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.image);

        holder.binding.name.setText(categories.get(position).getSubName());

        holder.binding.view.setOnClickListener(v -> {
            NavController navController= Navigation.findNavController(v);
            Bundle bundle= new Bundle();
            bundle.putString("id", String.valueOf(categories.get(position).getId()));
            navController.navigate(R.id.childFragment, bundle);
        });


    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowProductCategoryBinding binding;

        public MyViewHolder(@NonNull RowProductCategoryBinding itemView) {
            super(itemView.getRoot());
            this.binding= itemView;
        }
    }
}