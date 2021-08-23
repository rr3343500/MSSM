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
import com.root.mssm.List.List.searchresult.Product;
import com.root.mssm.R;
import com.root.mssm.databinding.RowBestProductBinding;
import com.root.mssm.databinding.RowSearchProductBinding;
import com.root.mssm.service.Config;

import java.util.ArrayList;
import java.util.List;

public class AdapterSearchProduct extends RecyclerView.Adapter<AdapterSearchProduct.MyViewHolder> {
    Context context;
    List<Product> categories = new ArrayList<>();

    public AdapterSearchProduct( List<Product> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MyViewHolder(RowSearchProductBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
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
        holder.binding.price.setText(context.getResources().getString(R.string.Rs)+categories.get(position).getCprice());
        holder.binding.actualprice.setText(context.getResources().getString(R.string.Rs)+categories.get(position).getPprice());
        holder.binding.actualprice.setPaintFlags(holder.binding.actualprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        holder.binding.rating.setText(categories.get(position).());
        double dis= discountPercentage(Double.parseDouble(categories.get(position).getPprice()),Double.parseDouble(categories.get(position).getCprice()));
        holder.binding.off.setText(String.valueOf(dis).substring(0,2)+"% off");
        holder.binding.count.setText("("+ categories.get(position).getReview()+ ")");
        holder.binding.rating.setText(categories.get(position).getRating().toString());

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
        RowSearchProductBinding binding;

        public MyViewHolder(@NonNull RowSearchProductBinding itemView) {
            super(itemView.getRoot());
            this.binding= itemView;
        }
    }

    static double discountPercentage(double M, double S) {
        // Calculating discount
        double discount = M - S;

        // Calculating discount percentage
        double disPercent = (discount / M) * 100;

        return disPercent;
    }
}