package com.root.mssm.ui.productdetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.root.mssm.Adapter.AdapterColor;
import com.root.mssm.Adapter.AdapterProductGallery;
import com.root.mssm.Adapter.AdapterSize;
import com.root.mssm.Interface.SelectColor;
import com.root.mssm.Interface.SelectSize;
import com.root.mssm.List.List.cart.CartList;
import com.root.mssm.List.List.cartcount.CartCountList;
import com.root.mssm.List.List.productdetail.ProductDetailList;
import com.root.mssm.List.List.productdetail.Productdetail;
import com.root.mssm.List.List.productdetail.Productgallery;
import com.root.mssm.R;
import com.root.mssm.databinding.ProductDetailFragmentBinding;
import com.root.mssm.global.Global;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ProductDetailFragment extends Fragment {

    private ProductDetailViewModel mViewModel;
    private ProductDetailFragmentBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;
    String ProductId="" ,SIZE="", COLOR="";
    SelectSize selectSize;
    List<Productdetail>  productdetails;
    SelectColor selectColor;

    public static ProductDetailFragment newInstance() {
        return new ProductDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ProductDetailFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog= new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait...");
        navController = Navigation.findNavController(view);
        chinniInterface= Config.getClient().create(ChinniInterface.class);
        sessionManage= new SessionManage(getActivity());
        cookies= new Cookies();
        if(getArguments()!= null){
            ProductId = getArguments().getString("id");
        }
        FragmentAction();
        GetContent();
        getCartCount();
    }

    void FragmentAction(){
        binding.topPanel.back.setOnClickListener(v -> {navController.popBackStack();});

        binding.allDetail.setOnClickListener(v -> {
            Bundle bundle= new Bundle();
            bundle.putString("id", String.valueOf(productdetails.get(0).getId()));
            navController.navigate(R.id.specificationFragment, bundle);
        });

        binding.addqtyLayout.setOnClickListener(v -> {
            binding.AddBtn.setVisibility(View.GONE);
            binding.plusMinBtn.setVisibility(View.VISIBLE);
        });

        binding.plusBtn.setOnClickListener(v -> {
            int qty= Integer.parseInt(binding.qty.getText().toString());
            qty++;
            binding.qty.setText(String.valueOf(qty));
        });

        binding.minBtn.setOnClickListener(v -> {
            int qty= Integer.parseInt(binding.qty.getText().toString());
            qty--;
            if(qty==0){
                binding.AddBtn.setVisibility(View.VISIBLE);
                binding.plusMinBtn.setVisibility(View.GONE);
            }else {
                qty--;
                binding.qty.setText(String.valueOf(qty));
            }
        });

        binding.addToCart.setOnClickListener(v -> {
            if(!sessionManage.isLoggedIn()){
                navController.popBackStack();
                navController.navigate(R.id.loginFragment);
            }else {
                SetCart();
            }

        });

        binding.goToCart.setOnClickListener(v -> {
            navController.navigate(R.id.cartFragment);
        });

        binding.topPanel.cart.setOnClickListener(v -> {
            navController.navigate(R.id.cartFragment);
        });

        binding.Change.setOnClickListener(v -> navController.navigate(R.id.selectAddressFragment));
    }

    void SetCart(){
        if(productdetails.get(0).getSize() != null && SIZE.isEmpty()){
            Snackbar.make(binding.getRoot(),"Select Size ",5000).show();
            return;
        }
        if(productdetails.get(0).getColor() != null && COLOR.isEmpty()){
            Snackbar.make(binding.getRoot(),"Select Color ",5000).show();
            return;
        }

        JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("user_id",sessionManage.getUserid());
        jsonObject.addProperty("product_id",ProductId);
        jsonObject.addProperty("product_qty","1");
        jsonObject.addProperty("size",SIZE);
        jsonObject.addProperty("color",COLOR);

        dialog.show();
        chinniInterface.CART_LIST_CALL(jsonObject).enqueue(new Callback<CartList>() {
            @Override
            public void onResponse(Call<CartList> call, Response<CartList> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        binding.addToCart.setVisibility(View.GONE);
                        binding.goToCart.setVisibility(View.VISIBLE);
                        getCartCount();
                        dialog.cancel();
                    }else {
                        dialog.cancel();
                        Snackbar.make(binding.getRoot(),response.body().getStatus(),5000).show();
                    }
                }else {
                    dialog.cancel();
                    Snackbar.make(binding.getRoot(),response.message(),5000).show();

                }
            }

            @Override
            public void onFailure(Call<CartList> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }

    void GetContent(){
        dialog.show();
        chinniInterface.PRODUCTDETAIL_CALL(ProductId,sessionManage.getUserid()).enqueue(new Callback<ProductDetailList>() {
            @Override
            public void onResponse(Call<ProductDetailList> call, Response<ProductDetailList> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        ((Global)getActivity().getApplicationContext()).setProductdetail(response.body());
                        productdetails = response.body().getProductdetail();
                        response.body().getProductgallery().add(new Productgallery(productdetails.get(0).getPhoto()));
                        binding.galley.setAdapter(new AdapterProductGallery(response.body().getProductgallery()));
                        SetFragmentData(response.body().getProductdetail());
                        dialog.cancel();
                    }else {
                        dialog.cancel();
                        Snackbar.make(binding.getRoot(),response.body().getStatus(),5000).show();
                    }
                }else {
                    dialog.cancel();
                    Snackbar.make(binding.getRoot(),response.message(),5000).show();

                }
            }

            @Override
            public void onFailure(Call<ProductDetailList> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }

    void SetFragmentData(List<Productdetail> productdetail){

        if(Boolean.valueOf(productdetail.get(0).getCartcheck())){
            binding.goToCart.setVisibility(View.VISIBLE);
            binding.addToCart.setVisibility(View.GONE);
        }

        binding.topPanel.title.setText(productdetail.get(0).getName());

        binding.producttitle.setText(productdetail.get(0).getName());

        binding.actualprice.setText(getResources().getString(R.string.Rs)+productdetail.get(0).getPprice());

        binding.actualprice.setPaintFlags(binding.actualprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        binding.price.setText(getResources().getString(R.string.Rs)+productdetail.get(0).getCprice());

        if(productdetail.get(0).getDescription()!= null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.descrition.setText(Html.fromHtml(productdetail.get(0).getDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                binding.descrition.setText(Html.fromHtml(productdetail.get(0).getDescription()));
            }
        }


        Log.e(TAG, "SetFragmentData: "+ productdetail.get(0).getStock() );
        if(productdetail.get(0).getStock()!=null && !productdetail.get(0).getStock().equals("0")){binding.stock.setText("In Stock "+productdetail.get(0).getStock());}else {binding.stock.setText("Out of Stock");}


        selectSize= s -> {
          SIZE = s;
        };

        selectColor= s -> {
            COLOR = s;
        };

        if(productdetail.get(0).getSize()!= null){
            binding.sizerecycle.setVisibility(View.VISIBLE);
            binding.sizeTitle.setVisibility(View.VISIBLE);
            binding.sizerecycle.setAdapter(new AdapterSize(productdetail.get(0).getSize(),selectSize));
        }else {
            binding.line1.setVisibility(View.GONE);
            binding.sizerecycle.setVisibility(View.GONE);
            binding.sizeTitle.setVisibility(View.GONE);
        }

        if(productdetail.get(0).getColor()!= null){
            binding.colorrecycle.setVisibility(View.VISIBLE);
            binding.colorTitle.setVisibility(View.VISIBLE);
            binding.colorrecycle.setAdapter(new AdapterColor(productdetail.get(0).getColor(),selectColor));
        }else {
            binding.line3.setVisibility(View.GONE);
            binding.colorrecycle.setVisibility(View.GONE);
            binding.colorTitle.setVisibility(View.GONE);
        }

        binding.deliveryTime.setText(productdetail.get(0).getShip());
        binding.replacementTime.setText(productdetail.get(0).getPolicy());
        binding.cashOnDelivery.setText("Cash on Delivery Available");

        binding.deliverAddress.setText(sessionManage.getUserDetails().get(SessionManage.ADDRESS));

        binding.deliveryCharge.setText("Shipping " + getResources().getString(R.string.Rs)+productdetail.get(0).getSshipingamount());



    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view).setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view).setVisibility(View.GONE);
    }

    void getCartCount(){
        dialog.show();
        chinniInterface.COUNT_LIST_CALL(sessionManage.getUserid()).enqueue(new Callback<CartCountList>() {
            @Override
            public void onResponse(Call<CartCountList> call, Response<CartCountList> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        binding.topPanel.count.setText(String.valueOf(response.body().getCartcount()));
                        dialog.cancel();
                    }else {
                        dialog.cancel();
                        Snackbar.make(binding.getRoot(),response.body().getStatus(),5000).show();
                    }
                }else {
                    dialog.cancel();
                    Snackbar.make(binding.getRoot(),response.message(),5000).show();

                }
            }

            @Override
            public void onFailure(Call<CartCountList> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }
}