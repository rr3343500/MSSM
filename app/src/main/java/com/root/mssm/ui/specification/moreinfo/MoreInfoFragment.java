package com.root.mssm.ui.specification.moreinfo;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.root.mssm.databinding.MoreInfoFragmentBinding;
import com.root.mssm.databinding.SpecificationFragmentBinding;
import com.root.mssm.global.Global;

import org.jetbrains.annotations.NotNull;

public class MoreInfoFragment extends Fragment {

    private MoreInfoViewModel mViewModel;
    private MoreInfoFragmentBinding binding;

    public static MoreInfoFragment newInstance() {
        return new MoreInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MoreInfoFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MoreInfoViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.originName.setText(((Global)getActivity().getApplicationContext()).getSpecificationList().getSpecification().get(0).getCountry());
        binding.genericName.setText(((Global)getActivity().getApplicationContext()).getSpecificationList().getSpecification().get(0).getGeneric());
        if(!((Global)getActivity().getApplicationContext()).getProductdetail().getProductdetail().get(0).getUserId().isEmpty()){
            binding.manufacName.setText("1. "+((Global)getActivity().getApplicationContext()).getProductdetail().getProductdetail().get(0).getUserId().get(0).getShopName());
        }

    }
}