package com.root.mssm.ui.childcategory;

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
import com.root.mssm.Adapter.AdapterChildCategory;
import com.root.mssm.Adapter.AdapterSubCatProducts;
import com.root.mssm.List.List.home.Childcategory;
import com.root.mssm.List.List.home.HomeList;
import com.root.mssm.List.List.subcatproduct.SubCategoryProduct;
import com.root.mssm.databinding.ChildFragmentBinding;
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

public class ChildFragment extends Fragment {

    private ChildViewModel mViewModel;
    private ChildFragmentBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;
    String CatId="";

    public static ChildFragment newInstance() {
        return new ChildFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ChildFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChildViewModel.class);
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
        binding.topPanel.title.setText("Child Categories");
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
        List<Childcategory> categories;
        List<Childcategory>temp= new ArrayList<>();
        categories= ((Global)getActivity().getApplicationContext()).getHomeLists().get(0).getChildcategory();
        for(int i = 0; i< categories.size(); i++ ){
            Log.e(TAG, "setCategory: "+ CatId + " == " +  categories.get(i).getSubcategoryId());
            if(CatId.equals(categories.get(i).getSubcategoryId())){
                temp.add(categories.get(i));
            }
        }
        binding.category.setAdapter(new AdapterChildCategory(temp));
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
        chinniInterface.SUB_CATEGORY_PRODUCT_CALL(CatId).enqueue(new Callback<SubCategoryProduct>() {
            @Override
            public void onResponse(Call<SubCategoryProduct> call, Response<SubCategoryProduct> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        dialog.cancel();
                        binding.subProducts.setAdapter(new AdapterSubCatProducts(response.body().getSubcategoryprod()));
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
            public void onFailure(Call<SubCategoryProduct> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }
}