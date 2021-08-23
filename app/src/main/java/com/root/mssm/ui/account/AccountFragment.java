package com.root.mssm.ui.account;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.root.mssm.List.List.cartcount.CartCountList;
import com.root.mssm.List.List.signup.Signup;
import com.root.mssm.R;
import com.root.mssm.databinding.FragmentNotificationsBinding;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog= new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait...");
        navController = Navigation.findNavController(view);
        chinniInterface= Config.getClient().create(ChinniInterface.class);
        sessionManage= new SessionManage(getActivity());
        binding.topPanel.title.setText("Account");
        cookies= new Cookies();
        if(!sessionManage.isLoggedIn()){
           navController.popBackStack();
           navController.navigate(R.id.loginFragment);

        }else {
            FragmnetAction();
            getCartCount();
            getProfile();
        }


    }

    void FragmnetAction(){
        binding.viewAllOrder.setOnClickListener(v -> { navController.navigate(R.id.myOrderFragment); });
        binding.viewmore.setOnClickListener(v -> navController.navigate(R.id.myAddressFragment));
        binding.topPanel.back.setOnClickListener(v -> {navController.popBackStack();});
        binding.topPanel.cart.setOnClickListener(v -> { navController.navigate(R.id.cartFragment); });
        binding.viewwallet.setOnClickListener(v -> { navController.navigate(R.id.walletFragment); });
        binding.logout.setOnClickListener(v -> {
            navController.popBackStack(R.id.navigation_home, true);
            sessionManage.Logout();
        });
        binding.edit.setOnClickListener(v -> navController.navigate(R.id.editProfileFragment));
    }

    void SetFragmentData(){
        if(binding!=null) {
            binding.name.setText(cookies.getFavorites(getContext()).getName());
            binding.email.setText(cookies.getFavorites(getContext()).getEmail());
            binding.number.setText(cookies.getFavorites(getContext()).getPhone());
            binding.address.setText(cookies.getFavorites(getContext()).getAddress());
//        binding.walletAmount.setText(getResources().getString(R.string.Rs)+ cookies.getFavorites(getContext()).get(0).getWallAmt());
        }
        }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.search).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.search_icon).setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view).setVisibility(View.GONE);
        getActivity().findViewById(R.id.search).setVisibility(View.GONE);
        getActivity().findViewById(R.id.search_icon).setVisibility(View.VISIBLE);
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
                        if(binding!=null){
                            binding.topPanel.count.setText(String.valueOf(response.body().getCartcount()));
                        }
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


    void getProfile(){
        chinniInterface.PROFILE_LIST_CALL(sessionManage.getUserid()).enqueue(new Callback<Signup>() {
            @Override
            public void onResponse(Call<Signup> call, Response<Signup> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        cookies.removeFavorite(getActivity());
                        cookies.addFavorite(getActivity(),response.body().getUserid());
                        dialog.cancel();
                        if (binding != null) {
                            binding.walletAmount.setText(getResources().getString(R.string.Rs)+ response.body().getUserid().getWallAmt());
                        }
                        SetFragmentData();
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
            public void onFailure(Call<Signup> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }

}