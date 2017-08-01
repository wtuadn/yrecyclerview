package com.wtuadn.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wtuadn.demo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_list:
                startActivity(new Intent(getApplicationContext(), ListAdapterActivity.class));
                break;
            case R.id.btn_cursor:
                startActivity(new Intent(getApplicationContext(), CursorAdapterActivity.class));
        }
    }
}
