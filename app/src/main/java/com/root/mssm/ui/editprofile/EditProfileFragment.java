package com.root.mssm.ui.editprofile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.root.mssm.List.List.cartcount.CartCountList;
import com.root.mssm.List.List.signup.Userid;
import com.root.mssm.List.List.updateprofilelist.UpdateProfileList;
import com.root.mssm.R;
import com.root.mssm.databinding.EditProfileFragmentBinding;
import com.root.mssm.databinding.FragmentNotificationsBinding;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class EditProfileFragment extends Fragment {

    private EditProfileViewModel mViewModel;
    private EditProfileFragmentBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;
    MultipartBody.Part filePart= null;
    private final int SELECT_PHOTO = 1;

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = EditProfileFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog= new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait...");
        navController = Navigation.findNavController(view);
        chinniInterface= Config.getClient().create(ChinniInterface.class);
        sessionManage= new SessionManage(getActivity());
        binding.topPanel.title.setText("Edit Profile");
        cookies= new Cookies();
        if(!sessionManage.isLoggedIn()){
            navController.popBackStack();
            navController.navigate(R.id.loginFragment);

        }else {
            SetFragmentData();
            FragmnetAction();
            getCartCount();
        }

    }

    void FragmnetAction(){
        binding.changepassword.setOnClickListener(v -> { navController.navigate(R.id.changePasswordFragment); });
        binding.topPanel.back.setOnClickListener(v -> {navController.popBackStack();});
        binding.topPanel.cart.setOnClickListener(v -> { navController.navigate(R.id.cartFragment); });
        binding.imageEdit.setOnClickListener(v -> {
            ImagePicker.Companion.with(this.getActivity())
                    .crop()
                    .cropSquare()
                    .maxResultSize(1080, 1080,true)
                    .createIntent();

        });

        binding.submit.setOnClickListener(v -> {
            if(binding.name.getText().toString().isEmpty()){
                binding.name.setError("Empty");
            }else {
                UpdateProfile();
            }
        });
    }

    void SetFragmentData(){
        binding.name.setText(cookies.getFavorites(getContext()).getName());
        binding.email.setText(cookies.getFavorites(getContext()).getEmail());
        binding.mobile.setText(cookies.getFavorites(getContext()).getPhone());
        Glide.with(getActivity())
                .load(Config.Image_URL + cookies.getFavorites(getContext()).getPhoto())
                .fitCenter()
                .placeholder(R.drawable.user)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.image);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri fileUri = data.getData();
            binding.image.setImageURI(fileUri);
            File file =  ImagePicker.Companion.getFile(data);
            filePart = MultipartBody.Part.createFormData("photo", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getActivity(), ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void UpdateProfile(){
        dialog.show();
        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), binding.name.getText().toString());
        RequestBody userid = RequestBody.create(MediaType.parse("multipart/form-data"), sessionManage.getUserid());
        Log.e(TAG, "UpdateProfile: " +  sessionManage.getUserid() );

        chinniInterface.PROFILE_UPDATE_CALL(filePart,name,userid).enqueue(new Callback<UpdateProfileList>() {
            @Override
            public void onResponse(Call<UpdateProfileList> call, Response<UpdateProfileList> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        Userid userid1=  cookies.getFavorites(getActivity());
                        userid1.setName(binding.name.getText().toString());
                        userid1.setPhoto(response.body().getUserid().getPhoto());
                        cookies.removeFavorite(getActivity());
                        cookies.addFavorite(getActivity(), userid1);
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
            public void onFailure(Call<UpdateProfileList> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }

}