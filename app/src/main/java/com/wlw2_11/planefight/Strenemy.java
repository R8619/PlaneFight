package com.wlw2_11.planefight;

import java.util.Random;

/**
 * Created by 任建康 on 2018/7/2.
 */

public class Strenemy {
    public int x;
    public int y;
    public int width;
    public int height;
    public int visual;
    public int v;
    public int boo;//爆炸
    public int treasure;
    public int life;
    public Strenemy(){
        Random random=new Random();
        y=0;
        width=115;//敌机的宽度
        height=135;//敌机的长度
        visual=0;
        life=2;
        v=40;//速度
        x=random.nextInt(900);//x轴随机
        boo=0;
        treasure=random.nextInt(20);//随机出现宝物
    }
}