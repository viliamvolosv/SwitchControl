package com.github.viliamvolosv.switchcontrol;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;

import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitchState;
import com.github.glomadrian.materialanimatedswitch.painter.SwitchInboxPinnedPainter;
import com.github.viliamvolosv.switchcontrol.painter.SwitchControllPainter;

import java.util.ArrayList;

/**
 * Created by ViliamVolosV on 22.11.2016.
 */

public class PainterControl implements SwitchControllPainter {


    ArrayList<SwitchControllPainter> switchInboxPinnedPainters;

    public PainterControl() {
        switchInboxPinnedPainters = new ArrayList<>();
    }

    public void add(SwitchControllPainter switchInboxPinnedPainter) {
        switchInboxPinnedPainters.add(switchInboxPinnedPainter);
    }

    public void remove(SwitchControllPainter switchInboxPinnedPainter) {
        switchInboxPinnedPainters.remove(switchInboxPinnedPainter);
    }


    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < switchInboxPinnedPainters.size(); i++) {
            switchInboxPinnedPainters.get(i).draw(canvas);
        }
    }

    @Override
    public void setColor(int i) {

    }

    @Override
    public int getColor() {
        return 0;
    }

    @Override
    public void onSizeChanged(int height, int width) {
        for (int i = 0; i < switchInboxPinnedPainters.size(); i++) {
            switchInboxPinnedPainters.get(i).onSizeChanged(height, width);
        }
    }

    @Override
    public void setState(MaterialAnimatedSwitchState materialAnimatedSwitchState) {
        for (int i = 0; i < switchInboxPinnedPainters.size(); i++) {
            switchInboxPinnedPainters.get(i).setState(materialAnimatedSwitchState);
        }
    }

    @Override
    public Paint getPaint() {
        return null;
    }

    public void setXfermode(PorterDuffXfermode porterDuffXfermode) {
        for (int i = 0; i < switchInboxPinnedPainters.size(); i++) {
            switchInboxPinnedPainters.get(i).getPaint().setXfermode(porterDuffXfermode);
        }
    }

}
