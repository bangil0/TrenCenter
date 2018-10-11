package com.bmc.trencenter.adapter;

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
import com.bmc.trencenter.R;
import com.bmc.trencenter.helper.CircleTransform;
import com.bmc.trencenter.model.BeritaModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 26/09/18.
 */

public class PartnershipAdapter extends ArrayAdapter<BeritaModel> {

    private String base = "http://156.67.221.225/voting/dashboard/save/foto_partnership/";

    private Context context;
    private List<BeritaModel> beritaList = new ArrayList<>();

    public PartnershipAdapter(Context context, ArrayList<BeritaModel> list) {
        super(context, 0, list);
        this.context = context;
        this.beritaList = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.list_berita,parent,false);

        BeritaModel currentBerita = beritaList.get(position);

        TextView title = (TextView) listItem.findViewById(R.id.title);
        title.setText(currentBerita.getTitle());

        ImageView imageView = (ImageView) listItem.findViewById(R.id.logo);
        String url = base + "/" + currentBerita.getFoto();

        Glide.with(getContext()).load(url)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        return listItem;
    }
}

