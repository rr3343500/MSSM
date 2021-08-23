package com.root.mssm.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.root.mssm.Interface.SelectStateCity;
import com.root.mssm.List.List.citystatelist.State;
import com.root.mssm.databinding.RowStateLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class AdapterState extends RecyclerView.Adapter<AdapterState.MyViewHolder> {
    Context context;
    List<State> categories = new ArrayList<>();
    SelectStateCity selectStateCity;
    Dialog dialog;

    public AdapterState(SelectStateCity selectStateCity, List<State> categories,Dialog dialog) {
//        this.categories = categories;
        this.selectStateCity = selectStateCity;
        this.categories= categories;
        this.dialog= dialog;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MyViewHolder(RowStateLayoutBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         holder.binding.name.setText(categories.get(position).getName());
         holder.binding.name.setOnClickListener(v -> {
             selectStateCity.onStateSelected(categories.get(position).getName(), String.valueOf(categories.get(position).getId()));
             dialog.cancel();
         });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowStateLayoutBinding binding;

        public MyViewHolder(@NonNull RowStateLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding= itemView;
        }
    }
}