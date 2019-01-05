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
import com.tren.trencenter.model.Caleg;

import java.util.List;

/**
 * Created by root on 27/09/18.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private Context context;
    private List<Caleg> calegList;

    public UserAdapter(Context context, List<Caleg> calegList) {
        this.context = context;
        this.calegList = calegList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Caleg current = calegList.get(position);
        holder.nama.setText(current.getNama());

        Glide.with(context).load(current.getImage())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.foto);
    }

    @Override
    public int getItemCount() {
        return calegList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nama;
        private ImageView foto;

        public MyViewHolder(View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.namaCaleg);
            foto = itemView.findViewById(R.id.calegImage);
        }
    }

    /*
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
    }*/
}
