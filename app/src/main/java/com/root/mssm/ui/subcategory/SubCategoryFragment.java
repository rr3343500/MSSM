package com.root.mssm.ui.subcategory;

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
import com.root.mssm.Adapter.AdapterMainCatProduct;
import com.root.mssm.Adapter.AdapterSubCategory;
import com.root.mssm.List.List.home.HomeList;
import com.root.mssm.List.List.home.Subcategory;
import com.root.mssm.List.List.maincatproduct.MainCategoryProduct;
import com.root.mssm.databinding.CategoryFragmentBinding;
import com.root.mssm.databinding.SubCategoryFragmentBinding;
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

import static android.content.ContentValues.TAG;

public class SubCategoryFragment extends Fragment {

    private SubCategoryViewModel mViewModel;
    private SubCategoryFragmentBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;
    String CatId="";

    public static SubCategoryFragment newInstance() {
        return new SubCategoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SubCategoryFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SubCategoryViewModel.class);
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
        binding.topPanel.title.setText("SubCategories");
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
        List<Subcategory> subcategories;
        List<Subcategory>temp= new ArrayList<>();
        subcategories= ((Global)getActivity().getApplicationContext()).getHomeLists().get(0).getSubcategory();
        for(int i = 0; i< subcategories.size(); i++ ){
            Log.e(TAG, "setCategory: "+  CatId + " ==  "  +subcategories.get(i).getCategoryId());
            if(CatId.equals(subcategories.get(i).getCategoryId())){
                temp.add(subcategories.get(i));
            }
        }
        binding.subcategory.setAdapter(new AdapterSubCategory(temp));
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
        chinniInterface.MAINCATPRODUCT_CALL(CatId).enqueue(new Callback<MainCategoryProduct>() {
            @Override
            public void onResponse(Call<MainCategoryProduct> call, Response<MainCategoryProduct> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        dialog.cancel();
                        binding.mainProduct.setAdapter(new AdapterMainCatProduct(response.body().getMaincategoryprod()));
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
            public void onFailure(Call<MainCategoryProduct> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }

}