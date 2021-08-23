package com.root.mssm.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.root.mssm.Interface.SelectColor;
import com.root.mssm.R;
import com.root.mssm.databinding.RowColorLayoutBinding;
import com.root.mssm.databinding.RowSizeLayoutBinding;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AdapterColor extends RecyclerView.Adapter<AdapterColor.MyViewHolder> {
    Context context;
    List<String> categories = new ArrayList<>();
    SelectColor selectSize;
    private static int lastClickedPosition = -1;
    private int selectedItem=-1;

    public AdapterColor(List<String> categories, SelectColor select) {
        this.categories = categories;
        this.selectSize= select;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MyViewHolder(RowColorLayoutBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.name.setBackgroundColor(Color.parseColor(categories.get(position).toString()));
        holder.itemView.setTag(categories.get(position));
        Log.e(TAG, "onBindViewHolder: "+  "" );

        if (selectedItem == position) {
            holder.binding.view.setCardBackgroundColor(context.getResources().getColor(R.color.gray_text));
        }else {
            holder.binding.view.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }

        holder.binding.view.setOnClickListener(v -> {
            selectedItem = position;
            selectSize.onSelectColor(categories.get(position).toString());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowColorLayoutBinding binding;

        public MyViewHolder(@NonNull RowColorLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding= itemView;
        }
    }
}