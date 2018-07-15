package com.wlw2_11.planefight.Object;
import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by 任建康 on 2018/6/17.
 */

public class Plane {
    public int x;
    public int y;
    public int width;
    public int height;
    public int life;
    public int boo;
    private static  boolean isMissileBoom;
    public Plane(){
        x=300;
        y=1400;
        width=150;
        height=124;
        life=40;
        boo=0;
        isMissileBoom = false;
    }
    /**
     * 设置导弹状态
     *
     * @param isBoom
     */
    public static void setMissileState(boolean isBoom) {
        isMissileBoom = isBoom;
    }


    public boolean getMissileState() {
        return isMissileBoom;
    }

}
