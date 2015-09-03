package com.hadesky.app.cacw;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by 45517 on 2015/9/3.
 */
public class ExampleFragment extends Fragment {

    private TextView mTextView;

    public ExampleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_example, container, false);

        mTextView = (TextView) view.findViewById(R.id.example_tv);
        mTextView.setText("欢迎使用蚂蚁团队");

        return view;
    }

}
