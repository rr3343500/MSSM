package com.root.mssm.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.root.mssm.Interface.CartChange;
import com.root.mssm.List.List.cartitem.Cartdatum;
import com.root.mssm.R;
import com.root.mssm.databinding.RowItemCartBinding;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.MyViewHolder> {
    Context context;
    List<Cartdatum> categories = new ArrayList<>();
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;
    CartChange cartChange;
    Boolean type;

    public AdapterCart(List<Cartdatum> categories ,  CartChange cartChange , boolean type) {
        this.categories = categories;
        this.cartChange = cartChange;
        this.type= type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        dialog= new ProgressDialog(context);
        dialog.setMessage("Please Wait...");
        chinniInterface= Config.getClient().create(ChinniInterface.class);
        sessionManage= new SessionManage(context);
        cookies= new Cookies();
        return new MyViewHolder(RowItemCartBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @SuppressLint({"ResourceType", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context)
                .load(Config.Image_URL + categories.get(position).getProductdata().get(0).getPhoto())
                .fitCenter()
                .placeholder(R.drawable.image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.image);

        if(type){
            holder.binding.itemAction.setVisibility(View.GONE);
            holder.binding.view.setPadding(0,20,0,20);
        }

        holder.binding.name.setText(categories.get(position).getProductdata().get(0).getName());
        holder.binding.actualprice.setText(context.getResources().getString(R.string.Rs)+categories.get(position).getProductdata().get(0).getPprice());
        holder.binding.price.setText(context.getResources().getString(R.string.Rs)+categories.get(position).getProductdata().get(0).getCprice());
        holder.binding.qty.setText("Qty:"+categories.get(position).getQuantity());
        if(categories.get(position).getSize() != null){
            holder.binding.sizeName.setText(": " +categories.get(position).getSize());
        }else {
            holder.binding.sizeName.setVisibility(View.GONE);
            holder.binding.sizeTitle.setVisibility(View.GONE);
        }


        if (categories.get(position).getColor() != null) {
            holder.binding.colorName.setBackgroundColor(Color.parseColor(categories.get(position).getColor()));
        }else {
            holder.binding.colorName.setVisibility(View.GONE);
            holder.binding.colorTitle.setVisibility(View.GONE);
        }

        double dis= discountPercentage(Double.parseDouble(categories.get(position).getProductdata().get(0).getPprice()),Double.parseDouble(categories.get(position).getProductdata().get(0).getCprice()));
        holder.binding.off.setText(String.valueOf(dis).substring(0,2)+"% off");

        holder.binding.actualprice.setPaintFlags(holder.binding.actualprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

          holder.binding.qty.setOnClickListener(v -> {
              PopupMenu dropDownMenu = new PopupMenu(context.getApplicationContext(), holder.binding.qty);
              dropDownMenu.getMenuInflater().inflate(R.menu.qty_popup_menu, dropDownMenu.getMenu());
              dropDownMenu.setOnMenuItemClickListener(item ->{
                  switch (item.getItemId()){
                      case R.id.one:
                          UpdateItemQuantity("1", categories.get(position).getProductdata().get(0).getId() ,position,v);
                          break;
                      case R.id.two:
                          UpdateItemQuantity("2", categories.get(position).getProductdata().get(0).getId() ,position,v);
                          break;
                      case R.id.three:
                          UpdateItemQuantity("3", categories.get(position).getProductdata().get(0).getId() ,position,v);
                          break;
                      case R.id.more:
                          showQuantityDialog("0", categories.get(position).getProductdata().get(0).getId() ,position,v);
                          break;
                  }

              return true;
              });
              dropDownMenu.show();
          });

          holder.binding.remove.setOnClickListener(v -> {
              Log.e(TAG, "onBindViewHolder: "+ sessionManage.getUserid() );
              Log.e(TAG, "onBindViewHolder: "+ categories.get(position).getId());
             Removefromcart(categories.get(position).getId() ,v, position);
          });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowItemCartBinding binding;

        public MyViewHolder(@NonNull RowItemCartBinding itemView) {
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

    void Removefromcart(String id, View view, int position){
          dialog.show();
          chinniInterface.REMOVE_FROM_CART(sessionManage.getUserid(),id).enqueue(new Callback<JSONObject>() {
              @Override
              public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                  if(response.isSuccessful()){
                      Snackbar.make(view,"Product Removed From Cart",5000).show();
                      categories.remove(position);
                      notifyDataSetChanged();
                      if(categories.isEmpty()){
                          cartChange.onCartChange(true,categories);
                      }else {
                          cartChange.onCartChange(false,categories);
                      }
                      dialog.cancel();
                  }else {
                      dialog.cancel();
                      Snackbar.make(view,response.message(),5000).show();

                  }
              }

              @Override
              public void onFailure(Call<JSONObject> call, Throwable t) {
                  dialog.cancel();
                  Snackbar.make(view,t.getMessage(),5000).show();
              }
          });
    }

    void UpdateItemQuantity(String qty , String product_id, int position, View view){

        Log.e(TAG, "UpdateItemQuantity: "+  product_id   + "   id "   +  categories.get(position).getId()  );

        dialog.show();
        chinniInterface.UPDATE_CART_QUANTITY(categories.get(position).getId(),qty, product_id).enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if(response.isSuccessful()){
                    categories.get(position).setQuantity(qty);
                    notifyItemChanged(position);
                    cartChange.onCartChange(false,categories);
                    dialog.cancel();
                }else {
                    dialog.cancel();
                    Snackbar.make(view,response.message(),5000).show();

                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(view,t.getMessage(),5000).show();
            }
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    public void showQuantityDialog(String qty , String product_id, int position, View view){
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.address_dialog_layout);
        TextView close = dialog1.findViewById(R.id.cencel);
        EditText address= dialog1.findViewById(R.id.EditAddress);
        TextView apply= dialog1.findViewById(R.id.apply);
        close.setOnClickListener(v -> dialog1.dismiss());

        apply.setOnClickListener(v -> {
           if(address.getText().toString().isEmpty()){
               address.setError("Enter Quantity");
           }else {
               UpdateItemQuantity(address.getText().toString(), product_id, position, view);
               dialog1.cancel();
           }
        });
        dialog1.show();

    }



}