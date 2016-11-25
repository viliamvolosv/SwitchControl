package com.github.viliamvolosv.switchcontrol.painter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitchState;
import com.github.glomadrian.materialanimatedswitch.observer.BallFinishObservable;
import com.github.glomadrian.materialanimatedswitch.observer.BallMoveObservable;
import com.github.viliamvolosv.switchcontrol.PainterControl;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by ViliamVolosV on 22.11.2016.
 */
public class SwitcherPainter extends RoundRectPainter {


    protected int ballPositionX;
    protected int ballMovementRange;

    private ValueAnimator moveAnimator;
    private ValueAnimator colorAnimator;
    private MaterialAnimatedSwitchState actualState;
    private BallFinishObservable ballFinishObservable;
    private BallMoveObservable ballMoveObservable;
    private Context context;

    private PainterControl selectedPainterControl;
    private int count;
    private PainterControl unselectedPainterControl;

    public SwitcherPainter(int color, int borderColor, float borderSize,
                           float radius,
                           BallFinishObservable ballFinishObservable, BallMoveObservable ballMoveObservable,
                           Context context,PainterControl selectedPainterControl,PainterControl unselectedPainterControl) {
        super(color, borderColor, borderSize, radius);
        this.ballFinishObservable = ballFinishObservable;
        this.ballMoveObservable = ballMoveObservable;
        this.selectedPainterControl = selectedPainterControl;
        this.unselectedPainterControl = unselectedPainterControl;
        this.context = context;
        this.selectedPainterControl.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    private void initAnimator() {
        int from = 0;
        int to = getWidth() / 2;
        ballMovementRange = to - from;
        moveAnimator = ValueAnimator.ofInt(from, to);
        moveAnimator.addUpdateListener(new BallAnimatorListener());
        moveAnimator.addListener(new BallAnimatorFinishListener());
    }

    private void initColorAnimator() {
        colorAnimator = ValueAnimator.ofInt(0, 255);
        colorAnimator.setDuration(ballMovementRange);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //toBgPainter.setAlpha((Integer) animation.getAnimatedValue());
            }
        });
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        unselectedPainterControl.draw(canvas);
        canvas.drawRoundRect(getRoundRect(), getRadius(), getRadius(), paint);
        count = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.drawRoundRect(getRoundRect(), getRadius(), getRadius(), paint);
        selectedPainterControl.draw(canvas);
        canvas.restoreToCount(count);
    }


    @Override
    public void onSizeChanged(int height, int width) {
        super.onSizeChanged(height, width);
        setRoundRect(ballPositionX + getBorderSize(), getBorderSize(), ballPositionX + (width / 2 - getBorderSize()), height - getBorderSize());
        initAnimator();
        initColorAnimator();
    }

    @Override
    public void setState(MaterialAnimatedSwitchState state) {
        switch (state) {
            case PRESS:
                actualState = MaterialAnimatedSwitchState.PRESS;
                moveAnimator.start();
                break;
            case RELEASE:
                actualState = MaterialAnimatedSwitchState.RELEASE;
                moveAnimator.reverse();
        }
    }

    private class BallAnimatorFinishListener implements ValueAnimator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {
            ballFinishObservable.setBallState(BallFinishObservable.BallState.MOVE);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (actualState.equals(MaterialAnimatedSwitchState.PRESS)) {
                ballFinishObservable.setBallState(BallFinishObservable.BallState.PRESS);
            } else {
                ballFinishObservable.setBallState(BallFinishObservable.BallState.RELEASE);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            //Empty
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            //Empty
        }
    }

    private class BallAnimatorListener implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int value = (int) animation.getAnimatedValue();
            //Move the ball
            ballPositionX = value;
            //1- Get pixel of movement from 0 to movementRange
            int pixelMove = value;
            //Transform the range movement to a 0 - 100 range
            int rangeValue = getAnimatedRange(pixelMove);
            //Change the color animation to the actual range value (duration is 100)
            colorAnimator.setCurrentPlayTime(rangeValue);
            //Set ball position to
            ballMoveObservable.setBallPosition(ballPositionX);
            //Put this value on a observable the listeners know the state of the movement in a range of 0
            //to 100
            ballMoveObservable.setBallAnimationValue(rangeValue);
            setRoundRect(ballPositionX + getBorderSize(), getBorderSize(), ballPositionX + (getWidth() / 2 - getBorderSize()), getHeight() - getBorderSize());
        }

        private int getAnimatedRange(int value) {
            return ((value * 100) / ballMovementRange);
        }
    }

}
