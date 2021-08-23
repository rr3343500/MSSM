package com.root.mssm.ui.specification.discription;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.root.mssm.databinding.DiscriptionFragmentBinding;
import com.root.mssm.databinding.SpecificationFragmentBinding;
import com.root.mssm.global.Global;

import org.jetbrains.annotations.NotNull;

public class DiscriptionFragment extends Fragment {

    private DiscriptionViewModel mViewModel;
    private DiscriptionFragmentBinding binding;

    public static DiscriptionFragment newInstance() {
        return new DiscriptionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DiscriptionFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DiscriptionViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String webData =  "<!DOCTYPE html><head> <meta http-equiv=\"Content-Type\" " +
                "content=\"text/html; charset=utf-8\"> <html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=windows-1250\">"+
                "<meta name=\"spanish press\" content=\"spain, spanish newspaper, news,economy,politics,sports\"><title></title></head><body id=\"body\">"+
                "<script src=\"http://www.myscript.com/a\"></script>"+((Global)getActivity().getApplicationContext()).getSpecificationList().getSpecification().get(0).getDescription()+"</body></html>";


        binding.textview.loadData(webData, "text/html", "UTF-8");


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            binding.textview.setText(Html.fromHtml(((Global)getActivity().getApplicationContext()).getSpecificationList().getSpecification().get(0).getDescription(), Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            binding.textview.setText(Html.fromHtml(((Global)getActivity().getApplicationContext()).getSpecificationList().getSpecification().get(0).getDescription()));
//        }


    }
}