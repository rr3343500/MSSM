package com.root.mssm.bottonshield;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.root.mssm.Interface.MonthFilter;
import com.root.mssm.databinding.FragmentTransactionCategpryBottomShieldBinding;
import com.root.mssm.databinding.FragmentTransactionMonthBottomShieldBinding;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class TransactionMonthBottomShield extends BottomSheetDialogFragment {

    FragmentTransactionMonthBottomShieldBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;
    MonthFilter monthFilter;
    JSONObject filter= new JSONObject();
    boolean action=true;

    public TransactionMonthBottomShield() {
        // Required empty public constructor
    }

    public static TransactionMonthBottomShield newInstance(String param1, String param2) {
        TransactionMonthBottomShield fragment = new TransactionMonthBottomShield();
        return fragment;
    }

    public void Listener(MonthFilter monthFilter){
        this.monthFilter= monthFilter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTransactionMonthBottomShieldBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog= new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait...");
        chinniInterface= Config.getClient().create(ChinniInterface.class);
        sessionManage= new SessionManage(getActivity());
        cookies= new Cookies();
        Action();
    }

    void Action(){
        binding.close.setOnClickListener(v -> { dismiss(); });
        binding.clear.setOnClickListener(v -> {
            binding.thisMonthCheck.setChecked(false);
            binding.last3MonthCheck.setChecked(false);
            binding.last5MonthCheck.setChecked(false);
            binding.lastMonthCheck.setChecked(false);
            binding.allCheck.setChecked(false);
        });



        binding.thisMonthCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(action) {
                action=false;
                binding.allCheck.setChecked(false);
                binding.lastMonthCheck.setChecked(false);
                binding.last3MonthCheck.setChecked(false);
                binding.last5MonthCheck.setChecked(false);
                action=true;
            }
            if(isChecked){
                try {
                    filter.putOpt("thismonth","thismonth");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                filter.remove("thismonth");
            }
        });

        binding.lastMonthCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(action) {
                action=false;
                binding.allCheck.setChecked(false);
                binding.thisMonthCheck.setChecked(false);
                binding.last3MonthCheck.setChecked(false);
                binding.last5MonthCheck.setChecked(false);
                action=true;
            }
            if(isChecked){
                try {
                    filter.putOpt("lastmonth","lastmonth");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                filter.remove("lastmonth");
            }
        });

        binding.last3MonthCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(action) {
                action=false;
                binding.allCheck.setChecked(false);
                binding.lastMonthCheck.setChecked(false);
                binding.thisMonthCheck.setChecked(false);
                binding.last5MonthCheck.setChecked(false);
                action=true;
            }
            if(isChecked){
                try {
                    filter.putOpt("last3month","last3month");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                filter.remove("last3month");
            }
        });

        binding.last5MonthCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(action) {
                action=false;
                binding.allCheck.setChecked(false);
                binding.lastMonthCheck.setChecked(false);
                binding.last3MonthCheck.setChecked(false);
                binding.thisMonthCheck.setChecked(false);
                action=true;
            }
            if(isChecked){
                try {
                    filter.putOpt("last5month","last5month");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                filter.remove("last5month");
            }
        });

        binding.allCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(action) {
                action=false;
                binding.thisMonthCheck.setChecked(false);
                binding.lastMonthCheck.setChecked(false);
                binding.last3MonthCheck.setChecked(false);
                binding.thisMonthCheck.setChecked(false);
                action=true;
            }
            if(isChecked){
                try {
                    filter.putOpt("all","all");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                filter.remove("all");
            }
        });


        binding.apply.setOnClickListener(v -> {
            monthFilter.onMonthSelected(filter);
            dismiss();
        });


    }
}