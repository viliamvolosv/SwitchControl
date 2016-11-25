package com.github.viliamvolosv.switchcontrol.painter;

import android.graphics.Canvas;

import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitchState;
import com.github.glomadrian.materialanimatedswitch.observer.BallMoveObservable;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by ViliamVolosV on 22.11.2016.
 */
public class BasePainter extends RoundRectPainter implements Observer {

    private ValueAnimator colorAnimator;
    private BallMoveObservable ballMoveObservable;

    public BasePainter(int color, int borderColor, float borderSize,
                       float radius, BallMoveObservable ballMoveObservable) {
        super(color, borderColor, borderSize, radius);
        this.ballMoveObservable = ballMoveObservable;
        initColorAnimator();
//        ballMoveObservable.addObserver(this);
    }


    private void initColorAnimator() {
        colorAnimator = ValueAnimator.ofInt(0, 255);
        colorAnimator.setDuration(100);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //  toBgPainter.setAlpha((Integer) animation.getAnimatedValue());
            }
        });
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRoundRect(getRoundRect(), getRadius(), getRadius(), paint);
    }

    @Override
    public void onSizeChanged(int height, int width) {
        super.onSizeChanged(height, width);
        setRoundRect(getBorderSize(), getBorderSize(), getWidth() - getBorderSize(), getHeight() - getBorderSize());
    }

    @Override
    public void setState(MaterialAnimatedSwitchState state) {
        //Empty
    }

    @Override
    public void update(Observable observable, Object data) {
        int value = ((BallMoveObservable) observable).getBallAnimationValue();
        colorAnimator.setCurrentPlayTime(value);
    }
}
