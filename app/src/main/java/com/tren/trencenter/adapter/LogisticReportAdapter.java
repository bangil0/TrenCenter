package com.tren.trencenter.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tren.trencenter.R;
import com.tren.trencenter.helper.CircleTransform;
import com.tren.trencenter.model.LogisticReport;

import java.util.List;

/**
 * Created by root on 29/09/18.
 */

public class LogisticReportAdapter extends RecyclerView.Adapter<LogisticReportAdapter.MyViewHolder> {

    private Context context;
    private List<LogisticReport> list;

    public LogisticReportAdapter(Context context, List<LogisticReport> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.logistic_report, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final LogisticReport currentLogistic = list.get(position);
        holder.title.setText(currentLogistic.getName());
        holder.total.setText(currentLogistic.getTotal());

        Glide.with(context).load("http://156.67.221.225/voting/dashboard/save/foto_logistik/"
                + currentLogistic.getImage())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .override(512, 160)
                .into(holder.logo);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView logo;
        private TextView title, total;

        public MyViewHolder(View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.logo);
            title = itemView.findViewById(R.id.title);
            total = itemView.findViewById(R.id.total);
        }
    }

    /*public LogisticReportAdapter(Context context, List<LogisticReport> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.logistic_report,parent,false);

        LogisticReport currentLogistik = list.get(position);

        TextView title = (TextView) listItem.findViewById(R.id.title);
        title.setText(currentLogistik.getName());

        TextView total = (TextView) listItem.findViewById(R.id.total);
        total.setText(currentLogistik.getTotal());

        ImageView imageView = (ImageView) listItem.findViewById(R.id.logo);
        String url = "http://156.67.221.225/voting/dashboard/save/foto_logistik/" + currentLogistik.getImage();

        Glide.with(getContext()).load(url)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .override(512, 160)
                .into(imageView);

        return listItem;
    }*/
}
