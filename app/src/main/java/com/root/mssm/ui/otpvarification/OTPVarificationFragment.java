package com.root.mssm.ui.otpvarification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.root.mssm.List.List.signup.Signup;
import com.root.mssm.R;
import com.root.mssm.databinding.OTPVarificationFragmentBinding;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.SessionManage;
import com.root.mssm.sms.SmsBroadcastReceiver;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class OTPVarificationFragment extends Fragment implements TextWatcher {
    OTPVarificationFragmentBinding binding;
    private OTPVarificationViewModel mViewModel;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    String OTPpattern = "[0-9]{10}";
    String phone = "";
    SessionManage sessionManage;
    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;

    public static OTPVarificationFragment newInstance() {
        return new OTPVarificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = OTPVarificationFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog= new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait...");
        navController = Navigation.findNavController(view);
        chinniInterface= Config.getClient().create(ChinniInterface.class);
        sessionManage= new SessionManage(getActivity());
        getActivity().findViewById(R.id.appbar).setVisibility(View.GONE);
        startSmsUserConsent();
        if (getArguments() != null) {
            phone = getArguments().getString("phone");
        }
        binding.detailsTitle.setText("Pleace check your mobile number " + phone);
        binding.edit1.addTextChangedListener(this);
        binding.edit2.addTextChangedListener(this);
        binding.edit3.addTextChangedListener(this);
        binding.edit4.addTextChangedListener(this);

        binding.nextBtn.setOnClickListener(v -> {
            sessionManage.setIsLogin();
            navController.popBackStack();
            navController.navigate(R.id.navigation_home);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OTPVarificationViewModel.class);
        // TODO: Use the ViewModel
    }


    private boolean validation(){
        boolean res = true;

        if (binding.edit1.getText().toString().isEmpty()) {
            binding.edit1.setError("Empty");
            return false;
        }
        if (binding.edit2.getText().toString().isEmpty()) {
            binding.edit2.setError("Empty");
            return false;
        }
        if (binding.edit3.getText().toString().isEmpty()) {
            binding.edit3.setError("Empty");
            return false;
        }
        if (binding.edit4.getText().toString().isEmpty()) {
            binding.edit4.setError("Empty");
            return false;
        }

        return res;
    }

    void RequsetSignup(){
        String otp = binding.edit1.getText().toString() + binding.edit2.getText().toString() + binding.edit3.getText().toString() + binding.edit4.getText().toString();
        JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("otp",otp);
        jsonObject.addProperty("phone",phone);


        chinniInterface.SIGNUP_CALL(jsonObject).enqueue(new Callback<Signup>() {
            @Override
            public void onResponse(Call<Signup> call, Response<Signup> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
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


    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(getActivity());
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(aVoid ->
                Toast.makeText(getActivity(), "On Success", Toast.LENGTH_LONG).show()).addOnFailureListener(e -> Toast.makeText(getActivity(), "On OnFailure", Toast.LENGTH_LONG).show());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
//                textViewMessage.setText(String.format("%s - %s", getString(R.string.received_message), message));
                getOtpFromMessage(message);
            }
        }
    }

    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{4}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            Log.e(TAG, "getOtpFromMessage: "+ matcher.group(0).substring(0,1));
            Log.e(TAG, "getOtpFromMessage: "+ matcher.group(0).substring(1,2));
            Log.e(TAG, "getOtpFromMessage: "+ matcher.group(0).substring(2,3));
            Log.e(TAG, "getOtpFromMessage: "+ matcher.group(0).substring(3,4));
            binding.edit1.setText(matcher.group(0).substring(0,1));
            binding.edit2.setText(matcher.group(0).substring(1,2));
            binding.edit3.setText(matcher.group(0).substring(2,3));
            binding.edit4.setText(matcher.group(0).substring(3,4));
        }
    }


    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }
                    @Override
                    public void onFailure() {
                        Log.e(TAG, "onFailure: "+"failure" );
                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        getActivity().registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 1) {
            if (binding.edit1.length() == 1) {
                binding.edit2.requestFocus();
            }

            if (binding.edit2.length() == 1) {
                binding.edit3.requestFocus();
            }
            if (binding.edit3.length() == 1) {
                binding.edit4.requestFocus();
            }
        } else {
            if (binding.edit4.length() == 0) {
                binding.edit3.requestFocus();
            }
            if (binding.edit3.length() == 0) {
                binding.edit2.requestFocus();
            }
            if (binding.edit2.length() == 0) {
                binding.edit1.requestFocus();
            }
        }
    }


}