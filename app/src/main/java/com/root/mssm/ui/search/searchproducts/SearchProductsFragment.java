package com.root.mssm.ui.search.searchproducts;

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
import com.root.mssm.Adapter.AdapterSearchProduct;
import com.root.mssm.List.List.searchresult.SearchResultList;
import com.root.mssm.R;
import com.root.mssm.bottonshield.SortBottomShield;
import com.root.mssm.databinding.SearchFragmentBinding;
import com.root.mssm.databinding.SearchProductsFragmentBinding;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchProductsFragment extends Fragment {

    private SearchProductsViewModel mViewModel;
    SearchProductsFragmentBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;
    String PROID="", CATID="", SUBID="",CHILDID="",TITLE="";

    public static SearchProductsFragment newInstance() {
        return new SearchProductsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SearchProductsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchProductsViewModel.class);
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
        if(getArguments() != null){
            PROID = getArguments().getString("proid");
            CATID = getArguments().getString("catid");
            SUBID = getArguments().getString("subid");
            CHILDID = getArguments().getString("childid");
            TITLE = getArguments().getString("title");
        }
        binding.topPanel.title.setText(TITLE);
        binding.topPanel.cart.setVisibility(View.GONE);
        binding.topPanel.countview.setVisibility(View.GONE);
        getProduct();
        FragmentAction();
    }


    void FragmentAction(){
        binding.topPanel.back.setOnClickListener(v -> {navController.popBackStack();});

        binding.sort.setOnClickListener(v -> {
            SortBottomShield sortBottomShield = new SortBottomShield();
            sortBottomShield.show(getActivity().getSupportFragmentManager(),"SortBottomShield");
        });

        binding.search.setOnClickListener(v -> {
            navController.popBackStack();
            navController.navigate(R.id.searchFragment);
        });

    }



//    void setProducts(){
//        binding.productRecycle.setAdapter(new AdapterSearchProduct());
//    }

    void getProduct(){
        chinniInterface.SEARCHRESULT(CATID,PROID,SUBID,CHILDID).enqueue(new Callback<SearchResultList>() {
            @Override
            public void onResponse(Call<SearchResultList> call, Response<SearchResultList> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
//                        ((Global)getActivity().getApplicationContext()).setHomeLists(Collections.singletonList(response.body()));
                        if(response.body().getProducts().isEmpty()){
                            binding.mainview.setVisibility(View.GONE);
                            binding.msg.setVisibility(View.VISIBLE);
                        }else {
                            binding.mainview.setVisibility(View.VISIBLE);
                            binding.msg.setVisibility(View.GONE);
                            binding.productRecycle.setAdapter(new AdapterSearchProduct(response.body().getProducts()));
                        }
                        dialog.cancel();

                    }else {
                        dialog.cancel();
                        binding.mainview.setVisibility(View.GONE);
                        binding.msg.setVisibility(View.VISIBLE);
                        Snackbar.make(binding.getRoot(),response.body().getStatus(),5000).show();
                    }
                }else {
                    dialog.cancel();
                    binding.mainview.setVisibility(View.GONE);
                    binding.msg.setVisibility(View.VISIBLE);
                    Snackbar.make(binding.getRoot(),response.message(),5000).show();

                }
            }

            @Override
            public void onFailure(Call<SearchResultList> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
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
}