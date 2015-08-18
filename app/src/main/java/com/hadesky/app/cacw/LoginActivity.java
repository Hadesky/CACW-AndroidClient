package com.hadesky.app.cacw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CircularImage icon = (CircularImage) findViewById(R.id.login_icon);
        icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));

        initActionBar();
        initEditText();

        //TODO:临时改
        SharedPreferences preferences = getSharedPreferences("count",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("count", 1);
        editor.apply();
    }

    private void initEditText() {
        EditText editText = (EditText) findViewById(R.id.login_username);
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("");
        }

    }


    public void login(View view) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
