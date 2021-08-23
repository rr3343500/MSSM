package com.root.mssm.ui.wallet;

import androidx.appcompat.app.AppCompatActivity;
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
import com.root.mssm.Adapter.AdapterWalletTransaction;
import com.root.mssm.Interface.CategoryFilter;
import com.root.mssm.Interface.MonthFilter;
import com.root.mssm.List.List.walletlist.WalletData;
import com.root.mssm.List.List.walletlist.WalletHistoryList;
import com.root.mssm.R;
import com.root.mssm.bottonshield.TransactionCategpryBottomShield;
import com.root.mssm.bottonshield.TransactionMonthBottomShield;
import com.root.mssm.databinding.SearchProductsFragmentBinding;
import com.root.mssm.databinding.WalletFragmentBinding;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WalletFragment extends Fragment implements CategoryFilter, MonthFilter {

    private WalletViewModel mViewModel;
    WalletFragmentBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;
    List<WalletData> walletData= new ArrayList<>();
    AdapterWalletTransaction adapterWalletTransaction;
    List<WalletData>temp= new ArrayList<>();
    Date Start,end;

    public static WalletFragment newInstance() {
        return new WalletFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WalletFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WalletViewModel.class);
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
        binding.topPanel.title.setText("Wallet");
        binding.topPanel.cart.setVisibility(View.GONE);
        binding.topPanel.countview.setVisibility(View.GONE);
        getWallet();
        FragmentAction();
    }

    void FragmentAction(){
        binding.topPanel.back.setOnClickListener(v -> {navController.popBackStack();});
        binding.category.setOnClickListener(v -> {
            TransactionCategpryBottomShield transactionCategpryBottomShield = new TransactionCategpryBottomShield();
            transactionCategpryBottomShield.Listener(this);
            transactionCategpryBottomShield.show(getActivity().getSupportFragmentManager(),"transactionCategpryBottomShield");
        });
        binding.month.setOnClickListener(v -> {
            TransactionMonthBottomShield transactionMonthBottomShield= new TransactionMonthBottomShield();
            transactionMonthBottomShield.Listener(this);
            transactionMonthBottomShield.show(getActivity().getSupportFragmentManager(),"transactionMonthBottomShield");
        });
    }



    void getWallet(){
        dialog.show();
        chinniInterface.WALLET_HISTORY_LIST_CALL("596").enqueue(new Callback<WalletHistoryList>() {
            @Override
            public void onResponse(Call<WalletHistoryList> call, Response<WalletHistoryList> response) {
                if(response.isSuccessful()){
                    if(response.body().isSuccess()){

                        if(response.body().getData().isEmpty()){
                            binding.mainview.setVisibility(View.GONE);
                            binding.msg.setVisibility(View.VISIBLE);
                        }else {
                            walletData.addAll(response.body().getData());
                            temp= response.body().getData();
                            binding.mainview.setVisibility(View.VISIBLE);
                            binding.msg.setVisibility(View.GONE);
                            adapterWalletTransaction = new AdapterWalletTransaction(temp);
                            binding.recycle.setAdapter(adapterWalletTransaction);

                        }
                        dialog.cancel();

                    }else {
                        dialog.cancel();
                        binding.mainview.setVisibility(View.GONE);
                        binding.msg.setVisibility(View.VISIBLE);
                        Snackbar.make(binding.getRoot(),response.body().getMessage(),5000).show();
                    }
                }else {
                    dialog.cancel();
                    binding.mainview.setVisibility(View.GONE);
                    binding.msg.setVisibility(View.VISIBLE);
                    Snackbar.make(binding.getRoot(),response.message(),5000).show();

                }
            }

            @Override
            public void onFailure(Call<WalletHistoryList> call, Throwable t) {
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

    @Override
    public void onFilterSelected(JSONObject SelectedField) {
        temp.clear();
        Log.e(TAG, "onFilterSelected: "+ SelectedField );
        if(SelectedField.optString("credit").equals("credit")){
            for(int i=0; i< walletData.size(); i++){
                if(walletData.get(i).getPaymentType().equals("0")){
                    temp.add(walletData.get(i));
                }
            }
        }
        if(SelectedField.optString("debit").equals("debit")){
            for(int i=0; i< walletData.size(); i++){
                if(walletData.get(i).getPaymentType().equals("1")){
                    temp.add(walletData.get(i));
                }
            }
        }
        if(SelectedField.optString("all").equals("all")){
            temp.addAll(walletData);
        }



        if(temp.isEmpty()){
            binding.recycle.setVisibility(View.GONE);
            binding.msg.setVisibility(View.VISIBLE);
        }else {
            binding.recycle.setVisibility(View.VISIBLE);
            binding.msg.setVisibility(View.GONE);
            adapterWalletTransaction.updatelist();
        }

    }

    @Override
    public void onMonthSelected(JSONObject SelectedField) {
        Log.e(TAG, "onFilterSelected: "+ SelectedField );
        temp.clear();

        if(SelectedField.optString("all").equals("all")){
            temp.addAll(walletData);
        }

        if(SelectedField.optString("thismonth").equals("thismonth")){
            getthismonth();
            for(int i=0; i< walletData.size(); i++){
                Date date1= null;
                try {


                    DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                    Log.e(TAG, "onMonthSelected: 3  "+ walletData.get(i).getDatetime().substring(0,10).replace("-","/") );
                     date1 = df.parse( walletData.get(i).getDatetime().substring(0,10).replace("-","/") );
                    if(Start.before(date1) && end.after(date1)){
                        temp.add(walletData.get(i));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }


        if(temp.isEmpty()){
            binding.recycle.setVisibility(View.GONE);
            binding.msg.setVisibility(View.VISIBLE);
        }else {
            binding.recycle.setVisibility(View.VISIBLE);
            binding.msg.setVisibility(View.GONE);
            adapterWalletTransaction.updatelist();
        }

    }


    void getthismonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date monthFirstDay = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthLastDay = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = df.format(monthFirstDay);
        String endDateStr = df.format(monthLastDay);
        Start= monthFirstDay;
        end= monthLastDay;

        Log.e("DateFirstLast",startDateStr+" "+endDateStr);
    }
}