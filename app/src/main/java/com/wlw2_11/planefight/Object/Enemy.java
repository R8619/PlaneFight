package com.wlw2_11.planefight.Object;
import com.wlw2_11.planefight.Bullet.*;
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
    public int vx;
    public int treasure;
    public int life;
    public Enemy(){
        Random random=new Random();
        y=0;
        width=97;//敌机的宽度
        height=124;//敌机的长度
        visual=0;
        life=2;
        v=20;//速度
        vx=10;
        x=random.nextInt(900);//x轴随机
        boo=0;
        treasure=random.nextInt(20);//随机出现宝物
    }
}