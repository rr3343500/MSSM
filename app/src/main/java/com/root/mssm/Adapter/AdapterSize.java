package com.root.mssm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.root.mssm.Interface.SelectSize;
import com.root.mssm.R;
import com.root.mssm.databinding.RowBestProductBinding;
import com.root.mssm.databinding.RowSizeLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class AdapterSize extends RecyclerView.Adapter<AdapterSize.MyViewHolder> {
    Context context;
    List<String> categories = new ArrayList<>();
    SelectSize selectSize;
    private static int lastClickedPosition = -1;
    private int selectedItem=-1;

    public AdapterSize(List<String> categories, SelectSize select) {
        this.categories = categories;
        this.selectSize= select;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MyViewHolder(RowSizeLayoutBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.name.setText(categories.get(position).toString());
        holder.itemView.setTag(categories.get(position));

        if (selectedItem == position) {
            holder.binding.view.setCardBackgroundColor(context.getResources().getColor(R.color.gray_text));
        }else {
            holder.binding.view.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }

        holder.binding.view.setOnClickListener(v -> {
            selectedItem = position;
            selectSize.onSelectSize(categories.get(position).toString());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowSizeLayoutBinding binding;

        public MyViewHolder(@NonNull RowSizeLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding= itemView;
        }
    }
}