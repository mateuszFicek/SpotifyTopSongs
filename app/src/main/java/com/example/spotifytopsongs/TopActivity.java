package com.example.spotifytopsongs;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

public class TopActivity extends FragmentActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
