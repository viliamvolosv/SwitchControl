package com.github.viliamvolosv.switchcontrol;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;

import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitchState;
import com.github.glomadrian.materialanimatedswitch.observer.BallFinishObservable;
import com.github.glomadrian.materialanimatedswitch.observer.BallMoveObservable;
import com.github.viliamvolosv.switchcontrol.painter.BasePainter;
import com.github.viliamvolosv.switchcontrol.painter.IconPainter;
import com.github.viliamvolosv.switchcontrol.painter.SwitcherPainter;
import com.github.viliamvolosv.switchcontrol.painter.TextPainter;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by ViliamVolosV on 22.11.2016.
 */
public class SwitchControl extends View {

    private int margin;

    private MaterialAnimatedSwitchState actualState;

    private int baseColorRelease = Color.parseColor("#3061BE");
    private int baseColorPress = Color.parseColor("#D7E7FF");
    private int ballColorRelease = Color.parseColor("#5992FB");
    private int ballColorPress = Color.parseColor("#FFFFFF");
    private int ballShadowColor = Color.parseColor("#99000000");

    private float radius = 0;
    private float borderSize = 0;
    private BallFinishObservable ballFinishObservable;
    private BallMoveObservable ballMoveObservable;
    private boolean isClickable = true;
    private boolean isRefreshable = true;
    private boolean isToggle = false;
    private CancelTask cancelTask = new CancelTask();
    private OnCheckedChangeListener onCheckedChangeListener;

    @DrawableRes
    private int leftIcon;
    @DrawableRes
    private int rightIcon;
    @DrawableRes
    private int leftSelectedIcon;
    @DrawableRes
    private int rightSelectedIcon;

    private String leftText;
    private String rightText;
    private float textSize = 18f;

    private PainterControl painterControl = new PainterControl();
    private PainterControl unselectedPainterControl = new PainterControl();
    private PainterControl selectedPainterControl = new PainterControl();

    public SwitchControl(Context context) {
        super(context);
        init();
    }

    public SwitchControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SwitchControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init() {
        margin = (int) getContext().getResources().getDimension(R.dimen.margin);
        initObservables();
        initPainters();
        actualState = MaterialAnimatedSwitchState.INIT;
        setState(actualState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void initPainters() {
        painterControl = new PainterControl();
        unselectedPainterControl = new PainterControl();
        selectedPainterControl = new PainterControl();

        BasePainter basePainter = new BasePainter(baseColorRelease, baseColorPress, borderSize, radius, ballMoveObservable);
        painterControl.add(basePainter);


        if (leftIcon != 0) {
            IconPainter leftIconPainter = new
                    IconPainter(getContext(), leftIcon, radius, true);
            unselectedPainterControl.add(leftIconPainter);
        }
        if (rightIcon != 0) {
            IconPainter rightIconPainter = new
                    IconPainter(getContext(), rightIcon, radius, false);
            unselectedPainterControl.add(rightIconPainter);
        }

        if (leftSelectedIcon != 0) {
            IconPainter leftNegativeIconPainter = new
                    IconPainter(getContext(), leftSelectedIcon, radius, true);
            selectedPainterControl.add(leftNegativeIconPainter);
        }

        if (rightSelectedIcon != 0) {
            IconPainter rightNegativeIconPainter = new
                    IconPainter(getContext(), rightSelectedIcon, radius, false);
            selectedPainterControl.add(rightNegativeIconPainter);
        }


        if (!TextUtils.isEmpty(leftText)) {
            TextPainter leftTextPainter = new
                    TextPainter(Color.parseColor("#FFFFFF"), leftText, radius, true, textSize);
            selectedPainterControl.add(leftTextPainter);
            TextPainter leftTextPainterMasked = new
                    TextPainter(Color.parseColor("#ff000000"), leftText, radius, true, textSize);
            unselectedPainterControl.add(leftTextPainterMasked);
        }

        if (!TextUtils.isEmpty(rightText)) {
            TextPainter rightTextPainter = new
                    TextPainter(Color.parseColor("#FFFFFF"), rightText, radius, false, textSize);
            selectedPainterControl.add(rightTextPainter);
            TextPainter rightTextPainterMasked = new
                    TextPainter(Color.parseColor("#ff000000"), rightText, radius, false, textSize);
            unselectedPainterControl.add(rightTextPainterMasked);
        }

        SwitcherPainter switcherPainter = new SwitcherPainter(ballColorRelease, ballColorPress, borderSize, radius, ballFinishObservable,
                ballMoveObservable, getContext(), selectedPainterControl, unselectedPainterControl);
        painterControl.add(switcherPainter);

    }

    private void init(AttributeSet attrs) {
        TypedArray attributes =
                getContext().obtainStyledAttributes(attrs, R.styleable.segmentAnimatedSwitch);
        initAttributes(attributes);
        attributes =
                getContext().obtainStyledAttributes(attrs, R.styleable.materialAnimatedSwitch);

        initParentAttributes(attributes);
        init();
    }

    private void initAttributes(TypedArray attributes) {
        leftIcon =
                attributes.getResourceId(R.styleable.segmentAnimatedSwitch_icon_left,
                        0);
        rightIcon =
                attributes.getResourceId(R.styleable.segmentAnimatedSwitch_icon_right,
                        0);
        leftSelectedIcon =
                attributes.getResourceId(R.styleable.segmentAnimatedSwitch_icon_left_selected,
                        0);
        rightSelectedIcon =
                attributes.getResourceId(R.styleable.segmentAnimatedSwitch_icon_right_selected,
                        0);
        radius =
                attributes.getDimension(R.styleable.segmentAnimatedSwitch_radius,
                        0f);
        borderSize =
                attributes.getDimension(R.styleable.segmentAnimatedSwitch_border_size,
                        0f);

        rightText = attributes.getString(R.styleable.segmentAnimatedSwitch_text_right);
        leftText = attributes.getString(R.styleable.segmentAnimatedSwitch_text_left);

        textSize = attributes.getDimension(R.styleable.segmentAnimatedSwitch_text_size,
                18f);

    }

    private void initParentAttributes(TypedArray attributes) {
        baseColorRelease = attributes.getColor(R.styleable.materialAnimatedSwitch_base_release_color,
                baseColorRelease);
        baseColorPress =
                attributes.getColor(R.styleable.materialAnimatedSwitch_base_press_color, baseColorPress);
        ballColorRelease = attributes.getColor(R.styleable.materialAnimatedSwitch_ball_release_color,
                ballColorRelease);
        ballColorPress =
                attributes.getColor(R.styleable.materialAnimatedSwitch_ball_press_color, ballColorPress);
    }

    private void initObservables() {
        ballFinishObservable = new BallFinishObservable();
        ballMoveObservable = new BallMoveObservable();
        ballFinishObservable.addObserver(new BallStateObserver());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 100;
        int desiredHeight = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }
        setMeasuredDimension(width, height);
        painterControl.onSizeChanged(height, width);
        unselectedPainterControl.onSizeChanged(height, width);
        selectedPainterControl.onSizeChanged(height, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        painterControl.draw(canvas);
        if (isRefreshable) {
            invalidate();
        }
    }

    private void setState(MaterialAnimatedSwitchState materialAnimatedSwitchState) {
        painterControl.setState(materialAnimatedSwitchState);
        unselectedPainterControl.setState(materialAnimatedSwitchState);
        selectedPainterControl.setState(materialAnimatedSwitchState);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isClickable) {
                    isToggle = false;
                    doActionDown();
                }
                return true;
            default:
                return false;
        }
    }

    private void doActionDown() {
        isRefreshable = true;
        removeCallbacks(cancelTask);
        postDelayed(cancelTask, 1000);
        if (actualState.equals(MaterialAnimatedSwitchState.RELEASE) || actualState.equals(
                MaterialAnimatedSwitchState.INIT) || actualState == null) {
            actualState = MaterialAnimatedSwitchState.PRESS;
            setState(actualState);
        } else {
            actualState = MaterialAnimatedSwitchState.RELEASE;
            setState(actualState);
        }
        playSoundEffect(SoundEffectConstants.CLICK);
        invalidate();
    }

    public boolean isChecked() {
        return actualState.equals(MaterialAnimatedSwitchState.PRESS);
    }

    public void toggle() {
        if (isClickable) {
            isToggle = true;
            doActionDown();
        }
    }

    /**
     * Avoid click when ball is still in movement
     * Call listener when state is updated
     */
    private class BallStateObserver implements Observer {
        @Override
        public void update(Observable observable, Object data) {
            BallFinishObservable ballFinishObservable = (BallFinishObservable) observable;
            isClickable = !ballFinishObservable.getState().equals(BallFinishObservable.BallState.MOVE);

            if (ballFinishObservable.getState().equals(BallFinishObservable.BallState.PRESS)) {
                if (onCheckedChangeListener != null && !isToggle) {
                    onCheckedChangeListener.onCheckedChanged(true);
                }
            } else if (ballFinishObservable.getState().equals(BallFinishObservable.BallState.RELEASE)) {
                if (onCheckedChangeListener != null && !isToggle) {
                    onCheckedChangeListener.onCheckedChanged(false);
                }
            }
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public interface OnCheckedChangeListener {

        void onCheckedChanged(boolean isChecked);
    }

    private class CancelTask implements Runnable {

        @Override
        public void run() {
            isRefreshable = false;
        }
    }
}
