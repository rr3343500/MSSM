package com.root.mssm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.root.mssm.List.List.home.Bottomslider;
import com.root.mssm.R;
import com.root.mssm.service.Config;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterBottomSlider extends SliderViewAdapter<AdapterBottomSlider.SliderAdapterVH> {

    private Context context;
    private List<Bottomslider> mSliderItems = new ArrayList<>();

    public AdapterBottomSlider(Context context, List<Bottomslider> mSliderItems) {
        this.context = context;
        this.mSliderItems=  mSliderItems;
    }

    public void renewItems(List<Bottomslider> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(Bottomslider sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        context= parent.getContext();
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        Glide.with(context)
                .load(Config.Image_URL+ mSliderItems.get(position).getPhoto())
                .centerCrop()
                .placeholder(R.drawable.image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.imageViewBackground);
        viewHolder.itemView.setOnClickListener(v -> {

        });
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    public  class SliderAdapterVH extends ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }

}