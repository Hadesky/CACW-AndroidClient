package com.hadesky.app.cacw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private boolean isReadyToExit = false;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkIfFirstRun();

        initActionBar();
        setupTabLayout();
        NavigationView navigationView = (NavigationView) findViewById(R.id.main_navigation);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    private void checkIfFirstRun() {
        SharedPreferences preferences = getSharedPreferences("count",MODE_PRIVATE);
        int count = preferences.getInt("count", 0);

        if (true) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            this.finish();
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("count", ++count);
        editor.apply();
    }

    private void setupTabLayout() {
        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.addTab(mTabLayout.newTab().setText("任务"));
        mTabLayout.addTab(mTabLayout.newTab().setText("私信"));
        mTabLayout.addTab(mTabLayout.newTab().setText("团队"));
    }

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

        loadUserImage(R.drawable.a, circularImage);
    }

    private void loadUserImage(int resId, ImageView imageView) {
        Bitmap mPlaceHolderBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_user_image_192);
        if (BitmapWorkerTask.cancelPotentialWork(resId, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView, getResources());
            final BitmapWorkerTask.AsyncDrawable defUserImg = new BitmapWorkerTask.AsyncDrawable(getResources(), mPlaceHolderBitmap, task);
            imageView.setImageDrawable(defUserImg);
            task.execute(resId);
        }
    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_logout:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else if (isReadyToExit) {
            super.onBackPressed();
        } else {
            isReadyToExit = true;
            Toast.makeText(this, "再次点击返回退出程序!", Toast.LENGTH_SHORT).show();
            Timer exitTimer = new Timer();
            exitTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isReadyToExit = false;
                }
            }, 2000);
        }
    }
}
