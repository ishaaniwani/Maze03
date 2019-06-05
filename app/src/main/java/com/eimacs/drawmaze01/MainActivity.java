package com.eimacs.drawmaze01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    /** Called when the user clicks the Start button */
    public void start(View view)
    {
        Intent intent = new Intent(this, MazeActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Quit button */
    public void quit(View view)
    {
        Intent quitApp = new Intent(Intent.ACTION_MAIN);
        quitApp.addCategory(Intent.CATEGORY_HOME);
        quitApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(quitApp);
    }
}