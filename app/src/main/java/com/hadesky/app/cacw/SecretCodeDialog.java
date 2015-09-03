package com.hadesky.app.cacw;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * 验证码Dialog
 * Created by 45517 on 2015/8/30.
 */
public class SecretCodeDialog extends Dialog {

    public interface OnBackListener {
        void onBack(String SecretCode);
    }

    private EditText editText;
    private Drawable mSecretCodeDrawable;
    private  OnBackListener onBackListener;

    protected SecretCodeDialog(Context context, Drawable drawable, OnBackListener onBackListener) {
        super(context, true, null);
        this.mSecretCodeDrawable = drawable;
        this.onBackListener = onBackListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_secret_code);
        setTitle("");
        editText = (EditText) findViewById(R.id.secret_code_edittext);
        Button button = (Button) findViewById(R.id.secret_code_bt);
        ImageView imageView = (ImageView) findViewById(R.id.secret_code_img);

        imageView.setImageDrawable(mSecretCodeDrawable);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackListener.onBack(editText.getText().toString());
            }
        });
    }
}
