package com.hadesky.app.cacw;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    public static final String URL = "http://jwgldx.gdut.edu.cn/default2.aspx";
    public static final int READ_TIMEOUT = 10000;
    public static final int CONNECT_TIMEOUT = 15000;


    private SessionManagement session;
    private EditText mUsername, mPassword;
    private ImageButton mPwButton;
    private Button mLoginButton;
    private boolean mIsPwVisitable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManagement(getApplicationContext());


        CircularImage icon = (CircularImage) findViewById(R.id.login_icon);
        icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));

        initActionBar();
        initEditText();
        initLoginBt();
        initPwButton();

    }

    private void initLoginBt() {
        mLoginButton = (Button) findViewById(R.id.login_login_button);
    }

    private void initPwButton() {
        mPwButton = (ImageButton) findViewById(R.id.login_password_eye);
        mPwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIsPwVisiable(!mIsPwVisitable);
                if (mIsPwVisitable) {
                    mPwButton.setSelected(false);
                    mIsPwVisitable = false;
                    setIsPwVisiable(false);
                } else {
                    mPwButton.setSelected(true);
                    mIsPwVisitable = true;
                    setIsPwVisiable(true);
                }
            }
        });
    }

    private void setIsPwVisiable(boolean visiable) {
        if (visiable) {
            mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            //定位到最后
            CharSequence text = mPassword.getText();
            if (text != null) {
                Spannable spanText = (Spannable) text;
                Selection.setSelection(spanText, text.length());
            }
        } else {
            mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            //定位到最后
            CharSequence text = mPassword.getText();
            if (text != null) {
                Spannable spanText = (Spannable) text;
                Selection.setSelection(spanText, text.length());
            }
        }
    }


    private void initEditText() {
        mUsername = (EditText) findViewById(R.id.login_username);
        mPassword = (EditText) findViewById(R.id.login_password);

        //设置IME输入法的监听
        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    mLoginButton.performClick();
                }
                return true;
            }
        });
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        //仅在第一次登录时使用返回箭头
        SharedPreferences preferences = getSharedPreferences("runCount", MODE_PRIVATE);
        int count = preferences.getInt("runCount", 0);
        if (count == 1) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        actionBar.setTitle("");

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

    private void login(final String username, final String password) {
        //临时使用test作为账号和密码
        ProgressDialog progressDialog = new ProgressDialog(this);

        LoginTask loginTask = new LoginTask(progressDialog);
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
        ProgressDialog progressDialog;

        public LoginTask(ProgressDialog progressDialog) {
            this.progressDialog = progressDialog;
        }

        @Override
        protected Boolean doInBackground(String... parms) {
            try {
                return loginAttempt(parms);
            } catch (Exception e) {
                return false;
            }
        }


        private Boolean loginAttempt(String... params) throws IOException, JSONException {
//            //TODO 先获取viewState,为登录教务系统做准备,后期可删
//            final String viewState = getViewState();
//
//            InputStream is = null;
//            String result = "";
//            HttpURLConnection connection = null;
//
//            try {
//                URL url = new URL(params[0]);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.setReadTimeout(READ_TIMEOUT);
//                connection.setConnectTimeout(CONNECT_TIMEOUT);
//                connection.setDoInput(true);
//                connection.setDoOutput(true);
//                connection.setUseCaches(false);
//                connection.setRequestMethod("POST");
//
//                //没有添加验证码
//                connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                connection.addRequestProperty("Accept", "text/html, application/xhtml+xml, image/jxr, */*");
//                connection.addRequestProperty("Accept-Language", "zh-Hans-CN,zh-Hans;q=0.5");
//                connection.addRequestProperty("Content-Language", "gb2312");
//                connection.addRequestProperty("Accept-Encoding", "gzip, deflate");
//                connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
//                connection.addRequestProperty("Pragma", "no-cache");
//
//                StringBuilder postString = new StringBuilder();
//                postString.append("__VIEWSTATE").append("=").append(URLEncoder.encode(viewState, "gb2312")).append("&");
//                postString.append("txtUserName").append("=").append(URLEncoder.encode(params[1], "gb2312")).append("&");
//                postString.append("TextBox2").append("=").append(URLEncoder.encode(params[1], "gb2312")).append("&");
//                postString.append("txtSecretCode").append("=").append("1234").append("&");
//                postString.append("RadioButtonList1").append("=").append(URLEncoder.encode("学生", "gb2312")).append("&");
//                postString.append("Button1").append("=").append("").append("&");
//                postString.append("lbLanguage").append("=").append("").append("&");
//                postString.append("hidPdrs").append("=").append("").append("&");
//                postString.append("hidsc").append("=").append("");
//
//                Log.d("TEST2222", "The postString is " + postString.toString());
//
//                byte[] bytes = postString.toString().getBytes("gb2312");
//                connection.getOutputStream().write(bytes);
//                //start
//                is = connection.getInputStream();
//
//                int response = connection.getResponseCode();
//                if (response != 200) {
//                    return false;
//                }
//
//                InputStreamReader isr = new InputStreamReader(is, "gb2312");
//                BufferedReader bufferedReader = new BufferedReader(isr);
//                String inputLine;
//                while ((inputLine = bufferedReader.readLine()) != null) {
//                    result += inputLine + "\n";
//                }

                //getPopString(result);

                return true;
//            } catch (IOException e) {
//                return false;
//            } finally {
//                if (is != null) {
//                    try {
//                        is.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (connection != null) {
//                    connection.disconnect();
//                }
//            }
        }


        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("登录中...");
            progressDialog.show();
        }


        @Override
        protected void onPostExecute(Boolean eSueecess) {
            if (eSueecess) {
                session.createLoginSession("一张树叶", "455173472@qq.com");
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                progressDialog.cancel();
                startActivity(intent);
            } else {
                progressDialog.cancel();
                //登录失败
                Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
            }
        }


        //TODO:以下均为专门为登录正方教务设计的方法
        private String getViewState() {

            InputStream is = null;
            String result = "";
            HttpURLConnection connection = null;


            String viewState = "";
            try {
                URL url = new URL(URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECT_TIMEOUT);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestMethod("GET");
                //start
                is = connection.getInputStream();

                int response = connection.getResponseCode();
                if (response != 200) {
                    return "";
                }

                InputStreamReader isr = new InputStreamReader(is, "gb2312");
                BufferedReader bufferedReader = new BufferedReader(isr);
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    result += inputLine + "\n";
                }


                //正则表达式获取viewState
                Pattern p = Pattern.compile("<input type=\"hidden\" name=\"__VIEWSTATE\" value=\"(.+?)\" />");
                Matcher matcher = p.matcher(result);
                StringBuilder buffer = new StringBuilder();
                while (matcher.find()) {
                    buffer.append(matcher.group(1));
                }
                return buffer.toString();

            } catch (IOException e) {
                return "";
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        //获取弹窗内容
        private String getPopString(String result) {
            Pattern p = Pattern.compile("alert(.+?);");
            Matcher matcher = p.matcher(result);
            StringBuilder buffer = new StringBuilder();
            while (matcher.find()) {
                buffer.append(matcher.group(1));
            }
            Log.d("TEST", "弹窗内容为" + buffer.toString());
            return buffer.toString();
        }
    }
}
