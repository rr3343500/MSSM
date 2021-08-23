package com.root.mssm.ui.search.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.root.mssm.Adapter.AdapterSuggestion;
import com.root.mssm.List.List.suggestionlist.SuggestionList;
import com.root.mssm.R;
import com.root.mssm.databinding.SearchFragmentBinding;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class SearchFragment extends Fragment {

    private static final int RESULT_OK = -1;
    private SearchViewModel mViewModel;
    SearchFragmentBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;
    private final int REQ_CODE = 100;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SearchFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
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
        getActivity().findViewById(R.id.appbar).setVisibility(View.GONE);
        if (binding.search.requestFocus()) {
            ((InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
            );
        }

        binding.searchVoiceBtn.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
            try {
                startActivityForResult(intent, REQ_CODE);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getActivity(), "Sorry your device not supported", Toast.LENGTH_SHORT).show();
            }
        });

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getSuggestion(s.toString());
            }
        });

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data){
                    ArrayList result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    StringBuilder sb = new StringBuilder();
                    for (int i=0; i<result.size(); i++){
                        sb.append(result.get(i).toString());
                        sb.append("\t");
                    }
                    binding.search.setText(sb);
                    Log.e(TAG, "onActivityResult: "+ sb );
                }
                break;
            }
        }
    }

    void getSuggestion(String s){
        chinniInterface.SUGGESTION_LIST_CALL(s).enqueue(new Callback<SuggestionList>() {
            @Override
            public void onResponse(Call<SuggestionList> call, Response<SuggestionList> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        if(response.body().getSearchResult()!=null && response.body().getSearchCat()!= null && response.body().getSearchSub()!= null  && response.body().getSearchChild()!= null){
                            if(response.body().getSearchResult().isEmpty() && response.body().getSearchCat().isEmpty() && response.body().getSearchSub().isEmpty() && response.body().getSearchChild().isEmpty()){
                                binding.menuRecyclerView.setVisibility(View.GONE);
                                binding.catRecycle.setVisibility(View.GONE);
                                binding.subRecycle.setVisibility(View.GONE);
                                binding.childRecycle.setVisibility(View.GONE);
                                binding.msg.setVisibility(View.VISIBLE);
                            }else {
                                binding.menuRecyclerView.setAdapter(new AdapterSuggestion(response.body().getSearchResult(), 0, response.body().getSearchCat(), response.body().getSearchSub(),response.body().getSearchChild(),s));
                                binding.catRecycle.setAdapter(new AdapterSuggestion(response.body().getSearchResult(), 1, response.body().getSearchCat(), response.body().getSearchSub(),response.body().getSearchChild(),s));
                                binding.subRecycle.setAdapter(new AdapterSuggestion(response.body().getSearchResult(), 2, response.body().getSearchCat(), response.body().getSearchSub(),response.body().getSearchChild(),s));
                                binding.childRecycle.setAdapter(new AdapterSuggestion(response.body().getSearchResult(), 3, response.body().getSearchCat(), response.body().getSearchSub(),response.body().getSearchChild(),s));
                                binding.menuRecyclerView.setVisibility(View.VISIBLE);
                                binding.catRecycle.setVisibility(View.VISIBLE);
                                binding.subRecycle.setVisibility(View.VISIBLE);
                                binding.childRecycle.setVisibility(View.VISIBLE);
                                binding.msg.setVisibility(View.GONE);
                            }
                        }else {
                            binding.menuRecyclerView.setVisibility(View.GONE);
                            binding.catRecycle.setVisibility(View.GONE);
                            binding.subRecycle.setVisibility(View.GONE);
                            binding.childRecycle.setVisibility(View.GONE);
                            binding.msg.setVisibility(View.VISIBLE);
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
            public void onFailure(Call<SuggestionList> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }

}