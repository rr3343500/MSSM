package com.root.mssm.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.root.mssm.Interface.SelectStateCity;
import com.root.mssm.List.List.citystatelist.City;
import com.root.mssm.databinding.RowStateLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> {
    Context context;
    List<City> cityList = new ArrayList<>();
    SelectStateCity selectStateCity;
    Dialog dialog;

    public CityAdapter(SelectStateCity selectStateCity,List<City> cityList , Dialog dialog) {
        this.cityList = cityList;
        this.selectStateCity = selectStateCity;
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
        holder.binding.name.setText(cityList.get(position).getName());
        holder.binding.name.setOnClickListener(v -> {
            selectStateCity.onCitySelected(cityList.get(position).getName(), String.valueOf(cityList.get(position).getId()));
            dialog.cancel();
        });
        holder.binding.name.setOnClickListener(v -> {
            selectStateCity.onCitySelected(cityList.get(position).getName(), String.valueOf(cityList.get(position).getId()));
            dialog.cancel();
        });
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowStateLayoutBinding binding;

        public MyViewHolder(@NonNull RowStateLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding= itemView;
        }
    }
}