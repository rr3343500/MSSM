package com.root.mssm.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.root.mssm.List.List.walletlist.WalletData;
import com.root.mssm.R;
import com.root.mssm.databinding.RowBestProductBinding;
import com.root.mssm.databinding.RowTransactionLayoutBinding;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class AdapterWalletTransaction extends RecyclerView.Adapter<AdapterWalletTransaction.MyViewHolder> {
    Context context;
    List<WalletData> categories = new ArrayList<>();

    public AdapterWalletTransaction(List<WalletData> categories) {
        this.categories = categories;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MyViewHolder(RowTransactionLayoutBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.name.setText(categories.get(position).getDescription());
        holder.binding.amount.setText(context.getResources().getString(R.string.Rs)+categories.get(position).getNetamount());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.binding.time.setText(getDayMonthYear(categories.get(position).getDatetime().substring(0,10)));
        }
        if(categories.get(position).getPaymentType().equals("0")){
            holder.binding.piad.setText("Received By");
            holder.binding.type.setText("Credit");
            holder.binding.image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_received));
        }else {
            holder.binding.type.setText("Debited");
            holder.binding.piad.setText("Paid to");
            holder.binding.image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_debited));
        }

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowTransactionLayoutBinding binding;

        public MyViewHolder(@NonNull RowTransactionLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding= itemView;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    String getDayMonthYear(String date) {

        LocalDate currentDate = LocalDate.parse(date);

        int day = currentDate.getDayOfMonth();

        Month month = currentDate.getMonth();
        int year = currentDate.getYear();

        System.out.println("Day: " + day);
        System.out.println("Month: " + month);
        System.out.println("Year: " + year);

        return day+" "+month+" "+year;
    }

    public void updatelist(){
        notifyDataSetChanged();
    }
}
