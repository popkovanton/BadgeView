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
        ImageView testIcon2 = findViewById(R.id.testIcon2);
        ImageView testIcon3 = findViewById(R.id.testIcon3);
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
        BadgeFactory.create(this)
                .setBadgeBackground(Color.GREEN)
                .setTextColor(Color.BLACK)
                .setTextSize(16)
                .setBadgeShape(BadgeView.SHAPE_RECTANGLE)
                .setBadgeGravity(Gravity.LEFT | Gravity.TOP)
                .setBadgeHeight(20)
                .setBadgeWidth(20)
                .setSpace(2, 2)
                .setBadgeValue(1)
                .setDrawForward(true)
                .bind(testIcon2);
        BadgeFactory.create(this)
                .setBadgeBackground(Color.GRAY)
                .setTextColor(Color.CYAN)
                .setTextSize(16)
                .setBadgeShape(BadgeView.SHAPE_OVAL)
                .setBadgeGravity(Gravity.RIGHT | Gravity.BOTTOM)
                .setBadgeHeight(20)
                .setBadgeWidth(30)
                .setSpace(2, 2)
                .setBadgeValue(1)
                .setDrawForward(true)
                .bind(testIcon3);
    }
}
