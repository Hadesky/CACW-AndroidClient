package com.hadesky.app.cacw;

import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActionBar();
        setupTabLayout();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.main_navigation);
        if (navigationView != null) {
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, getNavMenuItemData(), R.layout.nav_menu_layout, new String[]{"nav_img", "nav_title"}, new int[]{R.id.nav_img, R.id.nav_title});
            ListView listView = (ListView) findViewById(R.id.nav_menu_items);
            listView.setAdapter(simpleAdapter);

            setupDrawerContent(navigationView);
        }
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
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
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

    private List<Map<String,Object>>getNavMenuItemData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("nav_img", R.drawable.ic_home_black_36dp);
        map.put("nav_title", "首页");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("nav_img", R.drawable.ic_alarm_on_black_36dp);
        map.put("nav_title", "提醒");
        list.add(map);
        return list;
    }
}
