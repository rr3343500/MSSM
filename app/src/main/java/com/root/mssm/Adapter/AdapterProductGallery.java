package com.root.mssm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.root.mssm.List.List.productdetail.Productgallery;
import com.root.mssm.R;
import com.root.mssm.databinding.RowBestProductBinding;
import com.root.mssm.databinding.RowProductCategoryBinding;
import com.root.mssm.databinding.RowProductGalleryLayoutBinding;
import com.root.mssm.service.Config;

import java.util.ArrayList;
import java.util.List;

public class AdapterProductGallery extends RecyclerView.Adapter<AdapterProductGallery.MyViewHolder> {
    Context context;
    List<Productgallery> categories = new ArrayList<>();

    public AdapterProductGallery(List<Productgallery> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MyViewHolder(RowProductGalleryLayoutBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(position==0){
            Glide.with(context)
                    .load(Config.Image_URL + categories.get(position).getPhoto())
                    .fitCenter()
                    .placeholder(R.drawable.image_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.binding.image);
        }else {
            Glide.with(context)
                    .load(Config.GALLERY + categories.get(position).getPhoto())
                    .fitCenter()
                    .placeholder(R.drawable.image_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.binding.image);
        }

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowProductGalleryLayoutBinding binding;

        public MyViewHolder(@NonNull RowProductGalleryLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding= itemView;
        }
    }
}