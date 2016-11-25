package com.github.viliamvolosv.switchcontrol.painter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitchState;
import com.github.glomadrian.materialanimatedswitch.painter.SwitchInboxPinnedPainter;

/**
 * Created by ViliamVolosV on 22.11.2016.
 */

public class RoundRectPainter implements SwitchControllPainter {


    protected Paint paint;
    private Paint strokePaint;

    private int color;
    private int borderColor;

    private int height;
    private int width;

    private float borderSize = 0;
    private float radius = 0;

    private RectF roundRect;
    private RectF strokeRect;


    public RoundRectPainter(int color, int borderColor, float borderSize,
                            float radius) {
        this.color = color;
        this.borderColor = borderColor;
        this.radius = radius;
        this.borderSize = borderSize;
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);

        strokePaint = new Paint();
        strokePaint.setColor(borderColor);
        strokePaint.setAntiAlias(true);

        roundRect = new RectF();
        roundRect.set(borderSize, borderSize, width - borderSize, height - borderSize);
        strokeRect = new RectF();
        strokeRect.set(0, 0, width, height );

    }

    @Override
    public void draw(Canvas canvas) {
        if (borderSize>0)
        canvas.drawRoundRect(strokeRect, radius, radius, strokePaint);
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int getColor() {
        return color;
    }
    public int getHeight() {
        return height;
    }

    public float getRadius() {
        return radius;
    }

    public int getWidth() {
        return width;
    }

    public float getBorderSize() {
        return borderSize;
    }

    @Override
    public void onSizeChanged(int height, int width) {
        this.height = height;
        this.width = width;
        strokeRect.set(0, 0, width, height );
    }

    @Override
    public void setState(MaterialAnimatedSwitchState materialAnimatedSwitchState) {

    }

    public RectF getRoundRect() {
        return roundRect;
    }

    public void setRoundRect(float left, float top, float right, float bottom){
        roundRect.set(left,top,right,bottom);
        strokeRect.set(left - borderSize, top - borderSize, right+borderSize, bottom + borderSize );
    }

    @Override
    public Paint getPaint() {
        return paint;
    }
}
