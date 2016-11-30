package com.github.viliamvolosv.switchcontrol.painter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitchState;

/**
 * Created by ViliamVolosV on 22.11.2016.
 */

public class TextPainter implements SwitchControllPainter {

    public Paint paint;
    private int color;
    private int height;
    private int width;
    private String text;
    private float horizontalMargin;
    private boolean isLeft;
    private Rect textBounds;
    private float x, y;
    private float textSize =18f;

    public TextPainter(int color, String text, float horizontalMargin, boolean isLeft,float textSize) {
        this.color = color;
        this.text = text;
        this.horizontalMargin = horizontalMargin;
        this.isLeft = isLeft;
        this.textSize = textSize;
        init();
    }

    void init() {
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        //setTextSizeForWidth(paint, height/2-horizontalMargin, text);
        setTextSize();

    }

    void setTextSize() {
        paint.setTextSize(textSize);
        textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        y = height / 2 + textBounds.height() / 4;
        if (isLeft) {
            x = horizontalMargin + height / 2;
        } else {
            x = width - textBounds.width() - horizontalMargin - height / 2;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (isLeft)
            canvas.drawText(text, x, y, paint);
        else
            canvas.drawText(text, x, y, paint);
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void onSizeChanged(int height, int width) {
        this.height = height;
        this.width = width;
        setTextSize();
    }

    @Override
    public void setState(MaterialAnimatedSwitchState materialAnimatedSwitchState) {

    }

    private static void setTextSizeForWidth(Paint paint, float desiredWidth,
                                            String text) {

        // Pick a reasonably large value for the test. Larger values produce
        // more accurate results, but may cause problems with hardware
        // acceleration. But there are workarounds for that, too; refer to
        // http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
        final float testTextSize = 48f;

        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSize = testTextSize * desiredWidth / bounds.width();

        // Set the paint for that size.
        paint.setTextSize(desiredTextSize);
    }

    @Override
    public Paint getPaint() {
        return paint;
    }
}
