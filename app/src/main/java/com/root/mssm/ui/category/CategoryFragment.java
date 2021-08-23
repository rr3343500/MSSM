package com.root.mssm.ui.category;

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
import com.root.mssm.Adapter.AdapterCategoryList;
import com.root.mssm.Adapter.AdapterSuperProduct;
import com.root.mssm.List.List.home.Category;
import com.root.mssm.List.List.home.HomeList;
import com.root.mssm.List.List.superproducts.Superproduct;
import com.root.mssm.databinding.CategoryFragmentBinding;
import com.root.mssm.databinding.FragmentHomeBinding;
import com.root.mssm.global.Global;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  CategoryFragment extends Fragment {

    private CategoryViewModel mViewModel;
    private CategoryFragmentBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;
    String CatId="";

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CategoryFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
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
        binding.topPanel.title.setText("Categories");
        if(getArguments()!= null){
            CatId = getArguments().getString("id");
        }
        setCategory();
        GetProducts();
        FragmentAction();
        binding.refresh.setOnRefreshListener(() -> {
            GetContent();
        });
    }

    void setCategory(){
        List<Category>categories;
        List<Category>temp= new ArrayList<>();
        categories= ((Global)getActivity().getApplicationContext()).getHomeLists().get(0).getCategory();
        for(int i = 0; i< categories.size(); i++ ){
            if(CatId.equals(categories.get(i).getDadaCategorie())){
                temp.add(categories.get(i));
            }
        }
        binding.category.setAdapter(new AdapterCategoryList(temp));
    }
    void FragmentAction(){
        binding.topPanel.back.setOnClickListener(v -> {navController.popBackStack();});
    }

    void GetContent(){
        chinniInterface.HOME_LIST_CALL().enqueue(new Callback<HomeList>() {
            @Override
            public void onResponse(Call<HomeList> call, Response<HomeList> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        ((Global)getActivity().getApplicationContext()).setHomeLists(Collections.singletonList(response.body()));
                        setCategory();
                        GetProducts();
                        dialog.cancel();

                    }else {
                        dialog.cancel();
                        Snackbar.make(binding.getRoot(),response.body().getStatus(),5000).show();
                    }
                }else {
                    dialog.cancel();
                    Snackbar.make(binding.getRoot(),response.message(),5000).show();

                }
                binding.refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<HomeList> call, Throwable t) {
                dialog.cancel();
                binding.refresh.setRefreshing(false);
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }

    void GetProducts(){
        dialog.show();
        chinniInterface.SUPERPRODUCT_CALL(CatId).enqueue(new Callback<Superproduct>() {
            @Override
            public void onResponse(Call<Superproduct> call, Response<Superproduct> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        dialog.cancel();
                        binding.superProd.setAdapter(new AdapterSuperProduct(response.body().getSuperprod()));
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
            public void onFailure(Call<Superproduct> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }
}