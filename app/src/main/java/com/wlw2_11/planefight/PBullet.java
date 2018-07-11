package com.wlw2_11.planefight;

/**
 * Created by 任建康 on 2018/6/17.
 */

public class PBullet {
    public int width;
    public int height;
    public int v;
    public int x;
    public int y;
    public int visual;
    public int boo;
    public PBullet(){
        width=10;
        height=42;
        visual=0;//此处的THIS是不是会影响它子弹的种类
        boo=0;
    }
}
