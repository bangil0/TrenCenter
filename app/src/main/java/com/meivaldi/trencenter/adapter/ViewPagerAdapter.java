package com.meivaldi.trencenter.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.helper.CustomVolleyRequest;
import com.meivaldi.trencenter.helper.SliderUtils;

import java.util.List;

/**
 * Created by root on 07/09/18.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<SliderUtils> sliderImg;
    private List<String> headlineList;
    private ImageLoader imageLoader;
    //private Integer[] images = {R.drawable.bank, R.drawable.attach, R.drawable.check_mark, R.drawable.delivery_truck};

    public ViewPagerAdapter(List<SliderUtils> sliderImg, List<String> headlineList, Context context){
        this.context = context;
        this.sliderImg = sliderImg;
        this.headlineList = headlineList;
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
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.berita_item, null);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView headLine = (TextView) view.findViewById(R.id.headline);

        SliderUtils utils = sliderImg.get(position);
        headLine.setText(headlineList.get(position));

        //image.setImageResource(images[position]);
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(utils.getSliderImageUrl(), ImageLoader.getImageListener(image, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

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
