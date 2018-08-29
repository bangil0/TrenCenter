package com.meivaldi.trencenter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.meivaldi.trencenter.R;
import com.meivaldi.trencenter.fragment.AccountFragment;

public class ChangeUsername extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText username;
    private Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        username = (EditText) findViewById(R.id.newUsername);
        change = (Button) findViewById(R.id.changeUSername);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AccountFragment.class));
                finish();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUsername();
            }
        });

    }

    private void updateUsername() {
        String newUsername = username.getText().toString();


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
