package com.meivaldi.trencenter.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.helper.CircleTransform;
import com.meivaldi.trencenter.model.Logistik;
import com.meivaldi.trencenter.model.Program;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 11/09/18.
 */

public class LogistikAdapter extends ArrayAdapter<Logistik>{

    private Context context;
    private List<Logistik> logistikList = new ArrayList<>();

    private String base = "http://156.67.221.225/trencenter/voting/dashboard/save/foto_logistik/";

    public LogistikAdapter(Context context, List<Logistik> logistikList) {
        super(context, 0, logistikList);
        this.context = context;
        this.logistikList = logistikList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.logistic_list,parent,false);

        Logistik currentLogistik = logistikList.get(position);

        TextView title = (TextView) listItem.findViewById(R.id.title);
        title.setText(currentLogistik.getTitle());

        TextView location = (TextView) listItem.findViewById(R.id.location);
        location.setText("Lokasi: " + currentLogistik.getLokasi());

        TextView dateStart = (TextView) listItem.findViewById(R.id.tanggalMulai);
        dateStart.setText(currentLogistik.getDate());

        ImageView imageView = (ImageView) listItem.findViewById(R.id.logo);
        String url = base + currentLogistik.getImage();

        Glide.with(getContext()).load(url)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        return listItem;
    }
}
