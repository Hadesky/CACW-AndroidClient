package com.hadesky.app.cacw;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    public static final String URL = "172.16.1.100";

    private SessionManagement session;
    private EditText mUsername, mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManagement(getApplicationContext());


        CircularImage icon = (CircularImage) findViewById(R.id.login_icon);
        icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));

        initActionBar();
        initEditText();

    }

    private void initEditText() {
        mUsername = (EditText) findViewById(R.id.login_username);
        mPassword = (EditText) findViewById(R.id.login_password);
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


    public void btOnClick(View view) {
        //检查网络状态
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //有网络
            String username = mUsername.getText().toString();
            String password = mPassword.getText().toString();

            if (username.trim().length() > 0) {
                if (password.trim().length() > 0) {
                    login(username, password);
                } else {
                    //没输入密码
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
            } else {
                //没输入用户名
                Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请检查网络！", Toast.LENGTH_SHORT).show();
        }
    }

    private void login(final String username,final String password) {
        //临时使用test作为账号和密码
        LoginTask loginTask = new LoginTask();
        loginTask.execute(URL, username, password);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... parms) {
            try {
                return loginAttempt(parms);
            } catch (Exception e) {
                return false;
            }
        }

//        private Boolean loginAttemp(String... parms) throws IOException,JSONException {
//            try {
//                URL url = new URL(parms[0]);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setReadTimeout(10000);
//                connection.setConnectTimeout(15000);
//                //GET
//                connection.setRequestMethod("GET");
//                connection.setDoOutput(true);
//                connection.setUseCaches(false);
//                connection.addRequestProperty("Accept", "application/json");
//                connection.addRequestProperty("Content-Type", "application/json");
//                JSONObject parm = new JSONObject();
//                parm.put("loginId", parms[1]);
//                parm.put("password", parms[2]);
//                //start
//                connection.connect();
//                int response = connection.getResponseCode();
//                Log.d("Login Tag", "The response is " + response);
//            }
//        }

        private Boolean loginAttempt(String... parms) throws IOException,JSONException {
            try {
                URL url = new URL(parms[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                //start
                connection.connect();
                int response = connection.getResponseCode();
                Log.d("Login Tag", "The response is " + response);
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean eSueecess) {
            if (eSueecess) {
                session.createLoginSession("一张树叶", "455173472@qq.com");
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                //用户账户密码不对
                Toast.makeText(getApplicationContext(), "测试账户密码都是test", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
