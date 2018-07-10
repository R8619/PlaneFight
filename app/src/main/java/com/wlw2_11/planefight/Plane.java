package com.wlw2_11.planefight;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by 任建康 on 2018/6/17.
 */

public class Plane {
    private static final boolean Random = false;
    private float middle_x;
    private float middle_y;
    private long startTime; // 开始时间
    private long endTime; // 结束时间
    private boolean isChangeBullet; // 更换子弹类型
    private Bitmap mPlane;
    private Bitmap mPlaneExplosion;
    private boolean isInvincible; // 是否无敌
    private boolean isDamaged; // 是否受损
    private int bulletType;//当前子弹类型
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
        width=97;
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
