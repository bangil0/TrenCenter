package com.tren.trencenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.tren.trencenter.R;
import com.tren.trencenter.activity.DetailBerita;
import com.tren.trencenter.helper.CustomVolleyRequest;
import com.tren.trencenter.helper.SliderUtils;

import java.util.List;

/**
 * Created by root on 07/09/18.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<SliderUtils> sliderImg;
    private List<String> headlineList, sourceList;
    private ImageLoader imageLoader;

    public ViewPagerAdapter(List<SliderUtils> sliderImg, List<String> headlineList, List<String> sourceList,
                            Context context){
        this.context = context;
        this.sliderImg = sliderImg;
        this.headlineList = headlineList;
        this.sourceList = sourceList;
    }

    @Override
    public int getCount() {
        return sliderImg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.berita_item, null);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        ImageView background = (ImageView) view.findViewById(R.id.background);
        TextView headLine = (TextView) view.findViewById(R.id.headline);
        TextView sumber = (TextView) view.findViewById(R.id.sumber);

        SliderUtils utils = sliderImg.get(position);
        headLine.setText(headlineList.get(position));
        sumber.setText("Sumber: " + sourceList.get(position));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailBerita.class);
                if(position == 0){
                    intent.putExtra("INDEX", position);
                    context.startActivity(intent);

                    return;
                } else if(position == 1){
                    intent.putExtra("INDEX", position);
                    context.startActivity(intent);

                    return;
                } else if(position == 2){
                    intent.putExtra("INDEX", position);
                    context.startActivity(intent);

                    return;
                } else if(position == 3){
                    intent.putExtra("INDEX", position);
                    context.startActivity(intent);

                    return;
                }
            }
        });

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(utils.getSliderImageUrl(), ImageLoader.getImageListener(image, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        imageLoader.get(utils.getSliderImageUrl(), ImageLoader.getImageListener(background, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

}
