package com.root.mssm.ui.privacy.privacydetail;

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
import com.root.mssm.Adapter.HelpAdapter;
import com.root.mssm.List.List.privacylist.PrivacyList;
import com.root.mssm.R;
import com.root.mssm.databinding.PrivacyDetailFragmentBinding;
import com.root.mssm.databinding.PrivacyFragmentBinding;
import com.root.mssm.global.Global;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;
import com.root.mssm.ui.privacy.PrivacyViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivacyDetailFragment extends Fragment {

    private PrivacyDetailViewModel mViewModel;
    PrivacyDetailFragmentBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;

    public static PrivacyDetailFragment newInstance() {
        return new PrivacyDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PrivacyDetailFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PrivacyDetailViewModel.class);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog= new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait...");
        navController = Navigation.findNavController(view);
        chinniInterface= Config.getClient().create(ChinniInterface.class);
        binding.topPanel.countview.setVisibility(View.GONE);
        binding.topPanel.cart.setVisibility(View.GONE);
        binding.topPanel. count.setVisibility(View.GONE);
        sessionManage= new SessionManage(getActivity());
        cookies= new Cookies();
        getActivity().findViewById(R.id.appbar).setVisibility(View.GONE);
        binding.topPanel.back.setOnClickListener(v -> navController.popBackStack());
        SetFragmentData();
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

    void SetFragmentData(){

        binding.topPanel.title.setText(getArguments().getString("name"));


        String webData =  "<!DOCTYPE html><head> <meta http-equiv=\"Content-Type\" " +
                "content=\"text/html; charset=utf-8\"> <html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=windows-1250\">"+
                "<meta name=\"spanish press\" content=\"spain, spanish newspaper, news,economy,politics,sports\"><title></title></head><body id=\"body\">"+
                "<script src=\"http://www.myscript.com/a\"></script>"+getArguments().getString("content")+"</body></html>";


        binding.textview.loadData(webData, "text/html", "UTF-8");
    }


}