package com.example.biztechbytes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    List<SliderItems> sliderItems;
    LayoutInflater mLayoutInflater;
    Context context;
    ArrayList<String> titles;
    ArrayList<String> desc;
    ArrayList<String> newslinks;
    ArrayList<String> heads;
    VerticalViewPager verticalViewPager;

    public ViewPagerAdapter(Context context, List<SliderItems> sliderItems, ArrayList<String> titles, ArrayList<String> desc,
                            ArrayList<String> newslinks, ArrayList<String> heads, VerticalViewPager verticalViewPager) {
        this.context = context;
        this.sliderItems = sliderItems;
        this.titles=titles;
        this.desc=desc;
        this.newslinks=newslinks;
        this.heads=heads;
        this.verticalViewPager=verticalViewPager;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return sliderItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_container, container, false);
        ImageView imageView = itemView.findViewById(R.id.imageView);
        ImageView imageview2 = itemView.findViewById(R.id.imageView2);
        TextView title = itemView.findViewById(R.id.headline);
        TextView desctv = itemView.findViewById(R.id.desc);
        TextView head = itemView.findViewById(R.id.head);

        title.setText(titles.get(position));
        desctv.setText(desc.get(position));
        head.setText(heads.get(position));

        Glide.with(context)
                .load(sliderItems.get(position).getImage())
                .centerCrop()
                .into(imageView);

        Glide.with(context)
                .load(sliderItems.get(position).getImage())
                .centerCrop()
                .override(12,12)
                .into(imageview2);

        container.addView(itemView);
        return itemView;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
