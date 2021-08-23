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
import com.root.mssm.List.List.suggestionlist.SearchCat;
import com.root.mssm.List.List.suggestionlist.SearchChild;
import com.root.mssm.List.List.suggestionlist.SearchResult;
import com.root.mssm.List.List.suggestionlist.SearchSub;
import com.root.mssm.R;
import com.root.mssm.databinding.RowBestProductBinding;
import com.root.mssm.databinding.RowSearchHistoryBinding;
import com.root.mssm.service.Config;

import java.util.ArrayList;
import java.util.List;

public class AdapterSuggestion extends RecyclerView.Adapter<AdapterSuggestion.MyViewHolder> {
    Context context;
    List<SearchResult> categories = new ArrayList<>();
    List<SearchCat> searchCats = new ArrayList<>();
    List<SearchSub> searchSubs = new ArrayList<>();
    List<SearchChild> searchChildren = new ArrayList<>();
    int type;
    String searchtitle="";

    public AdapterSuggestion(List<SearchResult> categories, int type, List<SearchCat> searchCats, List<SearchSub> searchSubs, List<SearchChild> searchChildren, String s) {
        this.categories = categories;
        this.searchCats = searchCats;
        this.searchSubs = searchSubs;
        this.searchChildren = searchChildren;
        this.type = type;
        this.searchtitle=s;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MyViewHolder(RowSearchHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        switch (type) {
            case 0:
                Glide.with(context)
                        .load(Config.Image_URL + categories.get(position).getPhoto())
                        .fitCenter()
                        .placeholder(R.drawable.ic_search_black_24dp)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.binding.searchIcon);
                holder.binding.name.setText(categories.get(position).getName());

                holder.binding.view.setOnClickListener(v -> {
                    NavController navController = Navigation.findNavController(v);
                    Bundle bundle = new Bundle();
                    bundle.putString("proid", String.valueOf(categories.get(position).getName()));
                    bundle.putString("title", String.valueOf(categories.get(position).getName()));
                    navController.popBackStack();
                    navController.navigate(R.id.searchProductsFragment, bundle);
                });
                break;
            case 1:
                Glide.with(context)
                        .load(Config.Image_URL + searchCats.get(position).getPhoto())
                        .fitCenter()
                        .placeholder(R.drawable.ic_search_black_24dp)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.binding.searchIcon);
                holder.binding.name.setText(searchCats.get(position).getCatName());

                holder.binding.view.setOnClickListener(v -> {
                    NavController navController = Navigation.findNavController(v);
                    Bundle bundle = new Bundle();
                    bundle.putString("catid", String.valueOf(searchCats.get(position).getCatid()));
                    bundle.putString("title", String.valueOf(searchCats.get(position).getCatName()));
                    navController.popBackStack();
                    navController.navigate(R.id.searchProductsFragment, bundle);
                });
                break;
            case 2:
                Glide.with(context)
                        .load(Config.Image_URL + searchSubs.get(position).getPhoto())
                        .fitCenter()
                        .placeholder(R.drawable.ic_search_black_24dp)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.binding.searchIcon);
                holder.binding.name.setText(searchSubs.get(position).getSubName());

                holder.binding.view.setOnClickListener(v -> {
                    NavController navController = Navigation.findNavController(v);
                    Bundle bundle = new Bundle();
                    bundle.putString("subid", String.valueOf(searchSubs.get(position).getSubid()));
                    bundle.putString("title", String.valueOf(searchSubs.get(position).getSubName()));
                    navController.popBackStack();
                    navController.navigate(R.id.searchProductsFragment, bundle);
                });
                break;
            case 3:
                Glide.with(context)
                        .load(Config.Image_URL + searchChildren.get(position).getPhoto())
                        .fitCenter()
                        .placeholder(R.drawable.ic_search_black_24dp)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.binding.searchIcon);
                holder.binding.name.setText(searchChildren.get(position).getChildName());

                holder.binding.view.setOnClickListener(v -> {
                    NavController navController = Navigation.findNavController(v);
                    Bundle bundle = new Bundle();
                    bundle.putString("childid", String.valueOf(searchChildren.get(position).getChildid()));
                    bundle.putString("title", String.valueOf(searchChildren.get(position).getChildName()));
                    navController.popBackStack();
                    navController.navigate(R.id.searchProductsFragment, bundle);
                });
                break;

        }

    }

    @Override
    public int getItemCount() {
        int size;
        switch (type) {
            case 0:
               size= categories.size();
                break;
            case 1:
                size= searchCats.size();
                break;
            case 2:
                size = searchSubs.size();
                break;
            case 3:
                size = searchChildren.size();
                break;
            default:
                size = 0;
        }
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowSearchHistoryBinding binding;

        public MyViewHolder(@NonNull RowSearchHistoryBinding itemView) {
            super(itemView.getRoot());
            this.binding= itemView;
        }
    }
}
