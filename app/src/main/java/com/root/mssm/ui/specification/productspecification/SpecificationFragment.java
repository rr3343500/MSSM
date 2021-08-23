package com.root.mssm.ui.specification.productspecification;

import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.root.mssm.Adapter.AdapterSpecificationTab;
import com.root.mssm.List.List.productdetail.ProductDetailList;
import com.root.mssm.List.List.specification.SpecificationList;
import com.root.mssm.R;
import com.root.mssm.databinding.SpecificationFragmentBinding;
import com.root.mssm.global.Global;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecificationFragment extends Fragment {

    private SpecificationViewModel mViewModel;
    private SpecificationFragmentBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;
    ProductDetailList productDetailList;
    SpecificationList specificationList;
    String ProductId="";

    public static SpecificationFragment newInstance() {
        return new SpecificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SpecificationFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SpecificationViewModel.class);
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
        getActivity().findViewById(R.id.appbar).setVisibility(View.GONE);
        binding.topPanel.title.setText("Specification");
        if(getArguments()!= null){
            ProductId = getArguments().getString("id");
        }

        GetContent();

    }

    void FragmentAction(){
        binding.topPanel.back.setOnClickListener(v -> {navController.popBackStack();});
    }

    void SetFragmentData(){

        Glide.with(getActivity())
                .load(Config.Image_URL + productDetailList.getProductdetail().get(0).getPhoto())
                .fitCenter()
                .placeholder(R.drawable.image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.image);

        binding.name.setText(productDetailList.getProductdetail().get(0).getName());
        binding.price.setText(getResources().getString(R.string.Rs)+productDetailList.getProductdetail().get(0).getPprice());
        binding.actualprice.setText(getResources().getString(R.string.Rs)+productDetailList.getProductdetail().get(0).getCprice());
        binding.discount.setText("6% off");
        binding.actualprice.setPaintFlags(binding.actualprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        binding.tabs.addTab(binding.tabs.newTab().setText("DESCRIPTION") );
        binding.tabs.addTab(binding.tabs.newTab().setText("SPECIFICATIONS") );
        binding.tabs.addTab(binding.tabs.newTab().setText("MORE INFO") );

        final AdapterSpecificationTab adapter = new AdapterSpecificationTab(getActivity(),getActivity().getSupportFragmentManager(), binding.tabs.getTabCount());
        binding.viewpager.setAdapter(adapter);

        binding.viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabs));

        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    void GetContent(){
        dialog.show();
        chinniInterface.SPECIFICATION_LIST_CALL(ProductId).enqueue(new Callback<SpecificationList>() {
            @Override
            public void onResponse(Call<SpecificationList> call, Response<SpecificationList> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        ((Global)getActivity().getApplicationContext()).setSpecificationList(response.body());
                        productDetailList= ((Global)getActivity().getApplicationContext()).getProductdetail();
                        specificationList = response.body();
                        FragmentAction();
                        SetFragmentData();
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
            public void onFailure(Call<SpecificationList> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().findViewById(R.id.appbar).setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.appbar).setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().findViewById(R.id.appbar).setVisibility(View.GONE);
    }
}