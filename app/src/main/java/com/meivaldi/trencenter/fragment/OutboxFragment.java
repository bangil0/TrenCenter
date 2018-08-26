package com.meivaldi.trencenter.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.adapter.MessageAdapter;
import com.meivaldi.trencenter.model.Message;

import java.util.ArrayList;

public class OutboxFragment extends Fragment {

    private ListView listView;
    private MessageAdapter mAdapter;
    FloatingActionButton button;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_outbox, container, false);
        button = (FloatingActionButton) rootView.findViewById(R.id.outbox_send);

        listView = (ListView) rootView.findViewById(R.id.outbox_list);
        ArrayList<Message> messagesList = new ArrayList<>();

        messagesList.add(new Message("Suryono Prapto", R.string.description, R.drawable.boss));
        messagesList.add(new Message("Adi Susanto", R.string.description, R.drawable.boss));
        messagesList.add(new Message("Bambang", R.string.description, R.drawable.boss));
        messagesList.add(new Message("Wiro Sableng", R.string.description, R.drawable.boss));
        messagesList.add(new Message("Joko", R.string.description, R.drawable.boss));
        messagesList.add(new Message("Kirana Ayu", R.string.description, R.drawable.boss));
        messagesList.add(new Message("Saraswati", R.string.description, R.drawable.boss));

        mAdapter = new MessageAdapter(getContext(), messagesList);
        listView.setAdapter(mAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Masih Dalam Pengembangan", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}