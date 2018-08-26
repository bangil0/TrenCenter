package com.meivaldi.trencenter.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.adapter.MessageAdapter;
import com.meivaldi.trencenter.model.Message;

import java.util.ArrayList;

public class OutboxFragment extends Fragment {

    private ListView listView;
    private MessageAdapter mAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_outbox, container, false);

        listView = (ListView) rootView.findViewById(R.id.outbox_list);
        ArrayList<Message> messagesList = new ArrayList<>();

        messagesList.add(new Message(R.string.title, R.string.description, R.drawable.boss));
        messagesList.add(new Message(R.string.title, R.string.description, R.drawable.boss));
        messagesList.add(new Message(R.string.title, R.string.description, R.drawable.boss));
        messagesList.add(new Message(R.string.title, R.string.description, R.drawable.boss));
        messagesList.add(new Message(R.string.title, R.string.description, R.drawable.boss));
        messagesList.add(new Message(R.string.title, R.string.description, R.drawable.boss));
        messagesList.add(new Message(R.string.title, R.string.description, R.drawable.boss));

        mAdapter = new MessageAdapter(getContext(), messagesList);
        listView.setAdapter(mAdapter);

        return rootView;
    }
}