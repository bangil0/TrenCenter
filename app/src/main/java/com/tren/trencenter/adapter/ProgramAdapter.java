package com.tren.trencenter.adapter;

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
import com.tren.trencenter.R;
import com.tren.trencenter.helper.CircleTransform;
import com.tren.trencenter.model.Program;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 28/08/18.
 */

public class ProgramAdapter extends ArrayAdapter<Program> {

    private String base = "http://156.67.221.225/voting/dashboard/save/foto_program/";

    private Context context;
    private List<Program> programList = new ArrayList<>();

    public ProgramAdapter(Context context, ArrayList<Program> list) {
        super(context, 0, list);
        this.context = context;
        this.programList = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.program_kerja_item,parent,false);

        Program currentProgram = programList.get(position);

        TextView title = (TextView) listItem.findViewById(R.id.title);
        title.setText(currentProgram.getTitle());

        TextView location = (TextView) listItem.findViewById(R.id.location);
        location.setText("Lokasi: " + currentProgram.getLocation());

        TextView dateStart = (TextView) listItem.findViewById(R.id.tanggalMulai);
        dateStart.setText(currentProgram.getDate());

        ImageView imageView = (ImageView) listItem.findViewById(R.id.logo);
        String url = base + "/" + currentProgram.getImage();

        Glide.with(getContext()).load(url)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        return listItem;
    }
}
