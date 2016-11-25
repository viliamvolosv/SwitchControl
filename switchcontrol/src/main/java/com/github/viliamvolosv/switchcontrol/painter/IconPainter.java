package com.github.viliamvolosv.switchcontrol.painter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.DrawableRes;

import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitchState;
import com.github.glomadrian.materialanimatedswitch.Utils;
import com.github.glomadrian.materialanimatedswitch.painter.SwitchInboxPinnedPainter;

/**
 * Created by ViliamVolosV on 22.11.2016.
 */
public class IconPainter implements SwitchControllPainter {

    protected Bitmap iconBitmap;
    protected Context context;
    public Paint paint;
    protected int width;
    protected int height;
    protected int imageHeight;
    protected int imageWidth;
    protected boolean isVisible = true;
    @DrawableRes
    int drawableId;
    protected int iconXPosition;
    protected int iconYPosition;
    protected float margin;
    int iconSize;
    private MaterialAnimatedSwitchState actualState;

    boolean isLeft;

    public IconPainter(Context context, @DrawableRes int drawableId, float margin, boolean isLeft) {
        this.context = context;
        this.drawableId = drawableId;
        this.margin = margin;
        this.isLeft = isLeft;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        iconSize = height / 2;

    }

    protected void initBitmap() {
        iconBitmap = Utils.getBitmap(context, drawableId, iconSize, iconSize);
        imageHeight = iconBitmap.getHeight();
        imageWidth = iconBitmap.getWidth();
    }

    @Override
    public void draw(Canvas canvas) {
        if (isVisible && iconBitmap!=null) {
            canvas.drawBitmap(iconBitmap, iconXPosition, iconYPosition, paint);
        }
    }

    @Override
    public void setColor(int color) {

    }

    @Override
    public int getColor() {
        return 0;
    }

    @Override
    public void onSizeChanged(int height, int width) {
        if (this.height != height || this.width != width) {
            this.height = height;
            this.width = width;
            iconSize = height / 2;
            initBitmap();
        }

        iconYPosition = height / 2 - iconSize / 2;

        if (isLeft) {
            iconXPosition = (int) margin - iconSize / 2;
        } else {
            iconXPosition = width - (int) margin - iconSize / 2;
        }


    }

    @Override
    public void setState(MaterialAnimatedSwitchState state) {
        this.actualState = state;
        switch (state) {
            case INIT:
                break;
            case PRESS:

                break;
            case RELEASE:

                break;
        }
    }

    @Override
    public Paint getPaint() {
        return paint;
    }
}
