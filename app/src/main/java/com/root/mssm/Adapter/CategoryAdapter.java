package com.root.mssm.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.root.mssm.List.List.home.Supercatimage;
import com.root.mssm.R;
import com.root.mssm.service.Config;

import java.util.ArrayList;
import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>{
     Context context;
     List<Supercatimage> categories= new ArrayList<>();
    public CategoryAdapter(List<Supercatimage> categories) {
       this.categories= categories;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view= LayoutInflater.from( context ).inflate( R.layout.category_item,parent,false );
        return new MyViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context)
                .load(Config.Image_URL+ categories.get(position).getImage())
                .centerCrop()
                .override(300, 300)
                .placeholder(R.drawable.image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

        holder.name.setText(categories.get(position).getName());

        holder.view.setOnClickListener(v -> {
            NavController navController= Navigation.findNavController(v);
            Bundle bundle= new Bundle();
            bundle.putString("id", categories.get(position).getId());
            navController.navigate(R.id.categoryFragment, bundle);
        });


    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView imageView;
        MaterialCardView view;

        public MyViewHolder(@NonNull View itemView) {
            super( itemView );
            name=itemView.findViewById( R.id.category_name );
            imageView=itemView.findViewById( R.id.profile_image );
            view=itemView.findViewById( R.id.view );
        }
    }
}
