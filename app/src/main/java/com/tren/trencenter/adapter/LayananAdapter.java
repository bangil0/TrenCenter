package com.tren.trencenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tren.trencenter.model.LayananModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tren.trencenter.R;
import com.tren.trencenter.activity.DetailLayanan;

import java.util.List;

/**
 * Created by root on 26/09/18.
 */

public class LayananAdapter extends RecyclerView.Adapter<LayananAdapter.MyViewHolder> {

    private Context context;
    private List<LayananModel> layananList;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layanan_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LayananModel layanan = layananList.get(position);
        holder.title.setText(layanan.getTitle());
        String imageUrl = layanan.getImage();

        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .override(512, 160)
                .into(holder.cardImage);
    }

    @Override
    public int getItemCount() {
        return layananList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView cardImage;
        public View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            title = (TextView) itemView.findViewById(R.id.nama_layanan);
            cardImage = (ImageView) itemView.findViewById(R.id.gambar_layanan);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailLayanan.class);
                    intent.putExtra("INDEX", getAdapterPosition());
                    intent.putExtra("MAIN", true);
                    context.startActivity(intent);
                }
            });
        }

    }

    public LayananAdapter(Context context, List<LayananModel> layananList){
        this.context = context;
        this.layananList = layananList;
    }
}

