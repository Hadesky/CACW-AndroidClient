package com.hadesky.app.cacw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    //页面根布局
    private DrawerLayout mDrawerLayout;
    //左上角的汉堡图标
    private ActionBarDrawerToggle mDrawerToggle;
    //TabLayout
    private TabLayout mTabLayout;
    //三个ViewPager
    private ViewPager mViewPager;
    //登录时储存的session
    private SessionManagement session;
    //退出软件时的Toast
    private Toast exitToast;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManagement(getApplicationContext());

        checkIfFirstRun();
        checkIfLogin();

        //初始化退出的Toast，并不需要Show出来
        exitToast = Toast.makeText(this, "再按返回键退出", Toast.LENGTH_SHORT);

        initActionBar();
        setupTabLayout();
        //初始化侧边抽屉
        NavigationView navigationView = (NavigationView) findViewById(R.id.main_navigation);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    /**
     * 检查程序是否已经登录了，如果没有则返回Login界面
     */
    private void checkIfLogin() {
        if (!isFirstRun()) {
            if (!session.checkLogin()) {
                this.finish();
            }
        }
    }

    /**
     * 检查是否第一次运行程序
     */
    private void checkIfFirstRun() {
        if (isFirstRun()) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    /**
     *检查是否第一次运行程序，返回值表示是否第一次运行
     * @return  true表示第一次运行
     */
    private boolean isFirstRun() {
        SharedPreferences preferences = getSharedPreferences("runCount", MODE_PRIVATE);
        int count = preferences.getInt("runCount", 0);
        return count == 0;
    }

    /**
     * 初始化TabLayout
     */
    private void setupTabLayout() {
        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.addTab(mTabLayout.newTab().setText("任务"));
        mTabLayout.addTab(mTabLayout.newTab().setText("私信"));
        mTabLayout.addTab(mTabLayout.newTab().setText("团队"));
    }

    /**
     * 初始化ActionBar，替换成Toolbar
     */
    private void initActionBar() {
        //replace actionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.toggle_open, R.string.toggle_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    /**
     * 初始化侧边栏，对侧边栏选项设置listener
     * @param navigationView 非空
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.nav_home:
                        menuItem.setChecked(true);
                        break;
                    case R.id.action_exit:
                        finish();
                        break;
                    case R.id.action_settings:
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), SettingActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        menuItem.setChecked(true);
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        //设置头像
        CircularImage circularImage = (CircularImage) findViewById(R.id.user_photo);
        //TODO 暂时使用的是临时的头像，需要修改
        loadUserImage(R.drawable.a, circularImage);
    }

    /**
     * TODO 开启一个线程进行加载头像，需要修改
     * @param resId 暂时使用的是来着drawable的头像，届时应该换成session里的头像，
     * @param circularImage 头像所在的CircularImageView
     */
    private void loadUserImage(int resId, CircularImage circularImage) {
        Bitmap mPlaceHolderBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_user_image_192);
        if (BitmapWorkerTask.cancelPotentialWork(resId, circularImage)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(circularImage, getResources());
            final BitmapWorkerTask.AsyncDrawable defUserImg = new BitmapWorkerTask.AsyncDrawable(getResources(), mPlaceHolderBitmap, task);
            circularImage.setImageDrawable(defUserImg);
            task.execute(resId);
        }
    }

    /**
     * 在生命周期的onPostCreate时更新左上角汉堡图标的状态
     * @param savedInstanceState 你懂得
     * @param persistentState 你懂得
     */
    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 设置菜单响应内容
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                startSettingActivity();
                return true;
            case R.id.action_logout:
                session.logoutUser();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 打开Setting界面
     */
    private void startSettingActivity() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), SettingActivity.class);
        startActivity(intent);
    }

    /**
     * 先检测是否打开了抽屉，打开则先关闭，否则双击退出
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            if (exitToast.getView().getParent() == null) {
                exitToast.show();
            } else {
                super.onBackPressed();
            }
        }
    }

    /**
     * onDestroy的时候增加一次使用记录
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出自动增加一次使用次数
        SharedPreferences preferences = getSharedPreferences("runCount", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        int count = preferences.getInt("runCount", 0);
        editor.putInt("runCount", count + 1);
        editor.apply();
    }

    /**
     * 如果要建立连接，对于HttpUrlConnection有一个旧版本的bug，API12以前会产生BUG，可以调用这个函数来临时阻止BUG
     */
    private void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }
}
