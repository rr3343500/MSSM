package com.root.mssm.ui.editprofile.changepassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.root.mssm.List.List.cartcount.CartCountList;
import com.root.mssm.List.List.changepassword.ChangePasswordList;
import com.root.mssm.R;
import com.root.mssm.databinding.ChangePasswordFragmentBinding;
import com.root.mssm.databinding.EditProfileFragmentBinding;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {

    private ChangePasswordViewModel mViewModel;
    private ChangePasswordFragmentBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;


    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ChangePasswordFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChangePasswordViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog= new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait...");
        navController = Navigation.findNavController(view);
        chinniInterface= Config.getClient().create(ChinniInterface.class);
        sessionManage= new SessionManage(getActivity());
        binding.topPanel.title.setText("Change Password");
        cookies= new Cookies();
        if(!sessionManage.isLoggedIn()){
            navController.popBackStack();
            navController.navigate(R.id.loginFragment);

        }else {
            SetFragmentData();
            FragmnetAction();
            getCartCount();
        }
    }

    void FragmnetAction(){
        binding.topPanel.back.setOnClickListener(v -> {navController.popBackStack();});
        binding.topPanel.cart.setOnClickListener(v -> { navController.navigate(R.id.cartFragment); });
        binding.save.setOnClickListener(v -> {
            if(Validate()){
              Updatepassword();
            }
        });

        binding.currentPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.currentPass.getText().toString().isEmpty()){
                    binding.otpLayout.setError("Empty");
                    binding.otpLayout.setErrorEnabled(true);
                }else {
                    binding.otpLayout.setError(null);
                    binding.otpLayout.setErrorEnabled(false);
                }
            }
        });
        binding.newpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.newpassword.getText().toString().isEmpty()){
                    binding.newpasswordLayout.setError("Empty");
                    binding.newpasswordLayout.setErrorEnabled(true);
                }else {
                    binding.newpasswordLayout.setError(null);
                    binding.newpasswordLayout.setErrorEnabled(false);
                }
            }
        });
        binding.retypepassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.retypepassword.getText().toString().isEmpty()){
                    binding.retypepasswordLayout.setError("Empty");
                    binding.retypepasswordLayout.setErrorEnabled(true);
                }else {
                    binding.retypepasswordLayout.setError(null);
                    binding.retypepasswordLayout.setErrorEnabled(false);
                }
            }
        });
    }

    void SetFragmentData(){
//        binding.name.setText(cookies.getFavorites(getContext()).get(0).getName());
//        binding.email.setText(cookies.getFavorites(getContext()).get(0).getEmail());
//        binding.number.setText(cookies.getFavorites(getContext()).get(0).getPhone());
//        binding.address.setText(cookies.getFavorites(getContext()).get(0).getAddress());
//        binding.walletAmount.setText(cookies.getFavorites(getContext()).get(0).getWallAmt());
    }


    Boolean Validate(){

        boolean res= true;
        if(binding.newpassword.getText().toString().isEmpty()){
            binding.newpasswordLayout.setErrorEnabled(true);
            binding.newpasswordLayout.setError("Empty");
            res= false;
        }
        if(binding.retypepassword.getText().toString().isEmpty()){
            binding.retypepasswordLayout.setErrorEnabled(true);
            binding.retypepasswordLayout.setError("Empty");
            res= false;
        }
        if(binding.currentPass.getText().toString().isEmpty()){
            binding.otpLayout.setError("Empty");
            binding.otpLayout.setErrorEnabled(true);
            res= false;
        }
        if(res){
            if(binding.retypepassword.getText().toString().equals(binding.newpassword.getText().toString())){
                binding.retypepasswordLayout.setErrorEnabled(false);
                binding.retypepasswordLayout.setError(null);
                res= true;
            }else {
                binding.retypepasswordLayout.setErrorEnabled(true);
                binding.retypepasswordLayout.setError("Password not Matched");
                res = false;
            }
        }

        return res;

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

    void Updatepassword(){
        JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("user_id",sessionManage.getUserid());
        jsonObject.addProperty("cpass",binding.currentPass.getText().toString());
        jsonObject.addProperty("newpass",binding.newpassword.getText().toString());
        jsonObject.addProperty("renewpass",binding.retypepassword.getText().toString());
        dialog.show();
        chinniInterface.CHANGE_PASSWORD_LIST_CALL(jsonObject).enqueue(new Callback<ChangePasswordList>() {
            @Override
            public void onResponse(Call<ChangePasswordList> call, Response<ChangePasswordList> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("Successfully")){
                        Snackbar.make(binding.getRoot(),response.body().getData() +"\n Please Check Your Email.",5000).show();
                        dialog.cancel();
                    }else {
                        dialog.cancel();
                        Snackbar.make(binding.getRoot(),response.body().getData(),5000).show();
                    }
                }else {
                    dialog.cancel();
                    Snackbar.make(binding.getRoot(),response.message(),5000).show();

                }
            }

            @Override
            public void onFailure(Call<ChangePasswordList> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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