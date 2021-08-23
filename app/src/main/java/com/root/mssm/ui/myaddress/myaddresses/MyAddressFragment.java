package com.root.mssm.ui.myaddress.myaddresses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.root.mssm.Adapter.AdapterAddress;
import com.root.mssm.List.List.cartcount.CartCountList;
import com.root.mssm.List.List.myaddresslist.MyAddressList;
import com.root.mssm.R;
import com.root.mssm.databinding.MyAddressFragmentBinding;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAddressFragment extends Fragment {

    private MyAddressViewModel mViewModel;
    private MyAddressFragmentBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;

    public static MyAddressFragment newInstance() {
        return new MyAddressFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MyAddressFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MyAddressViewModel.class);
        // TODO: Use the ViewModel
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
        binding.topPanel.title.setText("My Address");
        if(!sessionManage.isLoggedIn()){
            navController.navigate(R.id.loginFragment);
            navController.popBackStack();
        }

        SetFragmentData();
        FragmnetAction();
        getCartCount();
        getAddress();
        setAddressRecycle();
    }

    void FragmnetAction(){
        binding.topPanel.back.setOnClickListener(v -> {navController.popBackStack();});
        binding.topPanel.cart.setOnClickListener(v -> { navController.navigate(R.id.cartFragment); });
        binding.addressLayout.setOnClickListener(v -> {navController.navigate(R.id.addNewAddressFragment);});
    }

    void SetFragmentData(){

    }

    void setAddressRecycle(){
//        binding.recycle.setAdapter(new AdapterSelectAddress());
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


    void getAddress(){
        dialog.show();
        chinniInterface.MY_ADDRESS_LIST_CALL(sessionManage.getUserid()).enqueue(new Callback<MyAddressList>() {
            @Override
            public void onResponse(Call<MyAddressList> call, Response<MyAddressList> response) {
                if(response.isSuccessful()){
                    if(response.body().isSuccess()){
                        if(response.body().getData().isEmpty()){
                            binding.recycle.setVisibility(View.GONE);
                            binding.msg.setVisibility(View.VISIBLE);
                        }else {
                            binding.recycle.setVisibility(View.VISIBLE);
                            binding.msg.setVisibility(View.GONE);
                            binding.recycle.setAdapter(new AdapterAddress(response.body().getData()));
                        }
                        dialog.cancel();
                    }else {
                        dialog.cancel();
                        Snackbar.make(binding.getRoot(),response.body().getMessage(),5000).show();
                    }
                }else {
                    dialog.cancel();
                    Snackbar.make(binding.getRoot(),response.message(),5000).show();

                }
            }

            @Override
            public void onFailure(Call<MyAddressList> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }
}