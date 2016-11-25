package com.github.viliamvolosv.switchcontrol.painter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitchState;
import com.github.glomadrian.materialanimatedswitch.painter.SwitchInboxPinnedPainter;

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

    public TextPainter(int color, String text, float horizontalMargin, boolean isLeft) {
        this.color = color;
        this.text = text;
        this.horizontalMargin = horizontalMargin;
        this.isLeft = isLeft;
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
        float desiredWidth = width / 2 - horizontalMargin - height / 2;
        final float testTextSize = 18f;

        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(testTextSize);
        //   Rect bounds = new Rect();
        //   paint.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        //   float desiredTextSize = testTextSize * desiredWidth / bounds.width();

        // Set the paint for that size.
        //  paint.setTextSize(desiredTextSize);
        textBounds = new
                Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
    }

    @Override
    public void draw(Canvas canvas) {
        if (isLeft)
            canvas.drawText(text, horizontalMargin + height / 4, height / 2 + textBounds.height() / 4, paint);
        else
            canvas.drawText(text, width / 2 + height / 4 + textBounds.width() / 2, height / 2 + textBounds.height() / 4, paint);
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
