package com.root.mssm.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.root.mssm.List.List.myaddresslist.AddressData;
import com.root.mssm.List.List.privacylist.Getallpage;
import com.root.mssm.R;
import com.root.mssm.databinding.RowMyaddressLayoutBinding;
import com.root.mssm.databinding.RowPrivayListBinding;
import com.root.mssm.global.Global;

import java.util.ArrayList;
import java.util.List;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.MyViewHolder> {
    Context context;
    List<Getallpage> categories = new ArrayList<>();

    public HelpAdapter(List<Getallpage> categories) {
        this.categories = categories;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MyViewHolder(RowPrivayListBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.name.setText(categories.get(position).getTitle());
        holder.binding.mainview.setOnClickListener(v -> {
           NavController navController= Navigation.findNavController(v);
            Bundle bundle = new Bundle();
            bundle.putString("name", categories.get(position).getTitle());
            bundle.putString("content", categories.get(position).getText());
            navController.navigate(R.id.privacyDetailFragment,bundle);
        });


    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowPrivayListBinding binding;

        public MyViewHolder(@NonNull RowPrivayListBinding itemView) {
            super(itemView.getRoot());
            this.binding= itemView;
        }
    }
}