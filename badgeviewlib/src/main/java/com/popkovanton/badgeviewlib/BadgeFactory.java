package com.popkovanton.badgeviewlib;

import android.content.Context;

public class BadgeFactory {
    public static BadgeView create(Context context) {
        return new BadgeView(context);
    }
}
