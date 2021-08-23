package com.root.mssm.ui.myaddress.editaddress;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.location.Address;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.root.mssm.Adapter.AdapterState;
import com.root.mssm.Adapter.CityAdapter;
import com.root.mssm.Interface.CurrentLocation;
import com.root.mssm.Interface.SelectStateCity;
import com.root.mssm.List.List.addnewaddress.AddNewAddress;
import com.root.mssm.List.List.cartcount.CartCountList;
import com.root.mssm.List.List.citystatelist.City;
import com.root.mssm.List.List.citystatelist.CityStateList;
import com.root.mssm.List.List.myaddresslist.AddressData;
import com.root.mssm.Location.GetCurrentLocation;
import com.root.mssm.R;
import com.root.mssm.databinding.AddNewAddressFragmentBinding;
import com.root.mssm.databinding.EditAddressFragmentBinding;
import com.root.mssm.databinding.MyAddressFragmentBinding;
import com.root.mssm.global.Global;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAddressFragment extends Fragment implements SelectStateCity, CurrentLocation {

    private EditAddressViewModel mViewModel;
    private EditAddressFragmentBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;
    String TYPE="", CITY="", STATE="";
    CityStateList cityStateList;
    AddressData addressData= new AddressData();

    public static EditAddressFragment newInstance() {
        return new EditAddressFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = EditAddressFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EditAddressViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog= new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait...");
        navController = Navigation.findNavController(view);
        chinniInterface= Config.getClient().create(ChinniInterface.class);
        sessionManage= new SessionManage(getActivity());
        cookies= new Cookies();
        binding.topPanel.title.setText("Edit Address");
        if(!sessionManage.isLoggedIn()){
            navController.navigate(R.id.loginFragment);
            navController.popBackStack();
        }
        addressData= ((Global)getActivity().getApplicationContext()).getAddressData();
        SetFragmentData();
        FragmnetAction();
        getCartCount();
        getCityState();
    }

    void FragmnetAction(){
        binding.topPanel.back.setOnClickListener(v -> {navController.popBackStack();});
        binding.topPanel.cart.setOnClickListener(v -> { navController.navigate(R.id.cartFragment); });
        binding.locationLayout.setOnClickListener(v -> getLocation());

        binding.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.name.getText().toString().isEmpty()){
                    binding.name.setError("Empty");
                }else {
                    binding.name.setError(null);
                }
            }
        });
        binding.phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.phone.getText().toString().isEmpty()){
                    binding.phone.setError("Empty");
                }else {
                    binding.phone.setError(null);
                }
            }
        });
        binding.pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.pincode.getText().toString().isEmpty()){
                    binding.pincode.setError("Empty");
                }else {
                    binding.pincode.setError(null);
                }
            }
        });
        binding.state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.state.getText().toString().isEmpty()){
                    binding.state.setError("Empty");
                }else {
                    binding.state.setError(null);
                }
            }
        });
        binding.city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.city.getText().toString().isEmpty()){
                    binding.city.setError("Empty");
                }else {
                    binding.city.setError(null);
                }
            }
        });
        binding.house.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.house.getText().toString().isEmpty()){
                    binding.house.setError("Empty");
                }else {
                    binding.house.setError(null);
                }
            }
        });
        binding.road.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.road.getText().toString().isEmpty()){
                    binding.road.setError("Empty");
                }else {
                    binding.road.setError(null);
                }
            }
        });

        binding.save.setOnClickListener(v -> {
            if(Validate()){
                SaveAddress();
            }
        });

        binding.homeLayout.setOnClickListener(v -> {
            binding.homeLayout.setStrokeColor(getResources().getColor(R.color.purple_500));
//            binding.homeTitle.setTextColor(getResources().getColor(R.color.purple_500));
//            binding.wordTitle.setTextColor(getResources().getColor(R.color.gray_text));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                binding.homeTitle.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.purple_500));
//                binding.wordTitle.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.gray_text));
//            }
            binding.workLayout.setStrokeColor(getResources().getColor(R.color.gray_text));
            TYPE = "0";
        });

        binding.workLayout.setOnClickListener(v -> {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                binding.homeTitle.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.gray_text));
//                binding.wordTitle.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.purple_500));
//            }
            binding.homeLayout.setStrokeColor(getResources().getColor(R.color.gray_text));
            binding.workLayout.setStrokeColor(getResources().getColor(R.color.purple_500));
//            binding.homeTitle.setTextColor(getResources().getColor(R.color.gray_text));
//            binding.wordTitle.setTextColor(getResources().getColor(R.color.purple_500));
            TYPE="1";
        });
    }

    void SetFragmentData(){
        binding.name.setText(addressData.getName());
        binding.pincode.setText(addressData.getPincode());
        binding.state.setText(addressData.getState());
        binding.city.setText(addressData.getCity());
        binding.phone.setText(addressData.getPhone());
        binding.house.setText(addressData.getHouse());
        binding.road.setText(addressData.getStreet());
        if(addressData.getAddType().equals("0")){
            binding.homeLayout.setStrokeColor(getResources().getColor(R.color.purple_500));
            binding.workLayout.setStrokeColor(getResources().getColor(R.color.gray_text));
        }else {
            binding.homeLayout.setStrokeColor(getResources().getColor(R.color.gray_text));
            binding.workLayout.setStrokeColor(getResources().getColor(R.color.purple_500));
        }

        TYPE=addressData.getAddType(); CITY=addressData.getCity();STATE=addressData.getState();


    }

    void getLocation(){
        dialog.show();
        GetCurrentLocation location = new GetCurrentLocation(getActivity(),this);
        location.getLastLocation();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
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

    void getCartCount(){
        dialog.show();
        chinniInterface.COUNT_LIST_CALL(sessionManage.getUserid()).enqueue(new Callback<CartCountList>() {
            @Override
            public void onResponse(Call<CartCountList> call, Response<CartCountList> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        binding.topPanel.count.setText(String.valueOf(response.body().getCartcount()));
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
            public void onFailure(Call<CartCountList> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }

    void getCityState(){
        chinniInterface.CITY_STATE_LIST_CALL().enqueue(new Callback<CityStateList>() {
            @Override
            public void onResponse(Call<CityStateList> call, Response<CityStateList> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        cityStateList= response.body();
                        binding.stateLayout.setOnClickListener(v -> StateDialog());
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
            public void onFailure(Call<CityStateList> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }

    void StateDialog(){
        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_state_layout);
        RecyclerView staterecycle= dialog1.findViewById(R.id.recycle);
        staterecycle.setAdapter(new AdapterState(this, cityStateList.getState(),dialog1));
        dialog1.show();
    }

    void CityDialog(List<City> cities){
        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_state_layout);
        RecyclerView staterecycle= dialog1.findViewById(R.id.recycle);
        TextView title_state= dialog1.findViewById(R.id.title_state);
        title_state.setText("Select City");
        staterecycle.setAdapter(new CityAdapter(this, cities,dialog1));
        dialog1.show();
    }

    @Override
    public void onStateSelected(String name, String id) {
        binding.state.setText(name);
        STATE= name;
        List<City> cities= new ArrayList<>();
        cities.clear();
        for(int i=0 ; i < cityStateList.getCity().size(); i++){
            if(cityStateList.getCity().get(i).getStateId().equals(id)){
                cities.add(cityStateList.getCity().get(i));
            }
        }
        binding.cityLayout.setOnClickListener(v -> CityDialog(cities));
    }

    @Override
    public void onCitySelected(String name, String id) {
        binding.city.setText(name);
        CITY= name;
        Log.e(TAG, "onCitySelected: "+ name );
    }

    @Override
    public void onSuccess(List<Address> location) {
        dialog.cancel();
        binding.pincode.setText(location.get(0).getPostalCode().toString());
        binding.state.setText(location.get(0).getAdminArea().toString());
        binding.city.setText(location.get(0).getSubAdminArea().toString());
//        binding.house.setText(location.get(0).getAddressLine().toString());
        binding.road.setText(location.get(0).getSubLocality().toString());
        CITY=location.get(0).getSubAdminArea().toString();
        STATE= location.get(0).getAdminArea().toString();

    }

    @Override
    public void onFailure(IOException error) {
        dialog.cancel();
        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    Boolean Validate(){
        boolean res= true;
        if(binding.name.getText().toString().isEmpty()){
            binding.name.setError("Empty");
            res= false;
        }if(binding.pincode.getText().toString().isEmpty()){
            binding.pincode.setError("Empty");
            res= false;
        }if(binding.state.getText().toString().isEmpty()){
            binding.state.setError("Empty");
            res= false;
        }if(binding.city.getText().toString().isEmpty()){
            binding.city.setError("Empty");
            res= false;
        }if(binding.house.getText().toString().isEmpty()){
            binding.house.setError("Empty");
            res= false;
        }if(binding.road.getText().toString().isEmpty()){
            binding.name.setError("Empty");
            res= false;
        }if(TYPE.isEmpty()){
            Snackbar.make(binding.getRoot(),"Select Address Type", 5000).show();
            res= false;
        }
        return res;
    }

    void SaveAddress(){
        dialog.show();
        JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("id",addressData.getId());
        jsonObject.addProperty("name",binding.name.getText().toString());
        jsonObject.addProperty("phone",binding.phone.getText().toString());
        jsonObject.addProperty("pincode",binding.pincode.getText().toString());
        jsonObject.addProperty("state",STATE);
        jsonObject.addProperty("city",CITY);
        jsonObject.addProperty("house",binding.house.getText().toString());
        jsonObject.addProperty("street",binding.road.getText().toString());
        jsonObject.addProperty("add_type",TYPE);
        chinniInterface.UPDATE_ADDRESS(jsonObject).enqueue(new Callback<AddNewAddress>() {
            @Override
            public void onResponse(Call<AddNewAddress> call, Response<AddNewAddress> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        dialog.cancel();
                        navController.popBackStack();
                        navController.navigate(R.id.myAddressFragment);
                    }else {
                        dialog.cancel();
                        Snackbar.make(binding.getRoot(), (CharSequence) response.body().getStatus(),5000).show();
                    }
                }else {
                    dialog.cancel();
                    Snackbar.make(binding.getRoot(),response.message(),5000).show();

                }
            }

            @Override
            public void onFailure(Call<AddNewAddress> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }

}