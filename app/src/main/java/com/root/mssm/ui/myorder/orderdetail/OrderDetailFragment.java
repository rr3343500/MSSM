package com.root.mssm.ui.myorder.orderdetail;

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

import com.root.mssm.databinding.MyOrderFragmentBinding;
import com.root.mssm.databinding.OrderDetailFragmentBinding;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;

import org.jetbrains.annotations.NotNull;

public class OrderDetailFragment extends Fragment {

    private OrderDetailViewModel mViewModel;
    OrderDetailFragmentBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;

    public static OrderDetailFragment newInstance() {
        return new OrderDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding =     OrderDetailFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OrderDetailViewModel.class);
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
    }
}