package com.hadesky.app.cacw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SettingActivity extends AppCompatActivity {

    //用于表示菜单选项的样式，normal表示既没有右箭头，又没有switch
    public static final int TYPE_NORMAL = 0;
    //用于表示菜单选项的样式，switch表示只有switch
    public static final int TYPE_SWITCH = 1;
    //用于表示菜单选项的样式，more表示只有一个右箭头
    public static final int TYPE_MORE = 2;
    //用于表示菜单选项的样式，more表示只有一个右箭头
    public static final int TYPE_DECORATION = -1;


    private RecyclerView mRecycleView;
    private SettingAdapter mAdapter;
    private List<Map<String, Object>> mMenuData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initActionBar();

        initMenuData();

        initSettingList();
    }

    private void initMenuData() {
        //以下两者确保一一对应
        final int text[] = {
                R.string.setting_general,
                -1,
                R.string.setting_receive_alarm,
                R.string.setting_sound,
                R.string.setting_vibration,
                -1,
                R.string.setting_about,
                R.string.logout};
        final int menutype[] = {
                TYPE_MORE,
                TYPE_DECORATION, TYPE_SWITCH, TYPE_SWITCH, TYPE_SWITCH,
                TYPE_DECORATION, TYPE_NORMAL, TYPE_NORMAL};

        mMenuData = new ArrayList<>();

        for (int i = 0; i < text.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("text", text[i]);
            map.put("type", menutype[i]);
            mMenuData.add(map);
        }
    }

    private void initSettingList() {
        mAdapter = new SettingAdapter(mMenuData, this);
        mRecycleView = (RecyclerView) findViewById(R.id.setting_recyclerView);
        mRecycleView.setAdapter(mAdapter);
        //这里第三个参数好像是设置是否置逆的，设置为False不置逆
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycleView.setLayoutManager(linearLayoutManager);

        //设置分割线
        mRecycleView.addItemDecoration(new SettingDecoration(this));

        //设置监听器
        mAdapter.setOnItemClickListener(new SettingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                switch ((int)mMenuData.get(pos).get("text")) {
                    case R.string.setting_general:
                        Toast.makeText(getApplicationContext(), "打开失败", Toast.LENGTH_SHORT).show();
                        break;
                    case R.string.setting_about:
                        Toast.makeText(getApplicationContext(), "打开失败", Toast.LENGTH_SHORT).show();
                        break;
                    case R.string.logout:
                        Toast.makeText(getApplicationContext(), "退出登录中....", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("");
    }


    /**
     * 自定义分割线
     */
    private class SettingDecoration extends RecyclerView.ItemDecoration {
        private final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };
        //分隔线
        private Drawable mDecoration;


        public SettingDecoration(Context context) {
            mDecoration = context.obtainStyledAttributes(ATTRS).getDrawable(0);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View view = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
                final int top = view.getBottom() + params.bottomMargin;
                final int bottom = top + mDecoration.getIntrinsicHeight();
                mDecoration.setBounds(left, top, right, bottom);
                mDecoration.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(0, 0, 0, mDecoration.getIntrinsicHeight());
        }
    }

}
