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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 45517 on 2015/8/26.
 */
public class SettingAdapter extends RecyclerView.Adapter<SettingViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }

    //菜单文字
    private List<Map<String, Object>> mMenuData;
    //Context
    private Context mContext;
    //渲染器
    private LayoutInflater mInflater;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public SettingAdapter(List<Map<String, Object>> mMenuData, Context mContext) {
        this.mMenuData = mMenuData;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }


    /**
     * 创建ViewHolder
     *
     * @param viewGroup
     * @param i
     * @return
     */
    @Override
    public SettingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_setting_menu, viewGroup, false);
        return new SettingViewHolder(view);
    }

    /**
     * 绑定ViewHolder
     * @param settingViewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(final SettingViewHolder settingViewHolder, final int i) {
        Map map = mMenuData.get(i);
        //因为text数组被我混进去了一些-1，要先判定再进行setText
        if ((int) map.get("text") != -1) {
            String text = mContext.getResources().getString((int)map.get("text"));
            settingViewHolder.textView.setText(text);
        }
        switch ((int)map.get("type")) {
            case SettingActivity.TYPE_NORMAL:
                settingViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(settingViewHolder.itemView, i);
                        }
                    }
                });
                settingViewHolder.switchCompat.setVisibility(View.GONE);
                settingViewHolder.imageView.setVisibility(View.GONE);
                break;
            case SettingActivity.TYPE_SWITCH:
                settingViewHolder.switchCompat.setVisibility(View.VISIBLE);
                settingViewHolder.imageView.setVisibility(View.GONE);
                break;
            case SettingActivity.TYPE_MORE:
                settingViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(settingViewHolder.itemView, i);
                        }
                    }
                });
                settingViewHolder.switchCompat.setVisibility(View.GONE);
                settingViewHolder.imageView.setVisibility(View.VISIBLE);
                break;
            case SettingActivity.TYPE_DECORATION:
                settingViewHolder.itemView.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMenuData.size();
    }
}

class SettingViewHolder extends RecyclerView.ViewHolder {

    protected TextView textView;
    protected SwitchCompat switchCompat;
    protected ImageView imageView;

    public SettingViewHolder(View itemView) {
        super(itemView);

        textView = (TextView) itemView.findViewById(R.id.setting_text_view);
        //一个功能切换器
        switchCompat = (SwitchCompat) itemView.findViewById(R.id.setting_switch);
        //右箭头
        imageView = (ImageView) itemView.findViewById(R.id.setting_image_view);
    }
}