package com.bmc.trencenter.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bmc.trencenter.R;
import com.bmc.trencenter.model.PlatformModel;

import java.util.List;

/**
 * Created by root on 13/10/18.
 */

public class PlatformAdapter extends RecyclerView.Adapter<PlatformAdapter.MyViewHolder> {

    private List<PlatformModel> platformList;
    private Context context;

    public PlatformAdapter(List<PlatformModel> platformList, Context context) {
        this.platformList = platformList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.platform_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PlatformModel platform = platformList.get(position);
        holder.platform.setText(platform.getPlatform());
    }

    @Override
    public int getItemCount() {
        return platformList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView platform;
        public LinearLayout background, foreground;

        public MyViewHolder(View view) {
            super(view);
            platform = (TextView) view.findViewById(R.id.platform);
            background = (LinearLayout) view.findViewById(R.id.view_background);
            foreground = (LinearLayout) view.findViewById(R.id.view_foreground);
        }

    }

}
