package com.popkovanton.badgeview;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;

import com.popkovanton.badgeviewlib.BadgeFactory;
import com.popkovanton.badgeviewlib.BadgeView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBadgeView();
    }

    private void initBadgeView() {
        ImageView testIcon = findViewById(R.id.testIcon);
        BadgeFactory.create(this)
                .setBadgeBackground(Color.BLUE)
                .setTextColor(Color.WHITE)
                .setTextSize(16)
                .setBadgeShape(BadgeView.SHAPE_CIRCLE)
                .setBadgeGravity(Gravity.LEFT | Gravity.BOTTOM)
                .setBadgeHeight(20)
                .setBadgeWidth(20)
                .setSpace(2, 2)
                .setBadgeValue(1)
                .setDrawForward(true)
                .bind(testIcon);
    }
}
