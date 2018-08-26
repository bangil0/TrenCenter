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

import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 26/08/18.
 */

public class MessageAdapter extends ArrayAdapter<Message> {

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

        Message currentMessage = messageList.get(position);

        ImageView image = (ImageView)listItem.findViewById(R.id.image);
        image.setImageResource(currentMessage.getImage());

        TextView title = (TextView) listItem.findViewById(R.id.title);
        title.setText(currentMessage.getTitle());

        TextView release = (TextView) listItem.findViewById(R.id.description);
        release.setText(currentMessage.getDescription());

        return listItem;
    }
}
