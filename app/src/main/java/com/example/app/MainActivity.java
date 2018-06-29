package com.example.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.creater.annotation.CompoundDrawable;
import com.creater.annotation.Image;
import com.creater.annotation.Listener;
import com.creater.annotation.Margin;
import com.creater.annotation.NewView;
import com.creater.annotation.Padding;
import com.creater.annotation.Text;
import com.creater.api.ViewCreater;

public class MainActivity extends Activity {

    @NewView(clip = false,
            bgId = R.drawable.bg)
    FrameLayout mainLayout = null;

    @NewView(width = 20, height = 200,
            parent = "mainLayout",
            bgcolor = Color.RED,
            margin = @Margin(left = 50, top = 200),
            padding = @Padding(left = 20, right = 20),
            listener = @Listener(focusListener = "focusChangeListener"))
    @Image(scaleType = ImageView.ScaleType.CENTER)
    ImageView img = null;

    @NewView(width = 400, height = 80,
            parent = "mainLayout",
            select = true,
            bgId = R.drawable.bg,
            margin = @Margin(top = 300, left = 500),
            padding = @Padding(left = 40))
    @Text(text = "测试",
            marquee = true,
            textColor = Color.BLUE,
            gravity = Gravity.CENTER_VERTICAL,
            compoundDrawable = @CompoundDrawable(left = R.drawable.ic_launcher_round, padding = 80))
    TextView textView = null;

    @NewView(width = 400, height = 80,
            parent = "mainLayout",
            select = true,
            padding = @Padding(left = 30),
            margin = @Margin(left = 500, top = 600),
            bgcolorId = R.color.bg)
    @Text(textId = R.string.app_name,
            textSize = 24,
            marquee = true,
            gravity = Gravity.CENTER,
            textColor = Color.WHITE)
    TextView editText = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewCreater.newView(this, this);
        setContentView(mainLayout);
    }

    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

        }
    };
}
