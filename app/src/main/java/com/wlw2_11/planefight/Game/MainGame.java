package com.wlw2_11.planefight.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;

import com.wlw2_11.planefight.Animation.AnimationManager;
import com.wlw2_11.planefight.Animation.BoomAnimation;
import com.wlw2_11.planefight.Animation.ImageAnimation;
import com.wlw2_11.planefight.Bullet.BBullet;
import com.wlw2_11.planefight.Bullet.EBullet;
import com.wlw2_11.planefight.Bullet.PBullet;
import com.wlw2_11.planefight.Bullet.ViceWeapon;
import com.wlw2_11.planefight.Goods.Treasure;
import com.wlw2_11.planefight.Object.Boss;
import com.wlw2_11.planefight.Object.Enemy;
import com.wlw2_11.planefight.Object.Plane;
import com.wlw2_11.planefight.R;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 任建康 on 2018/6/17.
 */

public class MainGame extends View implements Runnable {
    Bitmap img_bg;
    float g_t = 0;   // time
    float g_bg_speed = 500 / 60;// scrolling speed, pixel/second
    int g_x1, g_y1; // bg_1
    int g_x2, g_y2; // bg_2
    long g_c = 0;
    int g_bgH;
    Timer timer = new Timer();
    private int missileCount; // 导弹的数量
    private Bitmap missile_bt; // 导弹按钮图标
    private Bitmap vice_weapon;//副武器图标
    private Bitmap missile_goods;//副武器图标
    private float missile_bt_y;
    long MISSILEBOOM_TIME = 500;// 我方导弹爆炸;
    private Bitmap boom;//爆炸效果图
    public static int Screen_w;
    public static int Screen_h;
    public static Paint paint;
    public static int Point_x = 0;
    public static int Point_y = 0;
    public static boolean isdown;
    public int ide;//敌机编号
    public int numOfDestroy = 0;
    public int ideb = 1;//敌机子弹编号
    public int vde = 0;//副武器子弹编号
    public int numOfEnemy;
    public int numOfStrenemy;
    public int i;
    public int j;
    public int time;
    public int strBullet = 0;
    public int doubleBullet = 0;
    public int mutileweapon = 0;//多副武器子弹
    public int idOfTreasure;
    public boolean isWin = false;
    public int temp = 1;
    public int id = 0;
    public int background;
    public int background1;
    public boolean isLose = false;
    public int score = 0;
    Random rand;
    Plane plane;
    Boss boss;
    Thread thread;
    ViceWeapon[] weapons;//副武器子弹数组
    Treasure[] treasure;
    PBullet[] pb;
    EBullet[] eb;
    BBullet[] bb;
    Enemy[] enemy;
    private Context mcontext;
    public MainGame(Context context, Display display) {//初始化
        super(context);
        AnimationManager.Init(context);
        missileCount=100;
        mcontext = context;
        Screen_w = display.getWidth();//获取屏幕的宽
        Screen_h = display.getHeight();///获取屏幕的高


        img_bg = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bg1);
        g_bgH =Screen_h;
        g_x1 = g_x2 = g_y1 = 0;
        g_y2 = -g_bgH;

        TimerTask task= new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        };
        timer.schedule(task, 0, 16);


        paint = new Paint();
        ide=1;//初始化敌机编号
        idOfTreasure=0;
        numOfEnemy=0;
        plane=new Plane();//新建飞机
        pb=new PBullet[50];//新建子弹
        eb=new EBullet[160];
        bb=new BBullet[160];
        weapons = new ViceWeapon[50];//新建副武器
        enemy=new Enemy[50];//新建敌机
        treasure=new Treasure[70];//新建宝物
        boss=new Boss();//新建Boss
        for(i=0;i<=49;i++){
            pb[i]=new PBullet();
            enemy[i]=new Enemy();
            weapons[i]=new ViceWeapon();
        }
        //新建宝物
        for(i=0;i<=65;i++){
            treasure[i]=new Treasure();
        }
        //新建敌机子弹
        for(int i=0;i<=149;i++) {
            eb[i] = new EBullet();
        }
        //新建BOSS子弹
        for(int i=0;i<=149;i++){//*********Boss子弹的总数只有150颗
            bb[i] = new BBullet();
        }
        missile_bt = BitmapFactory.decodeResource(getResources(), R.mipmap.missile_bt);

        boom = BitmapFactory.decodeResource(getResources(),R.mipmap.boom);
        vice_weapon =BitmapFactory.decodeResource(getResources(),R.mipmap.viceweapon);
        missile_goods = BitmapFactory.decodeResource(getResources(),R.mipmap.missile_goods);
        missile_bt_y = Screen_h - 10 - missile_bt.getHeight();
        rand=new Random();
        background=0;
        this.thread = new Thread(this);//线程
        this.thread.start();//线程开始
    }
    public void run() {
        SoundPool soundPool;
        HashMap musicId =new HashMap();
        soundPool=new SoundPool(12, AudioManager.STREAM_MUSIC,5);
        musicId.put(1,soundPool.load(mcontext,R.raw.shoot,1));
        musicId.put(2,soundPool.load(mcontext,R.raw.bigexplosion,1));
        //飞机活动
        while ((!isLose)&&(!isWin)) {//如果没有赢并且没有输，那么就会执行以下的程序
           /* background+=10;//背景移动
            background1+=10;
            if(background>=2100){
                background=background1-2300;
            }else if(background1>=2300){
                background1=background-2300;
            }*/
            //产生敌机
            if(boss.visual==0){//如果BOSS不出现
                if(enemy[ide].visual==0) {//如果不存在飞机
                    //如果是第一个
                    if (ide == 1) {
                        enemy[ide].visual = 1;
                        enemy[ide].y = 0;
                        ide++;//敌机编号加1
                        numOfEnemy++;//敌机数目加1
                    } else if (ide > 0 && ide <= 39) {//可知敌机的数目只有39架，可以回头测试数目
                        if (enemy[ide - 1].y >= 300) {//如果上一架飞机Y轴的位置大于了300
                            enemy[ide].visual = 1;//出现新飞机，也就是说每300的Y轴出现一架飞机，飞机的间距是已经设置好的。
                            enemy[ide].y = 0;//初始时Y轴的位置
                            ide++;//编号+1
                            numOfEnemy++;//敌机总数+1
                        }
                    }
                }
                if(ide==40){
                    enemy[ide].visual = 1;
                    enemy[ide].y=0;
                    ide=1;
                    enemy[ide].y=0;
                    numOfEnemy++;
                }
            }
            //敌机移动
            for(int i=1;i<=40;i++) {
                enemy[i].y+=enemy[i].v;
                if(enemy[i].y>=Screen_h){
                    enemy[i].visual=0;
                }
                if(enemy[i].visual==1) {
                    enemy[i].x+=enemy[i].vx;
                    if(enemy[i].x+enemy[i].width+enemy[i].vx>Screen_w || enemy[i].x<=50){
                        enemy[i].vx=-enemy[i].vx;
                    }
                }
            }
            //敌机产生子弹
            for(int i=1;i<=40;i++){
                if(enemy[i].visual==1){
                    if((enemy[i].y>=100&&enemy[i].y<=110)){
                        eb[ideb].visual = 1;
                        eb[ideb].y = enemy[i].y+enemy[i].height;
                        eb[ideb].x = enemy[i].x+enemy[i].width/2 + 20;
                        ideb++;
                        eb[ideb].visual = 1;
                        eb[ideb].y = enemy[i].y+enemy[i].height;
                        eb[ideb].x = enemy[i].x+enemy[i].width/2 - 20;
                        ideb++;
                    }
                    if((enemy[i].y>=250&&enemy[i].y<=260)){
                        eb[ideb].visual = 1;
                        eb[ideb].y = enemy[i].y+enemy[i].height;
                        eb[ideb].x = enemy[i].x+enemy[i].width/2 + 20;
                        ideb++;
                        eb[ideb].visual = 1;
                        eb[ideb].y = enemy[i].y+enemy[i].height;
                        eb[ideb].x = enemy[i].x+enemy[i].width/2 - 20;
                        ideb++;
                    }
                    if((enemy[i].y>=400&&enemy[i].y<=410)){
                        eb[ideb].visual = 1;
                        eb[ideb].y = enemy[i].y+enemy[i].height;
                        eb[ideb].x = enemy[i].x+enemy[i].width/2 + 20;
                        ideb++;
                        eb[ideb].visual = 1;
                        eb[ideb].y = enemy[i].y+enemy[i].height;
                        eb[ideb].x = enemy[i].x+enemy[i].width/2 - 20;
                        ideb++;
                    }
                    if(ideb==145){
                        ideb=1;
                    }
                }
            }
            //产生BOSS
            if(numOfEnemy==60){
                boss.visual=2;
            }
            //BOSS移动
            if(boss.visual!=0){
                boss.y+=boss.v;
                if(boss.y>=100){
                    boss.visual=1;
                }
                if(boss.visual==1) {
                    boss.x+=boss.vx;
                    if (boss.y >= 900 || boss.y+boss.v < 100) {
                        boss.v = -boss.v;
                    }
                    if(boss.x+boss.width+boss.vx>Screen_w||boss.x<=50){
                        boss.vx=-boss.vx;
                    }
                }
            }
            //产生BOSS子弹
            if(boss.visual==1){
                if(ideb>=140){
                    ideb=1;
                }
                for(i=0;i<=10;i++) {
                    if (boss.y >= 100+i*40 && boss.y <= 105+i*40&&boss.v>0) {
                        bb[ideb].visual = 1;
                        bb[ideb].y = boss.y + boss.height / 2;
                        bb[ideb].x = boss.x + boss.width / 2 + 320;
                        ideb++;
                        bb[ideb].visual = 1;
                        bb[ideb].y = boss.y + boss.height / 2;
                        bb[ideb].x = boss.x + boss.width / 2 - 320;
                        ideb++;
                    }
                    if(boss.y >= 140+i*40 && boss.y <= 145+i*40&&boss.v<0){
                        bb[ideb].visual = 1;
                        bb[ideb].y = boss.y + boss.height / 2;
                        bb[ideb].x = boss.x + boss.width / 2 + 25;
                        ideb++;
                        bb[ideb].visual = 1;
                        bb[ideb].y = boss.y + boss.height / 2;
                        bb[ideb].x = boss.x + boss.width / 2 - 25;
                        ideb++;
                    }
                }
            }
            //敌机子弹运动
            for(int i=1;i<=145;i++){
                if(eb[i].visual==1){
                    eb[i].y+=eb[i].v;
                }
                if(eb[i].y>=Screen_h){
                    eb[i].visual=0;
                }
            }
            //BOSS子弹移动
            for(i=1;i<=145;i++){
                if(bb[i].visual==1){
                    bb[i].y+=bb[i].v;
                }
                if(bb[i].y>=Screen_h){
                    bb[i].visual=0;
                }
            }
            //按下之后
            if(isdown==true) {
                time++;
                temp++;
                if(temp==5){

                    if(pb[id].visual==0) {
                        if(doubleBullet>=1){
                            pb[id].visual = 1;
                            pb[id].x = plane.x + plane.width / 2+20;
                            soundPool.play((Integer) musicId.get(1),0.5f,0.5f, 0, 0, 1);
                            pb[id].y = plane.y;
                            pb[id].v = 40;
                            if (id <= 48) {
                                id++;
                            } else {
                                id = 0;
                            }
                            pb[id].visual = 1;
                            pb[id].x = plane.x + plane.width / 2-20;
                            soundPool.play((Integer) musicId.get(1),0.5f,0.5f, 0, 0, 1);
                            pb[id].y = plane.y;
                            pb[id].v = 40;
                            if (id <= 48) {
                                id++;
                            } else {
                                id = 0;
                            }
                            if(doubleBullet>=2) {
                                pb[id].visual = 1;
                                pb[id].x = plane.x + plane.width / 2;
                                soundPool.play((Integer) musicId.get(1),0.5f,0.5f, 0, 0, 1);
                                pb[id].y = plane.y;
                                pb[id].v = 40;
                                if (id <= 48) {
                                    id++;
                                } else {
                                    id = 0;
                                }
                            }
                        }
                        if(doubleBullet==0) {
                            pb[id].visual = 1;
                            pb[id].x = plane.x + plane.width / 2;
                            soundPool.play((Integer) musicId.get(1),0.5f,0.5f, 0, 0, 1);
                            pb[id].y = plane.y;
                            pb[id].v = 40;
                            if (id <= 48) {
                                id++;
                            } else {
                                id = 0;
                            }
                        }
                        if (time%7==0) {
                            while (time==100)
                                time=0;
                            if (mutileweapon >= 1) {
                                weapons[vde].visual = 1;
                                weapons[vde].x = plane.x + plane.width / 2 + 60;
                                weapons[vde].x = weapons[vde].x+2;
                                weapons[vde].y = plane.y - 5;
                                if (mutileweapon == 0)
                                    weapons[vde].a = 1.5f;
                                if (mutileweapon == 1)
                                    weapons[vde].a = 1.502f;
                                if (mutileweapon == 2)
                                    weapons[vde].a = 1.504f;
                                if (mutileweapon == 3)
                                    weapons[vde].a = 1.505f;
                                if (vde <= 48) {
                                    vde++;
                                } else {
                                    vde = 0;
                                }
                                weapons[vde].visual = 1;
                                weapons[vde].x = plane.x + plane.width / 2 - 60;
                                weapons[vde].x = weapons[vde].x-2;
                                weapons[vde].y = plane.y - 5;
                                if (mutileweapon == 0)
                                    weapons[vde].a = 1.5f;
                                if (mutileweapon == 1)
                                    weapons[vde].a = 1.502f;
                                if (mutileweapon == 2)
                                    weapons[vde].a = 1.504f;
                                if (mutileweapon == 3)
                                    weapons[vde].a = 1.505f;
                                if (vde <= 48) {
                                    vde++;
                                } else {
                                    vde = 0;
                                }
                                if (mutileweapon >= 2) {
                                    weapons[vde].visual = 1;
                                    weapons[vde].x = plane.x + plane.width / 2 + 100;
                                    weapons[vde].x = weapons[vde].x +3;
                                    weapons[vde].y = plane.y - 10;
                                    if (mutileweapon == 0)
                                        weapons[vde].a = 1.5f;
                                    if (mutileweapon == 1)
                                        weapons[vde].a = 1.502f;
                                    if (mutileweapon == 2)
                                        weapons[vde].a = 1.504f;
                                    if (mutileweapon == 3)
                                        weapons[vde].a = 1.505f;
                                    if (vde <= 48) {
                                        vde++;
                                    } else {
                                        vde = 0;
                                    }
                                    weapons[vde].visual = 1;
                                    weapons[vde].x = plane.x + plane.width / 2 - 100;
                                    weapons[vde].x = weapons[vde].x-3;
                                    weapons[vde].y = plane.y - 10;
                                    if (mutileweapon == 0)
                                        weapons[vde].a = 1.5f;
                                    if (mutileweapon == 1)
                                        weapons[vde].a = 1.502f;
                                    if (mutileweapon == 2)
                                        weapons[vde].a = 1.504f;
                                    if (mutileweapon == 3)
                                        weapons[vde].a = 1.505f;
                                    if (vde <= 48) {
                                        vde++;
                                    } else {
                                        vde = 0;
                                    }
                                }
                                if (mutileweapon >= 3) {
                                    weapons[vde].visual = 1;
                                    weapons[vde].x = plane.x + plane.width / 2 + 140;
                                    weapons[vde].x = weapons[vde].x+4;
                                    weapons[vde].y = plane.y - 25;
                                    if (mutileweapon == 0)
                                        weapons[vde].a = 1.5f;
                                    if (mutileweapon == 1)
                                        weapons[vde].a = 1.502f;
                                    if (mutileweapon == 2)
                                        weapons[vde].a = 1.504f;
                                    if (mutileweapon == 3)
                                        weapons[vde].a = 1.505f;
                                    if (vde <= 48) {
                                        vde++;
                                    } else {
                                        vde = 0;
                                    }
                                    weapons[vde].visual = 1;
                                    weapons[vde].x = plane.x + plane.width / 2 - 140;
                                    weapons[vde].x = weapons[vde].x-4;
                                    weapons[vde].y = plane.y - 25;
                                    if (mutileweapon == 0)
                                        weapons[vde].a = 1.5f;
                                    if (mutileweapon == 1)
                                        weapons[vde].a = 1.502f;
                                    if (mutileweapon == 2)
                                        weapons[vde].a = 1.504f;
                                    if (mutileweapon == 3)
                                        weapons[vde].a = 1.505f;
                                    if (vde <= 48) {
                                        vde++;
                                    } else {
                                        vde = 0;
                                    }
                                }
                            }
                            if (mutileweapon >= 0) {
                                weapons[vde].visual = 1;
                                weapons[vde].x = plane.x + plane.width / 2 + 20;
                                weapons[vde].x = weapons[vde].x+1;
                                weapons[vde].y = plane.y -20;
                                if (mutileweapon == 0)
                                    weapons[vde].a = 1.5f;
                                if (mutileweapon == 1)
                                    weapons[vde].a = 1.502f;
                                if (mutileweapon == 2)
                                    weapons[vde].a = 1.504f;
                                if (mutileweapon == 3)
                                    weapons[vde].a = 1.505f;
                                if (vde <= 48) {
                                    vde++;
                                } else {
                                    vde = 0;
                                }
                                weapons[vde].visual = 1;
                                weapons[vde].x = plane.x + plane.width / 2 - 20;
                                weapons[vde].x = weapons[vde].x-1;
                                weapons[vde].y = plane.y - 20;
                                if (mutileweapon == 0)
                                    weapons[vde].a = 1.5f;
                                if (mutileweapon == 1)
                                    weapons[vde].a = 1.502f;
                                if (mutileweapon == 2)
                                    weapons[vde].a = 1.504f;
                                if (mutileweapon == 3)
                                    weapons[vde].a = 1.505f;
                                if (vde <= 48) {
                                    vde++;
                                } else {
                                    vde = 0;
                                }
                            }
                        }
                        temp = 1;
                    }
                }
                //按下导弹
                if (Point_x > 10 && Point_x < 15 + missile_bt.getWidth() && Point_y > missile_bt_y
                        && Point_y < missile_bt_y + missile_bt.getHeight()+10) {
                    if (missileCount > 0) {
                        missileCount--;
                        plane.setMissileState(true);
                        soundPool.play((Integer) musicId.get(2),1.5f,1.5f, 0, 0, 1);
                        if (boss.visual==1){
                            boss.life-=3;
                        }
                        for (int j=0;j<40;j++){              //循环40个普通敌机
                            if(enemy[j].visual==1&& 0<enemy[j].x&&enemy[j].x<Screen_w&&0<enemy[j].y&&enemy[j].y<Screen_h){      //敌机存在并 判读敌机位置
                                enemy[j].life-=2;
                                enemy[j].visual = 0;//满足以上条件，敌机灭亡
                                enemy[j].boo=3;//爆炸
                                numOfDestroy++;//击杀+1
                            }
                        }
                        if (boss.visual==1){
                            boss.life-=10;
                        }


                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(MISSILEBOOM_TIME);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } finally {
                                    plane.setMissileState(false);
                                }

                            }
                        }).start();
                    }
                }

                //触摸移动飞机
                else {

                    //触摸移动飞机
                    if (Point_x >= plane.x + plane.width / 2) {
                        plane.x += (Point_x-plane.x-plane.width / 2);
                    } else {
                        plane.x -= (-Point_x+plane.x+plane.width / 2);
                    }
                    if (Point_y >= plane.y + plane.height / 2) {
                        plane.y += (Point_y-plane.y-plane.height / 2);
                    } else {
                        plane.y -= (-Point_y+plane.y+plane.height / 2);
                    }
                    //边界检测
                    if(plane.x<=0){//高度检测
                        plane.x=0;
                    }
                    if(plane.x+plane.width>=Screen_w){//宽度检测
                        plane.x=Screen_w-plane.width;
                    }
                }

            }
            //副武器子弹的移动
            for(i=0;i<=49;i++) {
                if (weapons[i].visual == 1) {
                    weapons[i].y -= weapons[i].v*weapons[i].t+weapons[i].a*weapons[i].t*weapons[i].t;
                    weapons[i].t++;
                    if(weapons[i].y<=30){//如果子弹超过边界
                        weapons[i].visual=0;
                        weapons[i].t=0;
                    }
                }
            }
            //子弹移动
            for(int i=0;i<=49;i++) {
                if (pb[i].visual == 1) {
                    pb[i].y -= pb[i].v;
                    if(pb[i].y<=30){//如果子弹超过边界
                        pb[i].visual=0;
                    }
                }
            }
            //与BOSS相撞
            if(boss.visual==2) {
                if (plane.x + plane.width - 10 >= boss.x && plane.x <= boss.x + boss.width) {
                    if (plane.y <= boss.y + boss.height && plane.y + plane.height >= boss.y) {
                        plane.life -= 5;
                        boss.life -=5;
                        if (plane.life <= 0) {
                            isLose = true;
                        }
                    }
                }
            }
            //子弹打到敌机
            for(int i=0;i<=49;i++) {
                for(int j=1;j<=40;j++){
                    if(pb[i].visual==1&&enemy[j].visual==1){//我方子弹和敌机存在
                        if(pb[i].x>=enemy[j].x&&pb[i].x<=enemy[j].x+enemy[j].width) {//子弹的X轴上的坐标介于敌机的X的坐标和其X坐标加上其宽度
                            if (pb[i].y <= enemy[j].y + enemy[j].height&&pb[i].y>=enemy[j].y) {//子弹的纵坐标介于敌机的Y的坐标和其Y坐标加上其长度
                                enemy[j].life-=2;
                                    enemy[j].visual = 0;//满足以上条件，敌机灭亡
                                    pb[i].visual = 0;// 子弹消失
                                    enemy[j].boo=3;//爆炸
                                    numOfDestroy++;//击杀+1
                                    //产生宝物
                                    if(enemy[j].treasure<=0){
                                        treasure[idOfTreasure].visual=1;
                                        treasure[idOfTreasure].x=enemy[j].x;//出现在敌机灭亡的X轴的位置上
                                        treasure[idOfTreasure].y=enemy[j].y;//出现在敌机灭亡的Y轴的位置上
                                        treasure[idOfTreasure].varible=1;//宝物的种类
                                        idOfTreasure++;
                                    }else if(enemy[j].treasure<=2){
                                        treasure[idOfTreasure].visual=1;
                                        treasure[idOfTreasure].x=enemy[j].x;
                                        treasure[idOfTreasure].y=enemy[j].y;
                                        treasure[idOfTreasure].varible=2;
                                        idOfTreasure++;
                                    }else if(enemy[j].treasure<=3){
                                        treasure[idOfTreasure].visual=1;
                                        treasure[idOfTreasure].x=enemy[j].x;
                                        treasure[idOfTreasure].y=enemy[j].y;
                                        treasure[idOfTreasure].varible=3;
                                        idOfTreasure++;
                                    }
                                    else if(enemy[j].treasure<=4){
                                        treasure[idOfTreasure].visual=1;
                                        treasure[idOfTreasure].x=enemy[j].x;
                                        treasure[idOfTreasure].y=enemy[j].y;
                                        treasure[idOfTreasure].varible=4;
                                        idOfTreasure++;
                                    }
                            }
                        }
                    }
                }
            }
            //副武器打到敌机
            for(int i=0;i<=49;i++) {
                for(int j=1;j<=40;j++){
                    if(weapons[i].visual==1&&enemy[j].visual==1){//我方子弹和敌机存在
                        if(weapons[i].x>=enemy[j].x&&weapons[i].x<=enemy[j].x+enemy[j].width) {//子弹的X轴上的坐标介于敌机的X的坐标和其X坐标加上其宽度
                            if (weapons[i].y <= enemy[j].y + enemy[j].height&&weapons[i].y>=enemy[j].y) {//子弹的纵坐标介于敌机的Y的坐标和其Y坐标加上其长度
                                enemy[j].life-=3;
                                enemy[j].visual = 0;//满足以上条件，敌机灭亡
                                weapons[i].visual = 0;// 子弹消失
                                enemy[j].boo=3;//爆炸
                                numOfDestroy++;//击杀+1
                                //产生宝物
                                if(enemy[j].treasure<=0){
                                    treasure[idOfTreasure].visual=1;
                                    treasure[idOfTreasure].x=enemy[j].x;//出现在敌机灭亡的X轴的位置上
                                    treasure[idOfTreasure].y=enemy[j].y;//出现在敌机灭亡的Y轴的位置上
                                    treasure[idOfTreasure].varible=1;//宝物的种类
                                    idOfTreasure++;
                                }else if(enemy[j].treasure<=2){
                                    treasure[idOfTreasure].visual=1;
                                    treasure[idOfTreasure].x=enemy[j].x;
                                    treasure[idOfTreasure].y=enemy[j].y;
                                    treasure[idOfTreasure].varible=2;
                                    idOfTreasure++;
                                }else if(enemy[j].treasure<=3){
                                    treasure[idOfTreasure].visual=1;
                                    treasure[idOfTreasure].x=enemy[j].x;
                                    treasure[idOfTreasure].y=enemy[j].y;
                                    treasure[idOfTreasure].varible=3;
                                    idOfTreasure++;
                                }
                                else if(enemy[j].treasure<=4){
                                    treasure[idOfTreasure].visual=1;
                                    treasure[idOfTreasure].x=enemy[j].x;
                                    treasure[idOfTreasure].y=enemy[j].y;
                                    treasure[idOfTreasure].varible=4;
                                    idOfTreasure++;
                                }
                            }
                        }
                    }
                }
            }
            //宝物移动
            for(int i=0;i<=65;i++){
                if(treasure[i].visual==1) {
                    treasure[i].y+=treasure[i].v;//Y轴移动
                    //吃到宝物
                    if(treasure[i].x+treasure[i].width>=plane.x&&treasure[i].x<=plane.x+plane.width){
                        if(treasure[i].y+treasure[i].height>=plane.y&&treasure[i].y<=plane.y+plane.height){
                            treasure[i].visual=0;
                            if(treasure[i].varible==1){
                                strBullet=1;
                            }
                            if(treasure[i].varible==2){
                                doubleBullet++;
                                mutileweapon++;
                            }
                            if(treasure[i].varible==3){
                                plane.life+=4;
                            }
                            if(treasure[i].varible==4){
                                missileCount++;
                            }
                        }
                    }
                }
            }
            //子弹打到BOSS
            if(boss.visual!=0&&(boss.life>=4)) {
                for (int i = 0; i <= 49; i++) {
                    if(pb[i].visual==1){
                        if(pb[i].x>=boss.x&&pb[i].x<=boss.x+boss.width) {
                            if (pb[i].y <= boss.y + boss.height && pb[i].y >= boss.y) {
                                if(strBullet==0) {
                                    boss.life-=2;
                                }else{
                                    boss.life-=3;
                                }
                                if(boss.life<=0){
                                    isWin=true;
                                }
                                pb[i].visual = 0;
                                pb[i].boo=2;
                            }
                        }
                    }
                }
            }
            //副武器打到BOSS
            if(boss.visual!=0&&(boss.life>=4)) {
                for (i = 0; i <= 49; i++) {
                    if(weapons[i].visual==1){
                        if(weapons[i].x>=boss.x&&weapons[i].x<=boss.x+boss.width) {
                            if (weapons[i].y <= boss.y + boss.height && weapons[i].y >= boss.y) {
                                    boss.life-=3;
                                if(boss.life<=0){
                                    isWin=true;
                                }
                                weapons[i].visual = 0;
                                weapons[i].boo=2;
                            }
                        }
                    }
                }
            }
            //通关条件
            if(boss.life<=0){
                isWin=true;
                boss.life=0;
            }
            //游戏结束
            if(plane.life <= 0){
                isLose=true;
                plane.life=0;
            }
            //飞机中敌机子弹
            for(int i=1;i<=145;i++){
                if(eb[i].visual==1){
                    if(eb[i].x-5>=plane.x&&eb[i].x+5<=plane.x+plane.width){
                        if (eb[i].y-5>=plane.y&&eb[i].y+5<=plane.y+plane.height){
                            plane.life-=2;
                            {//掉血后重置子弹
                                strBullet=0;
                                doubleBullet=0;
                                mutileweapon=0;
                            }
                            eb[i].visual=0;
                            eb[i].boo=2;
                            if(plane.life<=0){
                                isLose=true;
                                plane.life=0;
                            }
                        }
                    }
                }
            }
            //飞机中BOSS子弹
            for(int i=1;i<=145;i++){
                if(bb[i].visual==1){
                    if(bb[i].x-5>=plane.x&&bb[i].x+5<=plane.x+plane.width){
                        if (bb[i].y-5>=plane.y&&bb[i].y+5<=plane.y+plane.height){
                            plane.life-=2;
                            {//死亡后重置子弹
                                strBullet=0;
                                doubleBullet=0;
                                mutileweapon=0;
                            }
                            bb[i].visual=0;
                            bb[i].boo=2;
                            if(plane.life <= 0){
                                isLose=true;
                                plane.life=0;
                            }
                        }
                    }
                }
            }
            //飞机相撞
            for(int j=1;j<=40;j++){
                if(enemy[j].visual==1) {
                    if (enemy[j].x+enemy[j].width-5>= plane.x && enemy[j].x+5<= plane.x + plane.width) {
                        if(enemy[j].y+enemy[j].height>= plane.y&&enemy[j].y<=plane.y+plane.height) {
                            plane.life -= 2;
                            enemy[j].life = 0;
                            enemy[j].visual=0;
                            enemy[j].boo=2;
                            numOfDestroy++;
                            if(plane.life <= 0) {
                                isLose = true;
                                plane.life = 0;
                            }
                        }
                    }
                }
            }
            this.postInvalidate();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //下面是根据位图Bitmap把图片转换成了数据流
    Bitmap BBULLET = ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.bb)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.bb)).getBitmap() :null;//利用位图转换数据
    Bitmap EBULLET = ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.eb)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.eb)).getBitmap() :null;
    Bitmap PBULLET = ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.pb)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.pb)).getBitmap() :null;
    Bitmap BOSS = ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.boss)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.boss)).getBitmap() :null;
    Bitmap ENEMY = ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.enemy)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.enemy)).getBitmap() : null;
    Bitmap BOO = ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.boo)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.boo)).getBitmap() : null;
   // Bitmap BACK1 = ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.back3)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.back3)).getBitmap() : null;
    //Bitmap BACK = ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.back)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.back)).getBitmap() : null;
    Bitmap TREA1 = ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.trea1)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.trea1)).getBitmap() : null;
    Bitmap TREA2 = ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.trea2)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.trea2)).getBitmap() : null;
    Bitmap TREA3 = ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.trea3)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.trea3)).getBitmap() : null;
    //img_bg = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bg1);
    protected void onDraw(Canvas canvas) {//绘图函数，就是动画的绘图了
        super.onDraw(canvas);
        // scrooling

     /*   canvas.drawBitmap(img_bg, g_x1, g_y1, new Paint());
        canvas.drawBitmap(img_bg, g_x2, g_y2, new Paint());*/

        canvas.drawBitmap(img_bg, null,new Rect(0,g_y1,Screen_w,g_y1+Screen_h), new Paint());
        canvas.drawBitmap(img_bg, null,new Rect(0,g_y2,Screen_w,g_y2+Screen_h), new Paint());
        Log.w("drawBitmap",g_y1+"____________________"+g_y2);
        // repeat
        if(!isWin&&!isLose){
            if (g_y1 > g_bgH)
                g_y1 = g_y2 - g_bgH;
            if (g_y2 > g_bgH)
                g_y2 = g_y1 - g_bgH;
            g_t += 0.016;
            g_y1 += g_bg_speed;
            g_y2 += g_bg_speed;
            ++g_c;
        }

        //Log.d(TAG, "onDraw: " + g_t);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        //画背景
       // canvas.drawBitmap(BACK,null,new Rect(0,background-2100,0+Screen_w,background+2100),paint);
        //canvas.drawBitmap(BACK1,null,new Rect(0,background1-2100,0+Screen_w,background1+2100),paint);
        //显示己方生命值
        paint.setColor(Color.YELLOW);
        paint.setTextSize(40);
        canvas.drawText("HP:",0,Screen_h-50,paint);
        canvas.drawRect(80,Screen_h-100,80+plane.life*22,Screen_h-50,paint);
        //显示BOSS生命值
        if(boss.visual!=0) {
            paint.setColor(Color.RED);
            paint.setTextSize(40);
            canvas.drawText("BH:", 0, Screen_h - 1700, paint);
            canvas.drawRect(80, Screen_h - 1750, 80 + boss.life * 5, Screen_h - 1700, paint);
        }
        // 绘制导弹按钮
        if (missileCount > 0) {
            paint.setTextSize(40);
            paint.setColor(Color.BLACK);
            canvas.drawBitmap(missile_bt, 10, missile_bt_y, paint);
            canvas.drawText("X " + String.valueOf(missileCount),
                    10 + missile_bt.getWidth(), Screen_h - 25, paint);// 绘制文字
        }
        //画必杀技
        if (plane.getMissileState()) {
            float boom_x = plane.x- boom.getWidth() / 2+plane.width/2;
            float boom_y = plane.y - boom.getHeight() / 2+plane.height/2;
            canvas.drawBitmap(boom, boom_x, boom_y, paint);
            Update(g_c);
            AnimationManager.Render(canvas);

        }
        //画己方飞机
        if (((BitmapDrawable) this.getResources().getDrawable(R.mipmap.plane)) != null) {
            Bitmap HERO = ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.plane)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.mipmap.plane)).getBitmap() : null;
            canvas.drawBitmap(HERO,null,new Rect(plane.x,plane.y,plane.x+plane.width,plane.y+plane.height),paint);
        }
        //画己方子弹
        paint.setColor(Color.YELLOW);
        for( int j=0;j<=49;j++) {
            if(pb[j].visual==1) {
                if(strBullet==0){
                    paint.setColor(Color.YELLOW);
                }else{
                    paint.setColor(Color.rgb(0, 255, 255));
                }
                canvas.drawBitmap(PBULLET,null,new Rect(pb[j].x,pb[j].y,pb[j].x+pb[j].width,pb[j].y+pb[j].height),paint);
            }
            if(pb[j].boo==2){
                canvas.drawBitmap(BOO,null,new Rect(pb[j].x-20,pb[j].y-20,pb[j].x + pb[j].width+20,pb[j].y + pb[j].height+20),paint);
                pb[j].boo--;
            }
            if(pb[j].boo==1){
                canvas.drawBitmap(BOO,null,new Rect(pb[j].x-40,pb[j].y-40,pb[j].x + pb[j].width+40,pb[j].y + pb[j].height+40),paint);
                pb[j].boo--;
            }
        }
        //画我方的副武器
        paint.setColor(Color.YELLOW);
        for(int j=0;j<=49;j++) {
            if(weapons[j].visual==1) {
                canvas.drawBitmap(vice_weapon,null,new Rect(weapons[j].x,weapons[j].y,weapons[j].x+weapons[j].width,weapons[j].y+weapons[j].height),paint);
            }
            if(weapons[j].boo==2){
                canvas.drawBitmap(BOO,null,new Rect(weapons[j].x-30,weapons[j].y-30,weapons[j].x+weapons[j].width+30,weapons[j].y+weapons[j].height+30),paint);
                weapons[j].boo--;
            }
            if(weapons[j].boo==1){
                canvas.drawBitmap(BOO,null,new Rect(weapons[j].x-60,weapons[j].y-60,weapons[j].x+weapons[j].width+60,weapons[j].y+weapons[j].height+60),paint);
                weapons[j].boo--;
            }
        }
        //画宝物
        for(int i=0;i<=65;i++){
            if(treasure[i].visual==1){
                if(treasure[i].varible==1){
                    canvas.drawBitmap(TREA1,null,new Rect(treasure[i].x,treasure[i].y,treasure[i].x+treasure[i].width,treasure[i].y+treasure[i].height),paint);
                }else if(treasure[i].varible==2) {
                    canvas.drawBitmap(TREA2,null,new Rect(treasure[i].x,treasure[i].y,treasure[i].x+treasure[i].width,treasure[i].y+treasure[i].height),paint);
                }else if(treasure[i].varible==3){
                    canvas.drawBitmap(TREA3,null,new Rect(treasure[i].x,treasure[i].y,treasure[i].x+treasure[i].width,treasure[i].y+treasure[i].height),paint);
                }else if(treasure[i].varible==4){
                    canvas.drawBitmap(missile_goods,null,new Rect(treasure[i].x,treasure[i].y,treasure[i].x+treasure[i].width,treasure[i].y+treasure[i].height),paint);
                }
            }
        }
        //画敌机
        for(j=0;j<=40;j++){
            if(enemy[j].visual==1){
                canvas.drawBitmap(ENEMY,null,new Rect(enemy[j].x,enemy[j].y,enemy[j].x+enemy[j].width,enemy[j].y+enemy[j].height),paint);
            }
            if(enemy[j].boo!=0){
                switch (enemy[j].boo){//爆炸的种类
                    case 3:
                        canvas.drawBitmap(BOO,null,new Rect(enemy[j].x+20,enemy[j].y+20,enemy[j].x+enemy[j].width-20,enemy[j].y+enemy[j].height-20),paint);
                        enemy[j].boo--;
                        break;
                    case 2:
                        canvas.drawBitmap(BOO,null,new Rect(enemy[j].x+10,enemy[j].y+10,enemy[j].x+enemy[j].width-10,enemy[j].y+enemy[j].height-10),paint);
                        enemy[j].boo--;
                        break;
                    case 1:
                        canvas.drawBitmap(BOO,null,new Rect(enemy[j].x,enemy[j].y,enemy[j].x+enemy[j].width,enemy[j].y+enemy[j].height),paint);
                        enemy[j].boo--;
                        break;

                }
            }
        }
        //画敌机子弹
        for(i=1;i<=145;i++){
            if(eb[i].visual==1){
                canvas.drawBitmap(EBULLET,null,new Rect(eb[i].x,eb[i].y,eb[i].x+eb[i].width,eb[i].y+eb[i].height),paint);
            }
        }
        //画BOSS
        if(boss.visual!=0){
            if(boss.life>=4) {
                canvas.drawBitmap(BOSS, null, new Rect(boss.x, boss.y, boss.x + boss.width, boss.y + boss.height), paint);
            }else {
                switch (boss.life) {
                    case 3: canvas.drawBitmap(BOO,null,new Rect(boss.x,boss.y,boss.x+boss.width,boss.y+boss.height),paint);
                        boss.life--;
                        break;
                    case 2: canvas.drawBitmap(BOO,null,new Rect(boss.x,boss.y,boss.x+boss.width,boss.y+boss.height),paint);
                        boss.life--;
                        break;
                    case 1: canvas.drawBitmap(BOO,null,new Rect(boss.x,boss.y,boss.x+boss.width,boss.y+boss.height),paint);
                        boss.life--;
                        break;
                }
            }
        }
        //画BOSS子弹
        for(i=1;i<=145;i++){
            if(bb[i].visual==1){
                paint.setColor(Color.RED);
                canvas.drawBitmap(BBULLET,null,new Rect(bb[i].x,bb[i].y,bb[i].x+bb[i].width,bb[i].y+bb[i].height),paint);
            }
        }
        //如果失败
        if(isLose){
            invalidate();
            paint.setColor(Color.RED);
            paint.setTextSize(90);
            canvas.drawText("Game Over",350,800,paint);//DrawText是显示界面的内容，游戏结束，并且出现在（X,Y）坐标轴的位置上。
            canvas.drawBitmap(BOO,null,new Rect(plane.x,plane.y,plane.x+plane.width,plane.y+plane.height),paint);
            score=numOfDestroy*2;
            canvas.drawText("最终得分:"+score,350,900,paint);//设置红底70的字位置在（350,900）
        }
        //如果胜利
        if(isWin){
            invalidate();
            paint.setColor(Color.YELLOW);
            paint.setTextSize(90);
            canvas.drawText("You Win!",350,800,paint);
            paint.setColor(Color.RED);
            paint.setTextSize(70);
            score=numOfDestroy*2+30;
            canvas.drawText("最终得分:"+score,350,900,paint);//设置红底70的字位置在（350,900）
        }

    }

    private void Update(long c) {
        if (c % 20 == 0 ){
            ImageAnimation boom = new BoomAnimation();
            boom.Init(
                    new Random().nextInt(600)+10,
                    new Random().nextInt(1200)+10,
                    new Random().nextInt(5)+1);

            AnimationManager.PushAnimation(boom);
        }


    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    super.handleMessage(msg);
                    invalidate();
                    break;
            }
        }
    };
}
