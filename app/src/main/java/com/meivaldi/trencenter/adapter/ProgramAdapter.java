package com.meivaldi.trencenter.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.model.Message;
import com.meivaldi.trencenter.model.Program;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 28/08/18.
 */

public class ProgramAdapter extends ArrayAdapter<Program> {

    private Context context;
    private List<Program> programList = new ArrayList<>();

    public ProgramAdapter(Context context, ArrayList<Program> list) {
        super(context, 0, list);
        this.context = context;
        this.programList = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.program_kerja_item,parent,false);

        Program currentProgram = programList.get(position);

        ImageView image = (ImageView) listItem.findViewById(R.id.logo);
        try {
            URL url = new URL(currentProgram.getImage());
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            image.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView title = (TextView) listItem.findViewById(R.id.title);
        title.setText(currentProgram.getTitle());

        TextView location = (TextView) listItem.findViewById(R.id.location);
        location.setText(currentProgram.getLocation());

        TextView dateStart = (TextView) listItem.findViewById(R.id.tanggalMulai);
        dateStart.setText(currentProgram.getDate());

        return listItem;
    }
}
