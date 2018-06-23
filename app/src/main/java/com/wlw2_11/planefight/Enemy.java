package com.wlw2_11.planefight;

import java.util.Random;

/**
 * Created by 任建康 on 2018/6/17.
 */

public class Enemy {
    public int x;
    public int y;
    public int width;
    public int height;
    public int visual;
    public int v;
    public int boo;//爆炸
    public int treasure;
    public Enemy(){
        Random random=new Random();
        y=0;
        width=97;
        height=124;
        visual=0;
        v=20;
        x=random.nextInt(900);//x轴随机
        boo=1;
        treasure=random.nextInt(20);//随机出现宝物
    }
}