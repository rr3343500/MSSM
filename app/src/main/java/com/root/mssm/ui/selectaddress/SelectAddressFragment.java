package com.root.mssm.ui.selectaddress;

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
import com.root.mssm.Adapter.AdapterSelectAddress;
import com.root.mssm.List.List.myaddresslist.MyAddressList;
import com.root.mssm.R;
import com.root.mssm.databinding.OrderSummaryFragmentBinding;
import com.root.mssm.databinding.SelectAddressFragmentBinding;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectAddressFragment extends Fragment {

    private SelectAddressViewModel mViewModel;
    private SelectAddressFragmentBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;

    public static SelectAddressFragment newInstance() {
        return new SelectAddressFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SelectAddressFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SelectAddressViewModel.class);
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
        binding.topPanel.title.setText("Select Delivery Address");
        binding.topPanel.countview.setVisibility(View.GONE);
        binding.topPanel.cart.setVisibility(View.GONE);
        getActivity().findViewById(R.id.appbar).setVisibility(View.GONE);
        FragmentAction();
        setCartRecycle();
        getAddress();
    }

    void FragmentAction(){
        binding.topPanel.back.setOnClickListener(v -> {navController.popBackStack();});
        binding.addressLayout.setOnClickListener(v -> {navController.navigate(R.id.addNewAddressFragment);});
    }

    void setCartRecycle(){
//        binding.itemrecycle.setAdapter(new AdapterSelectAddress());
    }

    void getAddress(){
        dialog.show();
        chinniInterface.MY_ADDRESS_LIST_CALL(sessionManage.getUserid()).enqueue(new Callback<MyAddressList>() {
            @Override
            public void onResponse(Call<MyAddressList> call, Response<MyAddressList> response) {
                if(response.isSuccessful()){
                    if(response.body().isSuccess()){
                        if(response.body().getData().isEmpty()){
                            binding.itemrecycle.setVisibility(View.GONE);
                            binding.msg.setVisibility(View.VISIBLE);
                        }else {
                            binding.itemrecycle.setVisibility(View.VISIBLE);
                            binding.msg.setVisibility(View.GONE);
                            binding.itemrecycle.setAdapter(new AdapterSelectAddress(response.body().getData()));
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.appbar).setVisibility(View.VISIBLE);
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
}