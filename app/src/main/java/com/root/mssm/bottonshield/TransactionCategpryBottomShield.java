package com.root.mssm.bottonshield;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.root.mssm.Interface.CategoryFilter;
import com.root.mssm.databinding.FragmentTransactionCategpryBottomShieldBinding;
import com.root.mssm.databinding.WalletFragmentBinding;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class TransactionCategpryBottomShield extends BottomSheetDialogFragment {

    FragmentTransactionCategpryBottomShieldBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;
    CategoryFilter categoryFilter;
    JSONObject filter;
    boolean action= true;


    public TransactionCategpryBottomShield() {
        // Required empty public constructor
    }

    public void Listener(CategoryFilter categoryFilter){
        this.categoryFilter= categoryFilter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTransactionCategpryBottomShieldBinding.inflate(inflater, container, false);
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
        filter = new JSONObject();
        Action();
    }

    void Action(){
       binding.close.setOnClickListener(v -> { dismiss(); });
       binding.clear.setOnClickListener(v -> {
           binding.merchantCheck.setChecked(false);
           binding.rechargeCheck.setChecked(false);
           binding.moneyCheck.setChecked(false);
           binding.debitedCheck.setChecked(false);
           binding.allCheck.setChecked(false);
       });

       binding.merchantCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
           if(isChecked){
               if(action){
                   action= false;
                   binding.rechargeCheck.setChecked(false);
                   binding.moneyCheck.setChecked(false);
                   binding.debitedCheck.setChecked(false);
                   binding.allCheck.setChecked(false);
                   action= true;
               }

               try {
                   filter.putOpt("merchant","merchant");
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }else {
               filter.remove("merchant");
           }
       });

        binding.rechargeCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(action){
                action= false;
                binding.merchantCheck.setChecked(false);
                binding.moneyCheck.setChecked(false);
                binding.debitedCheck.setChecked(false);
                binding.allCheck.setChecked(false);
                action= true;
            }

            if(isChecked){
                try {
                    filter.putOpt("recharge","recharge");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                filter.remove("recharge");
            }
        });

        binding.moneyCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(action){
                action= false;
                binding.rechargeCheck.setChecked(false);
                binding.merchantCheck.setChecked(false);
                binding.debitedCheck.setChecked(false);
                binding.allCheck.setChecked(false);
                action= true;
            }

            if(isChecked){
                try {
                    filter.putOpt("credit","credit");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                filter.remove("money");
            }
        });

        binding.debitedCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(action){
                action= false;
                binding.rechargeCheck.setChecked(false);
                binding.moneyCheck.setChecked(false);
                binding.merchantCheck.setChecked(false);
                binding.allCheck.setChecked(false);
                action= true;
            }

            if(isChecked){
                try {
                    filter.putOpt("debit","debit");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                filter.remove("debit");
            }
        });

     binding.allCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(action){
                action= false;
                binding.rechargeCheck.setChecked(false);
                binding.moneyCheck.setChecked(false);
                binding.merchantCheck.setChecked(false);
                binding.debitedCheck.setChecked(false);
                action= true;
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
           Log.e(TAG, "Action: "+ filter );
           categoryFilter.onFilterSelected(filter);
           dismiss();
       });


    }
}