package com.root.mssm.ui.helpcenter;

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
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.root.mssm.Adapter.HelpAdapter;
import com.root.mssm.List.List.Contactus.ContactUs;
import com.root.mssm.List.List.privacylist.PrivacyList;
import com.root.mssm.List.List.signup.Signup;
import com.root.mssm.R;
import com.root.mssm.databinding.HelpFragmentBinding;
import com.root.mssm.databinding.PrivacyFragmentBinding;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpFragment extends Fragment {

    private HelpViewModel mViewModel;
    HelpFragmentBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;
    String MobilePattern = "[0-9]{10}";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HelpFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HelpViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onViewCreated(view, savedInstanceState);
        dialog= new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait...");
        navController = Navigation.findNavController(view);
        chinniInterface= Config.getClient().create(ChinniInterface.class);
        binding.topPanel.title.setText("Help Center");
        binding.topPanel.countview.setVisibility(View.GONE);
        binding.topPanel.cart.setVisibility(View.GONE);
        binding.topPanel. count.setVisibility(View.GONE);
        sessionManage= new SessionManage(getActivity());
        cookies= new Cookies();
        getActivity().findViewById(R.id.appbar).setVisibility(View.GONE);
        binding.topPanel.back.setOnClickListener(v -> navController.popBackStack());

        if(!sessionManage.isLoggedIn()){
            navController.popBackStack();
            navController.navigate(R.id.loginFragment);
        }else {
            FragmentAction();
        }


    }


    void FragmentAction(){
        binding.submit.setOnClickListener(v -> {
            if(validation()){
                Submit();
            }
        });
    }


    private boolean validation(){
        boolean res = true;

        if (binding.name.getText().toString().isEmpty()) {
            binding.name.setError( "Enter the name" );
            binding.name.requestFocus();
            res = false;
        }

        if(!binding.phone.getText().toString().matches(MobilePattern)) {
            binding.phone.setError( "Please enter valid 10 digit phone number" );
            res= false ;
        }

        if (!binding.email.getText().toString().matches(emailPattern)) {
            binding.email.setError( "Please Enter Valid Email Address" );
            binding.email.requestFocus();
            res = false ;
        }


        if (binding.message.getText().toString().length() < 6) {
            binding.message.setError( "Please Enter Message" );
            binding.message.requestFocus();
            res= false;
        }

        return res;
    }


    void Submit(){
        dialog.show();
        JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("email",binding.email.getText().toString());
        jsonObject.addProperty("name",binding.name.getText().toString());
        jsonObject.addProperty("phone",binding.phone.getText().toString());
        jsonObject.addProperty("text",binding.message.getText().toString());
        jsonObject.addProperty("userid",sessionManage.getUserid());

        chinniInterface.CONTACT_US_API(jsonObject).enqueue(new Callback<ContactUs>() {
            @Override
            public void onResponse(Call<ContactUs> call, Response<ContactUs> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                          dialog.cancel();
                          Snackbar.make(binding.getRoot(),response.body().getStatus(),5000).show();
                          navController.popBackStack();
                          navController.navigate(R.id.helpFragment);
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
            public void onFailure(Call<ContactUs> call, Throwable t) {
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
        getActivity().findViewById(R.id.appbar).setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().findViewById(R.id.appbar).setVisibility(View.GONE);
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



}