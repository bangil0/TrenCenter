package com.meivaldi.trencenter.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.helper.CircleTransform;
import com.meivaldi.trencenter.helper.SQLiteHandler;
import com.meivaldi.trencenter.model.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 26/08/18.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private Context context;
    private List<Message> messageList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, release;
        private ImageView foto;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            release = (TextView) view.findViewById(R.id.description);
            foto = (ImageView) view.findViewById(R.id.image);
        }

    }

    public MessageAdapter(Context context, ArrayList<Message> list) {
        this.context = context;
        this.messageList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.title.setText(message.getTitle());
        holder.release.setText(message.getDate());

        Glide.with(context).load(message.getImage())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.foto);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void removeItem(int position) {
        messageList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Message message, int position) {
        messageList.add(position, message);
        notifyItemInserted(position);
    }
}
