package com.root.mssm.ui.signup;

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
import com.root.mssm.List.List.signup.Signup;
import com.root.mssm.R;
import com.root.mssm.databinding.SignupFragmentBinding;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.NetworkCheck;
import com.root.mssm.service.SessionManage;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupFragment extends Fragment {
    SignupFragmentBinding binding;
    private SignupViewModel mViewModel;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    String MobilePattern = "[0-9]{10}";
    String PinPattern = "[0-9]{6}";
    SessionManage sessionManage;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Cookies cookies;

    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SignupFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SignupViewModel.class);
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

        binding.login.setOnClickListener(v -> navController.navigate(R.id.loginFragment));
        binding.txtLogin.setOnClickListener(v -> {
            if(validation()){
                if(new NetworkCheck().haveNetworkConnection(getActivity())){
                    dialog.show();
                    RequsetSignup();
                }
                else {
                    Snackbar.make(binding.getRoot(),"No Internet Connection",5000).show();
                }
            }
        });
    }

    private boolean validation(){
        boolean res = true;

        if (binding.namefist.getText().toString().isEmpty()) {
            binding.namefist.setError( "Enter the name" );
            binding.namefist.requestFocus();
            res = false;
        }

        if(!binding.phone.getText().toString().matches(MobilePattern)) {
            binding.phone.setError( "Please enter valid 10 digit phone number" );
            res= false ;
        }

        if(!binding.pincode.getText().toString().matches(PinPattern)) {
            binding.pincode.setError( "Please enter valid 6 digit Pincode" );
            res= false ;
        }
        if (!binding.email1.getText().toString().matches(emailPattern)) {
            binding.email1.setError( "Please Enter Valid Email Address" );
            binding.email1.requestFocus();
            res = false ;
        }
//        if (binding.selectcity.getText().toString().isEmpty()) {
//            binding.selectcity.setError( "Enter the city" );
//            binding.selectcity.requestFocus();
//           res= false;
//        }

//        if (binding.address.getText().toString().isEmpty()) {
//            binding.address.setError( "Enter the Address" );
//            binding.address.requestFocus();
//           res= false;
//        }

//        if (binding.referrel.getText().toString().isEmpty()) {
//            binding.referrel.setError( "Enter the  Referral ID" );
//            binding.referrel.requestFocus();
//           res= false;
//        }

        if (binding.password1.getText().toString().length() < 6) {
            binding.password1.setError( "pls enter more the 6 character in password" );
            binding.password1.requestFocus();
            res= false;
        }

        return res;
    }

    void RequsetSignup(){
        JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("referid",binding.referrel.getText().toString());
        jsonObject.addProperty("email",binding.email1.getText().toString());
        jsonObject.addProperty("name",binding.namefist.getText().toString());
        jsonObject.addProperty("phone",binding.phone.getText().toString());
        jsonObject.addProperty("city",binding.selectcity.getText().toString());
        jsonObject.addProperty("address",binding.address.getText().toString());
        jsonObject.addProperty("postalcode",binding.pincode.getText().toString());
        jsonObject.addProperty("password",binding.password1.getText().toString());

        chinniInterface.SIGNUP_CALL(jsonObject).enqueue(new Callback<Signup>() {
            @Override
            public void onResponse(Call<Signup> call, Response<Signup> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        cookies.addFavorite(getActivity(), response.body().getUserid());
                        sessionManage.UserDetail(
                                String.valueOf(response.body().getUserid().getId()),
                                response.body().getUserid().getName(),
                                response.body().getUserid().getEmail(),
                                response.body().getUserid().getPhone(),
                                response.body().getUserid().getAddress(),
                                response.body().getUserid().getCity(),
                                response.body().getUserid().getZip(),
                                response.body().getUserid().getPhoto(),
                                ""
                        );
                        dialog.cancel();
                        navController.popBackStack();
                        Bundle bundle = new Bundle();
                        Toast.makeText(getActivity(), response.body().getStatus() , Toast.LENGTH_SHORT).show();
                        bundle.putString("phone",response.body().getUserid().getPhone());
                        navController.navigate(R.id.OTPVarificationFragment,bundle);
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
            public void onFailure(Call<Signup> call, Throwable t) {
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
        getActivity().findViewById(R.id.appbar).setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view).setVisibility(View.GONE);
        getActivity().findViewById(R.id.appbar).setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view).setVisibility(View.GONE);
        getActivity().findViewById(R.id.appbar).setVisibility(View.GONE);
    }
}