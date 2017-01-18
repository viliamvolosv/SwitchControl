package com.github.viliamvolosv.switchcontrol.painter;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by ViliamVolosV on 18.01.2017.
 */

public class IconTextPainter extends TextPainter {

    IconPainter iconPainter;
    float space =3f;

    public IconTextPainter(int color, String text, float horizontalMargin, boolean isLeft, float textSize, IconPainter iconPainter) {
        super(color, text, horizontalMargin, isLeft, textSize);
        this.iconPainter = iconPainter;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (iconPainter != null)
            iconPainter.draw(canvas);
    }

    @Override
    public void onSizeChanged(int height, int width) {
        //super.onSizeChanged(height, width);

        paint.setTextSize(textSize);
        textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);

        y = height / 2 - (textBounds.bottom + textBounds.top) / 2;
        float imagewidth = 0;
        if (iconPainter != null) {
            imagewidth =  iconPainter.getImageWidth();
        }

        if (isLeft) {
            x = width / 4 - textBounds.width() / 2 + imagewidth/2;
        } else {
            //x = width - textBounds.width() - horizontalMargin - height / 2;
            x = width * 0.75f - textBounds.width() / 2 - imagewidth/2 ;
        }
        if (iconPainter != null) {
            iconPainter.getPaint().setXfermode(paint.getXfermode());
            iconPainter.onSizeChanged(height, width);
            if (iconPainter.isLeft)
                iconPainter.setMargin(x-imagewidth/2 - space);
            else
                iconPainter.setMargin(width/4 - textBounds.width() / 2 -space);
        }

    }
}
