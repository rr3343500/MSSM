package com.root.mssm.ui.privacy;

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
import com.root.mssm.Adapter.AdapterSuggestion;
import com.root.mssm.Adapter.HelpAdapter;
import com.root.mssm.List.List.privacylist.PrivacyList;
import com.root.mssm.List.List.suggestionlist.SuggestionList;
import com.root.mssm.R;
import com.root.mssm.databinding.PrivacyFragmentBinding;
import com.root.mssm.databinding.SearchFragmentBinding;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivacyFragment extends Fragment {

    private PrivacyViewModel mViewModel;
    PrivacyFragmentBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;

    public static PrivacyFragment newInstance() {
        return new PrivacyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PrivacyFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PrivacyViewModel.class);
        // TODO: Use the ViewModel
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog= new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait...");
        navController = Navigation.findNavController(view);
        chinniInterface= Config.getClient().create(ChinniInterface.class);
        binding.topPanel.title.setText("Privacy & Others");
        binding.topPanel.countview.setVisibility(View.GONE);
        binding.topPanel.cart.setVisibility(View.GONE);
        binding.topPanel. count.setVisibility(View.GONE);
        sessionManage= new SessionManage(getActivity());
        cookies= new Cookies();
        getActivity().findViewById(R.id.appbar).setVisibility(View.GONE);
        getSuggestion();

        binding.topPanel.back.setOnClickListener(v -> navController.popBackStack());
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
        getActivity().findViewById(R.id.appbar).setVisibility(View.GONE);
    }


    void getSuggestion(){
        dialog.show();
        chinniInterface.PRIVACY_LIST_CALL().enqueue(new Callback<PrivacyList>() {
            @Override
            public void onResponse(Call<PrivacyList> call, Response<PrivacyList> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        if(response.body().getGetallpages().isEmpty()){
                            binding.recycle.setVisibility(View.GONE);
                            binding.msg.setVisibility(View.VISIBLE);
                        }else {
                            binding.msg.setVisibility(View.GONE);
                            binding.recycle.setVisibility(View.VISIBLE);
                            binding.recycle.setAdapter(new HelpAdapter(response.body().getGetallpages()));
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
            public void onFailure(Call<PrivacyList> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }

}