package com.popkovanton.badgeviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;

import static com.popkovanton.badgeviewlib.Utils.dip2px;
import static com.popkovanton.badgeviewlib.Utils.sp2px;

public class BadgeView extends AppCompatImageView {

    public static final int ICON_DRAWABLE = 0;
    public static final int SHAPE_CIRCLE = 1;
    public static final int SHAPE_OVAL = 2;
    public static final int SHAPE_RECTANGLE = 3;
    private Paint numberPaint;
    private Paint backgroundPaint;
    private Paint forwardPaint;
    private int badgeShape = SHAPE_CIRCLE;
    private int defaultTextColor = Color.WHITE;
    private int defaultTextSize;
    private int defaultForwardColor;
    private int defaultBackgroundColor = Color.RED;
    private String showValue = "0";
    private String showValueText = "!";
    private int badgeGravity = Gravity.LEFT | Gravity.BOTTOM;
    private int horizontalSpace = 0;
    private int verticalSpace = 0;
    private int badgeWidth, badgeHeight;
    private float badgeCenterX, badgeCenterY;
    private int left, top, right, bottom;
    private boolean valueIsText = false;
    private boolean drawForward = false;
    private Typeface typeFace;
    private float textBaselineX, textBaselineY;
    private Drawable iconDrawable;

    public BadgeView(Context context) {
        super(context);
        setupPaints();
        setupGravity();
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BadgeView);
        try {
            badgeGravity = a.getInt(R.styleable.BadgeView_bv_badgeGravity, badgeGravity);
            badgeShape = a.getInt(R.styleable.BadgeView_bv_badgeShape, badgeShape);
            badgeWidth = a.getDimensionPixelSize(R.styleable.BadgeView_bv_badgeWidth, badgeWidth);
            badgeHeight = a.getDimensionPixelSize(R.styleable.BadgeView_bv_badgeHeight, badgeHeight);
            horizontalSpace = a.getDimensionPixelSize(R.styleable.BadgeView_bv_horizontalSpace, horizontalSpace);
            verticalSpace = a.getDimensionPixelSize(R.styleable.BadgeView_bv_verticalSpace, verticalSpace);
            defaultTextColor = a.getColor(R.styleable.BadgeView_bv_badgeTextColor, defaultTextColor);
            defaultTextSize = a.getDimensionPixelSize(R.styleable.BadgeView_bv_badgeTextSize, getResources().getDimensionPixelSize(R.dimen.counter_txt_size));
            defaultBackgroundColor = a.getColor(R.styleable.BadgeView_bv_badgeBackColor, defaultBackgroundColor);
            defaultForwardColor = a.getColor(R.styleable.BadgeView_bv_badgeForwardColor, getResources().getColor(R.color.badgeForwardColor));
            iconDrawable = a.getDrawable(R.styleable.BadgeView_bv_iconDrawable);
            if (a.hasValue(R.styleable.BadgeView_bv_badgeFontFamily)) {
                int fontId = a.getResourceId(R.styleable.BadgeView_bv_badgeFontFamily, -1);
                typeFace = ResourcesCompat.getFont(context, fontId);
            }
        } finally {
            a.recycle();
        }
        setupPaints();
        setupGravity();
    }

    private void setupPaints() {
        numberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        numberPaint.setColor(defaultTextColor);
        numberPaint.setStyle(Paint.Style.FILL);
        numberPaint.setTextSize(defaultTextSize);
        numberPaint.setTextAlign(Paint.Align.CENTER);
        numberPaint.setTypeface(typeFace);
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(defaultBackgroundColor);
        backgroundPaint.setStyle(Paint.Style.FILL);

        forwardPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        forwardPaint.setColor(defaultForwardColor);
        forwardPaint.setStyle(Paint.Style.FILL);
    }

    private void setupGravity() {
        if (badgeGravity == (Gravity.RIGHT | Gravity.TOP)) {
            badgeCenterX = getMeasuredWidth() - (badgeWidth / 2f) - horizontalSpace;
            badgeCenterY = (badgeHeight / 2f) + verticalSpace;
            left = getMeasuredWidth() - badgeWidth - horizontalSpace;
            top = verticalSpace;
            right = getMeasuredWidth() - horizontalSpace;
            bottom = badgeHeight + verticalSpace;
        }
        if (badgeGravity == (Gravity.RIGHT | Gravity.BOTTOM)) {
            badgeCenterX = getMeasuredWidth() - (badgeWidth / 2f) - horizontalSpace;
            badgeCenterY = getMeasuredHeight() - (badgeHeight / 2f) - verticalSpace;
            left = getMeasuredWidth() - badgeWidth - horizontalSpace;
            top = getMeasuredHeight() - badgeHeight - verticalSpace;
            right = getMeasuredWidth() - horizontalSpace;
            bottom = getMeasuredHeight() - verticalSpace;
        }
        if (badgeGravity == (Gravity.LEFT | Gravity.TOP)) {
            badgeCenterX = (badgeWidth / 2f) + horizontalSpace;
            badgeCenterY = (badgeHeight / 2f) + verticalSpace;
            left = horizontalSpace;
            top = verticalSpace;
            right = badgeWidth + horizontalSpace;
            bottom = badgeHeight + verticalSpace;
        }
        if (badgeGravity == (Gravity.LEFT | Gravity.BOTTOM)) {
            badgeCenterX = (badgeWidth / 2f) + horizontalSpace;
            badgeCenterY = getMeasuredHeight() - (badgeHeight / 2f) - verticalSpace;
            left = horizontalSpace;
            top = getMeasuredHeight() - badgeHeight - verticalSpace;
            right = badgeWidth + horizontalSpace;
            bottom = getMeasuredHeight() - verticalSpace;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setupGravity();
    }

    private void badgeTextSizeAndPosition() {
        Rect bounds = new Rect();
        String text = valueIsText ? showValueText : showValue;
        numberPaint.getTextBounds(text, 0, text.length(), bounds);
        float textHeight = bounds.height();

        textBaselineY = badgeCenterY + textHeight * 0.5f;
        textBaselineX = badgeCenterX;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF(left, top, right, bottom);
        badgeTextSizeAndPosition();
        switch (badgeShape) {
            case ICON_DRAWABLE:
                if(iconDrawable != null) {
                    iconDrawable.setBounds(left, top, right, bottom);
                    iconDrawable.draw(canvas);
                }
                canvas.drawText(valueIsText ? showValueText : showValue, textBaselineX, textBaselineY, numberPaint);
                if (drawForward) {
                    canvas.drawCircle(badgeCenterX, badgeCenterY, badgeWidth / 2, forwardPaint);
                }
                break;
            case SHAPE_CIRCLE:
                canvas.drawCircle(badgeCenterX, badgeCenterY, badgeWidth / 2, backgroundPaint);
                canvas.drawText(valueIsText ? showValueText : showValue, textBaselineX, textBaselineY, numberPaint);
                if (drawForward) {
                    canvas.drawCircle(badgeCenterX, badgeCenterY, badgeWidth / 2, forwardPaint);
                }
                break;
            case SHAPE_OVAL:
                canvas.drawOval(rectF, backgroundPaint);
                canvas.drawText(valueIsText ? showValueText : showValue, textBaselineX, textBaselineY, numberPaint);
                break;
            case SHAPE_RECTANGLE:
                canvas.drawRect(rectF, backgroundPaint);
                canvas.drawText(valueIsText ? showValueText : showValue, textBaselineX, textBaselineY, numberPaint);
                break;
        }

    }

    public BadgeView setForwardColor(int defaultForwardColor) {
        this.defaultForwardColor = defaultForwardColor;
        setupPaints();
        invalidate();
        return this;
    }

    public BadgeView setDrawForward(boolean drawForward) {
        this.drawForward = drawForward;
        invalidate();
        return this;
    }

    public BadgeView setBadgeTypeFace(Typeface typeFace) {
        this.typeFace = typeFace;
        setupPaints();
        invalidate();
        return this;
    }

    public BadgeView setBadgeShape(int badgeShape) {
        this.badgeShape = badgeShape;
        invalidate();
        return this;
    }

    public BadgeView setBadgeGravity(int gravity) {
        badgeGravity = gravity;
        setupGravity();
        invalidate();
        return this;
    }

    public BadgeView setBadgeWidthAndHeight(int badgeWidth, int badgeHeight) {
        this.badgeWidth = badgeWidth;
        this.badgeHeight = badgeHeight;
        invalidate();
        return this;
    }

    public BadgeView setBadgeWidth(int badgeWidth) {
        this.badgeWidth = dip2px(getContext(), badgeWidth);
        invalidate();
        return this;

    }

    public BadgeView setBadgeHeight(int badgeHeight) {
        this.badgeHeight = dip2px(getContext(), badgeHeight);
        invalidate();
        return this;
    }

    public BadgeView setSpace(int horizontalSpace, int verticalSpace) {
        this.horizontalSpace = dip2px(getContext(), horizontalSpace);
        this.verticalSpace = dip2px(getContext(), verticalSpace);
        setupGravity();
        invalidate();
        return this;
    }

    public BadgeView setTextSize(int sp) {
        defaultTextSize = sp2px(getContext(), sp);
        setupPaints();
        invalidate();
        return this;
    }

    public BadgeView setTextColor(int color) {
        defaultTextColor = color;
        setupPaints();
        invalidate();
        return this;
    }

    public BadgeView setBadgeBackground(int color) {
        defaultBackgroundColor = color;
        setupPaints();
        invalidate();
        return this;
    }

    public void setupBadgeBackground(int defaultBackgroundColor) {
        backgroundPaint.setColor(defaultBackgroundColor);
        invalidate();
    }

    public int getBadgeBackgroundColor() {
        return defaultBackgroundColor;
    }


    public BadgeView setBadgeValue(int count) {
        valueIsText = false;
        showValue = String.valueOf(count);
        invalidate();
        return this;
    }

    public BadgeView setBadgeValue(String showValueText) {
        valueIsText = true;
        this.showValueText = showValueText;
        invalidate();
        return this;
    }

    public int getBadgeValue() {
        return Integer.parseInt(showValue);
    }

    public void incCount(int inc) {
        valueIsText = false;
        showValue = String.valueOf((Integer.parseInt(showValue) + inc));
        invalidate();
    }

    public void decCount(int dec) {
        valueIsText = false;
        showValue = String.valueOf((Integer.parseInt(showValue) - dec));
        invalidate();
    }

    public BadgeView getView() {
        return this;
    }

    public BadgeView bind(View view) {
        if (getParent() != null)
            ((ViewGroup) getParent()).removeView(this);
        if (view == null)
            return this;
        if ((view.getParent() instanceof FrameLayout)) {
            ((FrameLayout) view.getParent()).addView(this);
            return this;
        } else if (view.getParent() instanceof ViewGroup) {
            ViewGroup parentContainer = (ViewGroup) view.getParent();
            int viewIndex = ((ViewGroup) view.getParent()).indexOfChild(view);
            ((ViewGroup) view.getParent()).removeView(view);
            FrameLayout container = new FrameLayout(getContext());
            ViewGroup.LayoutParams containerParams = view.getLayoutParams();
            int originHeight = containerParams.height;
            int originWidth = containerParams.width;
            ViewGroup.LayoutParams wheelParams = new ViewGroup.LayoutParams(originWidth, originHeight);
            FrameLayout.LayoutParams viewLayoutParams = new FrameLayout.LayoutParams(originWidth, originHeight);
            viewLayoutParams.gravity = Gravity.CENTER;

            container.setLayoutParams(containerParams);
            view.setLayoutParams(viewLayoutParams);
            setLayoutParams(wheelParams);
            container.setLayoutParams(containerParams);

            container.setId(view.getId());
            container.addView(view);
            container.addView(this);
            parentContainer.addView(container, viewIndex);
        } else if (view.getParent() == null) {
            Log.e("badgeview", "View must have a parent");
        }
        return this;
    }

    public boolean unbind() {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
            return true;
        }
        return false;
    }
}
