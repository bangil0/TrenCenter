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
import com.meivaldi.trencenter.activity.DetailLogistik;
import com.meivaldi.trencenter.helper.CircleTransform;
import com.meivaldi.trencenter.model.Caleg;

import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by root on 27/09/18.
 */
public class UserAdapter extends ArrayAdapter {
    private Context context;
    private List<Caleg> calegList;

    public UserAdapter(Context context, List<Caleg> calegList){
        super(context, 0, calegList);
        this.context = context;
        this.calegList = calegList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.user,parent,false);

        Caleg currentCaleg = calegList.get(position);

        TextView nama = (TextView) listItem.findViewById(R.id.namaCaleg);
        nama.setText(currentCaleg.getNama());

        ImageView imageView = (ImageView) listItem.findViewById(R.id.calegImage);
        String url = currentCaleg.getImage();

        Glide.with(getContext()).load(url)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        return listItem;
    }
}
