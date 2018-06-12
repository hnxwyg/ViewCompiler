package com.example.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.creater.annotation.Listener;
import com.creater.annotation.Margin;
import com.creater.annotation.NewView;
import com.creater.annotation.Padding;
import com.creater.api.ViewCreater;

public class MainActivity extends Activity {

    @NewView()
    FrameLayout mainLayout = null;
    @NewView(width = 200, height = 200,
            parent = "mainLayout",
            bgcolor = Color.RED,
            focusable = true,
            margin = @Margin(left = 50, top = 200),
            padding = @Padding(left = 20, right = 20),
            listener = @Listener(focusListener = "focusChangeListener"))
    ImageView img = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewCreater.newView(this,this);
        setContentView(mainLayout);
    }

    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

        }
    };
}
