package com.root.mssm.ui.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.root.mssm.Adapter.AdapterCart;
import com.root.mssm.Interface.CartChange;
import com.root.mssm.List.List.cartitem.CartItemList;
import com.root.mssm.List.List.cartitem.Cartdatum;
import com.root.mssm.R;
import com.root.mssm.databinding.CartFragmentBinding;
import com.root.mssm.databinding.ProductDetailFragmentBinding;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class CartFragment extends Fragment implements CartChange {

    private CartViewModel mViewModel;
    private CartFragmentBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;
    int PRICE=0, ITEM=0, DELIVERY_CHARGE=0, DISCOUNT=0,DISCOUNT_PRICE=0;

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CartFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);
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
        binding.topPanel.title.setText("MyCart");
        binding.topPanel.countview.setVisibility(View.GONE);
        binding.topPanel.cart.setVisibility(View.GONE);
        FragmentAction();
        GetContent();
    }

    void FragmentAction(){
        binding.topPanel.back.setOnClickListener(v -> {navController.popBackStack();});
        binding.shopNow.setOnClickListener(v -> { navController.navigate(R.id.navigation_home); });
        binding.placeOrder.setOnClickListener(v -> navController.navigate(R.id.orderSummaryFragment));
        binding.Change.setOnClickListener(v -> navController.navigate(R.id.selectAddressFragment));
    }

    void setCartRecycle(List<Cartdatum> cartdatum){
        binding.cartrecycle.setAdapter(new AdapterCart(cartdatum, this::onCartChange,false));
    }

    void GetContent(){
        dialog.show();
        chinniInterface.ITEM_LIST_CALL(sessionManage.getUserid()).enqueue(new Callback<CartItemList>() {
            @Override
            public void onResponse(Call<CartItemList> call, Response<CartItemList> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        if(response.body().getCartdata().isEmpty()){
                            binding.scrollView.setVisibility(View.GONE);
                            binding.bottomview.setVisibility(View.GONE);
                            binding.emptyView.setVisibility(View.VISIBLE);
                        }else {
                            binding.scrollView.setVisibility(View.VISIBLE);
                            binding.bottomview.setVisibility(View.VISIBLE);
                            binding.emptyView.setVisibility(View.GONE);
                            setCartRecycle(response.body().getCartdata());
                        }

                        setFragmentData();
                        CartAmountCalulation(response.body().getCartdata());
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
            public void onFailure(Call<CartItemList> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.search).setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view).setVisibility(View.GONE);
        getActivity().findViewById(R.id.search).setVisibility(View.GONE);
    }


    static double discountPercentage(double M, double S) {
        // Calculating discount
        double discount = M - S;

        // Calculating discount percentage
        double disPercent = (discount / M) * 100;

        return disPercent;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view).setVisibility(View.GONE);
        getActivity().findViewById(R.id.search).setVisibility(View.GONE);
        PRICE=0; ITEM=0; DELIVERY_CHARGE=0; DISCOUNT=0; DISCOUNT_PRICE=0;
    }

    void setFragmentData(){
        binding.deliverAddress.setText(sessionManage.getUserDetails().get(SessionManage.ADDRESS));

    }

    void CartAmountCalulation(List<Cartdatum> cartdatum){
        int Tempmax=0, Previous=0;
        for (int i=0; i< cartdatum.size(); i++){
            int price = Integer.parseInt(cartdatum.get(i).getProductdata().get(0).getCprice());
            int discountprice = Integer.parseInt(cartdatum.get(i).getProductdata().get(0).getPprice());
            int qty =   Integer.parseInt(cartdatum.get(i).getQuantity());
            PRICE +=   price*qty;
            DISCOUNT_PRICE +=   discountprice*qty;
            Log.e(TAG, "CartAmountCalulation: "+ price +  "       " +  PRICE );


            if(DELIVERY_CHARGE>= Integer.parseInt(cartdatum.get(i).getProductdata().get(0).getSshipingamount())){

            }else {
                DELIVERY_CHARGE= Integer.parseInt(cartdatum.get(i).getProductdata().get(0).getSshipingamount());
            }




            ITEM ++;
        }

        binding.price.setText(getResources().getString(R.string.Rs)+ PRICE);
        binding.itemCount.setText(ITEM+" Item");
        int total= PRICE+DELIVERY_CHARGE;
        binding.totalAmount.setText(getResources().getString(R.string.Rs)+total);
        binding.finalPrice.setText(getResources().getString(R.string.Rs)+total);
        binding.finalPrice.setText(getResources().getString(R.string.Rs)+total);
        binding.deliveryCharge.setText(getResources().getString(R.string.Rs)+DELIVERY_CHARGE);

        binding.discount.setText(getResources().getString(R.string.Rs)+String.valueOf(DISCOUNT_PRICE - PRICE));
    }


    @Override
    public void onCartChange(Boolean blank_status,List<Cartdatum> categories) {
       if(blank_status){
           binding.scrollView.setVisibility(View.GONE);
           binding.bottomview.setVisibility(View.GONE);
           binding.emptyView.setVisibility(View.VISIBLE);
       }else {
           binding.scrollView.setVisibility(View.VISIBLE);
           binding.bottomview.setVisibility(View.VISIBLE);
           binding.emptyView.setVisibility(View.GONE);
           PRICE = DELIVERY_CHARGE = ITEM = DISCOUNT = DISCOUNT_PRICE = 0;
           CartAmountCalulation(categories);
       }
    }


}