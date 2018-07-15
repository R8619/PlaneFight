package com.wlw2_11.planefight.Object;


/**
 * Created by 任建康 on 2018/6/17.
 */

public class Boss {
    public int x;
    public int y;
    public int width;
    public int height;
    public int boo;//爆炸效果显示时间
    public int visual;//可视
    public int life;///生命值
    public int v;//纵向速度
    public int vx;//横向速度
    public Boss(){
        x=200;
        y=-300;
        width=420;
        height=400;
        v=10;
        visual=0;
        life=200;
        vx=20;
        boo=0;
    }
}
