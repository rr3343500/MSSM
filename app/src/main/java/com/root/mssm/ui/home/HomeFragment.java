package com.root.mssm.ui.home;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.google.android.material.snackbar.Snackbar;
import com.root.mssm.Adapter.AdapterBestProduct;
import com.root.mssm.Adapter.AdapterBottomSlider;
import com.root.mssm.Adapter.AdapterBrands;
import com.root.mssm.Adapter.AdapterFeatureProduct;
import com.root.mssm.Adapter.AdapterHotProducts;
import com.root.mssm.Adapter.AdapterLatestProduct;
import com.root.mssm.Adapter.AdapterTopProduct;
import com.root.mssm.Adapter.CategoryAdapter;
import com.root.mssm.Adapter.SliderAdapterExample;
import com.root.mssm.List.List.home.HomeList;
import com.root.mssm.R;
import com.root.mssm.databinding.FragmentHomeBinding;
import com.root.mssm.global.Global;
import com.root.mssm.service.ChinniInterface;
import com.root.mssm.service.Config;
import com.root.mssm.service.Cookies;
import com.root.mssm.service.SessionManage;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    NavController navController;
    ChinniInterface chinniInterface;
    ProgressDialog dialog;
    SessionManage sessionManage;
    Cookies cookies;
    SliderAdapterExample adapter1;
    AdapterBottomSlider adapterbottom;
    CategoryAdapter adapter;
    AdapterFeatureProduct adapterfeature;
    AdapterBestProduct adapterbestProduct;
    AdapterTopProduct adapterTopProduct;
    AdapterHotProducts adapterHotProducts;
    AdapterLatestProduct adapterLatestProduct;
    AdapterBrands adapterBrands;
    List<HomeList> homeLists= new ArrayList<>();
    boolean action= false;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait...");
        navController = Navigation.findNavController(view);
        chinniInterface = Config.getClient().create(ChinniInterface.class);
        sessionManage = new SessionManage(getActivity());
        cookies = new Cookies();
        if (((Global) getActivity().getApplicationContext()).getHomeLists().isEmpty()) {
            homeLists = cookies.getAppdata(getActivity());
            Log.e(TAG, "onViewCreated: "  + homeLists.isEmpty());
            if(homeLists.get(0).getSliders()!=null){
                setCategory();
                setSlider();
                setFeatureProduct();
                setBestProduct();
                setTopProduct();
                setBottomSlider();
                setHOTProduct();
                setLatestProduct();
                setBrands();
                FragmentAction();
                dialog.cancel();
                GetContent(false);
            }else {
                GetContent(true);
            }

        } else {
            homeLists = ((Global) getActivity().getApplicationContext()).getHomeLists();
            setCategory();
            setSlider();
            setFeatureProduct();
            setBestProduct();
            setTopProduct();
            setBottomSlider();
            setHOTProduct();
            setLatestProduct();
            setBrands();
            FragmentAction();
            dialog.cancel();
        }
        binding.content.refresh.setOnRefreshListener(() -> {
            GetContent(false);
        });


    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void setCategory(){
        adapter=new CategoryAdapter(homeLists.get(0).getSupercatimage());
        binding.content.category.setVisibility( View.VISIBLE );
        binding.content.category.setAdapter( adapter );
    }

    void setSlider(){

        adapter1 = new SliderAdapterExample( getActivity(), homeLists.get(0).getSliders() );
        binding.content.sliderView.setSliderAdapter(adapter1);
        binding.content.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.content.sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.content.sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.content.sliderView.setIndicatorSelectedColor(Color.WHITE);
        binding.content.sliderView.setIndicatorUnselectedColor(Color.GRAY);
        binding.content.sliderView.setScrollTimeInSec(5);
        binding.content.sliderView.startAutoCycle();
    }

    void setBottomSlider(){
        adapterbottom = new AdapterBottomSlider( getActivity(), homeLists.get(0).getBottomslider() );
        binding.content.bottomsliderView.setSliderAdapter(adapterbottom);
        binding.content.bottomsliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.content.bottomsliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.content.bottomsliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.content.bottomsliderView.setIndicatorSelectedColor(Color.WHITE);
        binding.content.bottomsliderView.setIndicatorUnselectedColor(Color.GRAY);
        binding.content.bottomsliderView.setScrollTimeInSec(5);
        binding.content.bottomsliderView.startAutoCycle();
    }

    void setFeatureProduct(){
        adapterfeature=new AdapterFeatureProduct(homeLists.get(0).getFeaturedprod());
        binding.content.featureproduct.setVisibility( View.VISIBLE );
        binding.content.featureproduct.setAdapter( adapterfeature );
    }

    void setBestProduct(){
        adapterbestProduct=new AdapterBestProduct(homeLists.get(0).getBestprod());
        binding.content.bestproduct.setVisibility( View.VISIBLE );
        binding.content.bestproduct.setAdapter( adapterbestProduct );
    }

    void setTopProduct(){
        adapterTopProduct=new AdapterTopProduct(homeLists.get(0).getTopprod());
        binding.content.topproducts.setVisibility( View.VISIBLE );
        binding.content.topproducts.setAdapter( adapterTopProduct );
    }

    void setHOTProduct(){
        adapterHotProducts=new AdapterHotProducts(homeLists.get(0).getHotprod());
        binding.content.hotproduct.setVisibility( View.VISIBLE );
        binding.content.hotproduct.setAdapter( adapterHotProducts );
    }

    void setLatestProduct(){
        adapterLatestProduct=new AdapterLatestProduct(homeLists.get(0).getLatestprod());
        binding.content.latestproducts.setVisibility( View.VISIBLE );
        binding.content.latestproducts.setAdapter( adapterLatestProduct );
    }

    void setBrands(){
        adapterBrands=new AdapterBrands(homeLists.get(0).getBrand());
        binding.content.brandproduct.setVisibility( View.VISIBLE );
        binding.content.brandproduct.setAdapter( adapterBrands );
    }

    void FragmentAction(){





//        binding.content.smithnestel.getViewTreeObserver().addOnScrollChangedListener(() -> {
//            if ( binding.content.smithnestel != null) {
//                if ( binding.content.smithnestel.getScrollY()==0) {
//                    if(action){
//                        action= false;
//                        binding.toolbar.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    if(action){
//                    binding.toolbar.setVisibility(View.GONE);
//                    }
//                    action= true;
//                }
//            }
//        });
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
        getActivity().findViewById(R.id.appbar).setVisibility(View.VISIBLE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.appbar).setVisibility(View.VISIBLE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
    }

    void GetContent(Boolean aBoolean){
        if(aBoolean){dialog.show();}
        chinniInterface.HOME_LIST_CALL().enqueue(new Callback<HomeList>() {
            @Override
            public void onResponse(Call<HomeList> call, Response<HomeList> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("success")){
                        ((Global)getActivity().getApplicationContext()).setHomeLists(Collections.singletonList(response.body()));
//                        homeLists.clear();
                        homeLists = Collections.singletonList(response.body());
//                        cookies.removeCcchedata(getActivity(), new HomeList());
                        cookies.CacheDate(getActivity(),homeLists.get(0));
                        if(!aBoolean){
                            adapter.notifyDataSetChanged();
                            adapter1.notifyDataSetChanged();
                            adapterbottom.notifyDataSetChanged();
                            adapterfeature.notifyDataSetChanged();
                            adapterbestProduct.notifyDataSetChanged();
                            adapterTopProduct.notifyDataSetChanged();
                            adapterHotProducts.notifyDataSetChanged();
                            adapterLatestProduct.notifyDataSetChanged();
                            adapterBrands.notifyDataSetChanged();
                            FragmentAction();
                        }else {
                            setCategory();
                            setSlider();
                            setFeatureProduct();
                            setBestProduct();
                            setTopProduct();
                            setBottomSlider();
                            setHOTProduct();
                            setLatestProduct();
                            setBrands();
                            FragmentAction();
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
                binding.content.refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<HomeList> call, Throwable t) {
                dialog.cancel();
                binding.content.refresh.setRefreshing(false);
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });

        
    }

}