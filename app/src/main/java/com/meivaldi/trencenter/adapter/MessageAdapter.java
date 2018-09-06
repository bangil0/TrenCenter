package com.meivaldi.trencenter.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
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

public class MessageAdapter extends ArrayAdapter<Message> {

    private SQLiteHandler db;

    private Context context;
    private List<Message> messageList = new ArrayList<>();

    public MessageAdapter(@NonNull Context context, @LayoutRes ArrayList<Message> list) {
        super(context, 0, list);
        this.context = context;
        this.messageList = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);

        db = new SQLiteHandler(getContext());
        HashMap<String, String> user = db.getUserDetails();
        String tipe = user.get("type");

        Message currentMessage = messageList.get(position);

        ImageView image = (ImageView)listItem.findViewById(R.id.image);
        Glide.with(getContext()).load(currentMessage.getImage())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);

        TextView title = (TextView) listItem.findViewById(R.id.title);
        title.setText(currentMessage.getTitle());

        TextView release = (TextView) listItem.findViewById(R.id.description);
        release.setText(currentMessage.getDate());

        return listItem;
    }
}
