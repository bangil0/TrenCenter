package com.meivaldi.trencenter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class InboxFragment extends Fragment {

    private ListView listView;
    private MessageAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);

        listView = (ListView) rootView.findViewById(R.id.inbox_list);
        ArrayList<Message> messagesList = new ArrayList<>();

        messagesList.add(new Message("Suryono Prapto", R.string.description, R.drawable.team));
        messagesList.add(new Message("Adi Susanto", R.string.description, R.drawable.team));
        messagesList.add(new Message("Bambang", R.string.description, R.drawable.team));
        messagesList.add(new Message("Wiro Sableng", R.string.description, R.drawable.team));
        messagesList.add(new Message("Joko", R.string.description, R.drawable.team));
        messagesList.add(new Message("Kirana Ayu", R.string.description, R.drawable.team));
        messagesList.add(new Message("Saraswati", R.string.description, R.drawable.team));

        mAdapter = new MessageAdapter(getContext(), messagesList);
        listView.setAdapter(mAdapter);

        return rootView;
    }
}
