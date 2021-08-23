package com.root.mssm.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.root.mssm.List.List.myaddresslist.AddressData;
import com.root.mssm.List.List.removeaddress.RemoveAddressList;
import com.root.mssm.R;
import com.root.mssm.databinding.RowAddressLayoutBinding;
import com.root.mssm.global.Global;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.SessionManage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterSelectAddress extends RecyclerView.Adapter<AdapterSelectAddress.MyViewHolder> {
    Context context;
    List<AddressData> categories = new ArrayList<>();
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;

    public AdapterSelectAddress(List<AddressData> categories) {
        this.categories = categories;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        dialog= new ProgressDialog(parent.getContext());
        dialog.setMessage("Deleting Address...");
        chinniInterface= Config.getClient().create(ChinniInterface.class);
        return new MyViewHolder(RowAddressLayoutBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
           holder.binding.name.setText(categories.get(position).getName());
           holder.binding.address.setText(categories.get(position).getHouse()+" " + categories.get(position).getStreet());
           holder.binding.mobile.setText(categories.get(position).getPhone());
           holder.binding.cityPincode.setText(categories.get(position).getCity()+" " +categories.get(position).getState() +" " +categories.get(position).getPincode()  );

           if(categories.get(position).getAddType().equals("0")){
               holder.binding.type.setText("HOME");
           }else {
               holder.binding.type.setText("OFFICE");
           }

           holder.binding.menu.setOnClickListener(v -> {
               PopupMenu dropDownMenu = new PopupMenu(context.getApplicationContext(), holder.binding.menu);
               dropDownMenu.getMenuInflater().inflate(R.menu.address_menu, dropDownMenu.getMenu());
               dropDownMenu.setOnMenuItemClickListener(item ->{
                   switch (item.getItemId()){
                       case R.id.edit:
                           ((Global)context.getApplicationContext()).setAddressData(categories.get(position));
                           NavController navController= Navigation.findNavController(v);
                           navController.navigate(R.id.editAddressFragment);
                           break;
                       case R.id.remove:
                            RemoveAddress(position,v);
                           break;
                   }

                   return true;
               });
               dropDownMenu.show();
           });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowAddressLayoutBinding binding;

        public MyViewHolder(@NonNull RowAddressLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding= itemView;
        }
    }

    void  RemoveAddress(int pos , View v){
        dialog.show();
        chinniInterface.REMOVE_ADDRESS_LIST_CALL(String.valueOf(categories.get(pos).getId())).enqueue(new Callback<RemoveAddressList>() {
            @Override
            public void onResponse(Call<RemoveAddressList> call, Response<RemoveAddressList> response) {
                if(response.isSuccessful()){
                    if(response.body().getSuccess()){
                        categories.remove(pos);
                        notifyDataSetChanged();
                        dialog.cancel();
                        Snackbar.make(v,response.body().getMessage(),5000).show();
                    }else {
                        dialog.cancel();
                        Snackbar.make(v,response.body().getMessage(),5000).show();
                    }
                }else {
                    dialog.cancel();
                    Snackbar.make(v,response.body().getMessage(),5000).show();

                }
            }

            @Override
            public void onFailure(Call<RemoveAddressList> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(v,t.getMessage(),5000).show();
            }
        });
    }
}