package com.wlw2_11.planefight.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Display;
import android.view.View;

import com.wlw2_11.planefight.Bullet.BBullet;
import com.wlw2_11.planefight.Bullet.EBullet;
import com.wlw2_11.planefight.Bullet.PBullet;
import com.wlw2_11.planefight.Goods.Treasure;
import com.wlw2_11.planefight.Object.Boss;
import com.wlw2_11.planefight.Object.Enemy;
import com.wlw2_11.planefight.Object.Plane;
import com.wlw2_11.planefight.Object.Strenemy;
import com.wlw2_11.planefight.R;

import java.util.Random;

/**
 * Created by 任建康 on 2018/7/9.
 */

public class MainGame1 extends View implements Runnable{
    public static int Screen_w;
    public static int Screen_h;
    public static Paint paint;
    public static int Point_x=0;
    public static int Point_y=0;
    public static boolean isdown;
    public int ide;//敌机编号
    public int idse;
    public int numOfDestroy=0;
    public int ideb=1;//敌机子弹编号
    public int numOfEnemy;
    public int numOfStrenemy;
    public int i;
    public int j;
    public int strBullet=0;
    public int doubleBullet=0;
    public int idOfTreasure;
    public boolean isWin=false;
    public int temp=1;
    public int id=0;
    public int background;
    public int background1;
    public boolean isLose=false;
    public int score=0;
    Random rand;
    Plane plane;
    Boss boss;
    Thread thread;
    Treasure[] treasure;
    PBullet[] pb;
    EBullet[] eb;
    BBullet[] bb;
    Enemy[] enemy;
    Strenemy[] strenemy;
    public MainGame1(Context context, Display display) {//初始化
        super(context);
        Screen_w = display.getWidth();//获取屏幕的宽
        Screen_h = display.getHeight();///获取屏幕的高
        paint = new Paint();
        ide=1;//初始化敌机编号
        idse=1;
        idOfTreasure=0;
        numOfEnemy=0;
        numOfStrenemy=0;
        plane=new Plane();//新建飞机
        pb=new PBullet[50];//新建子弹
        eb=new EBullet[160];
        bb=new BBullet[160];
        enemy=new Enemy[50];//新建敌机
        strenemy=new Strenemy[50];
        treasure=new Treasure[70];//新建宝物
        boss=new Boss();//新建Boss
        for(i=0;i<=49;i++){
            pb[i]=new PBullet();
            enemy[i]=new Enemy();
            strenemy[i]=new Strenemy();
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
        rand=new Random();
        background=0;
        this.thread = new Thread(this);//线程
        this.thread.start();//线程开始
    }
    public void run() {
        //飞机活动
        while ((!isLose)&&(!isWin)) {//如果没有赢并且没有输，那么就会执行以下的程序
            background+=10;//背景移动
            background1+=10;
            if(background>=2100){
                background=background1-2100;
            }else if(background1>=2100){
                background1=background-2100;
            }
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
            //产生加强版敌机
            if(boss.visual==0){//如果BOSS不出现
                if(enemy[ide].visual==0) {
                    //如果是第一个
                    if (idse == 1) {
                        strenemy[idse].visual = 1;
                        strenemy[idse].y = 0;
                        idse++;//敌机编号加1
                        numOfStrenemy++;//敌机数目加1
                    } else if (idse > 0 && idse <= 39) {//可知敌机的数目只有39架，可以回头测试数目
                        if (strenemy[idse-1].y >= 250) {//如果上一架飞机Y轴的位置大于了400
                            strenemy[idse].visual = 1;//出现新飞机，也就是说每300的Y轴出现一架飞机，飞机的间距是已经设置好的。
                            strenemy[idse].y = 0;//初始时Y轴的位置
                            idse++;//编号+1
                            numOfStrenemy++;//敌机总数+1
                        }
                    }
                }
                if(idse==40){
                    strenemy[idse].visual = 1;
                    strenemy[idse].y=0;
                    idse=1;
                    strenemy[idse].y=0;
                    numOfStrenemy++;
                }
            }
            //敌机移动
            for(i=1;i<=40;i++) {
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
            //加强版敌机移动
            for(i=1;i<=40;i++) {
                strenemy[i].y += strenemy[i].v;
                if (strenemy[i].y >= Screen_h) {
                    strenemy[i].visual = 0;//敌机消失
                }
            }
            //敌机产生子弹
            for(i=1;i<=40;i++){
                if(enemy[i].visual==1){
                    if((enemy[i].y>=100+i*40&&enemy[i].y<=110+i*40 && enemy[i].v>0)||(enemy[i].y>=300&&enemy[i].y<=310)){
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
            for(i=1;i<=145;i++){
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
                temp++;
                if(temp==5){
                    if(pb[id].visual==0) {
                        if(doubleBullet>=1){
                            pb[id].visual = 1;
                            pb[id].x = plane.x + plane.width / 2+20;
                            pb[id].y = plane.y;
                            pb[id].v = 40;
                            if (id <= 48) {
                                id++;
                            } else {
                                id = 0;
                            }
                            pb[id].visual = 1;
                            pb[id].x = plane.x + plane.width / 2-20;
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
                            pb[id].y = plane.y;
                            pb[id].v = 40;
                            if (id <= 48) {
                                id++;
                            } else {
                                id = 0;
                            }
                        }
                        temp = 1;
                    }
                }
                //触摸移动飞机
                if (Point_x >= plane.x + plane.width / 2) {
                    plane.x += (Point_x-plane.x-plane.width / 2)/4;
                } else {
                    plane.x -= (-Point_x+plane.x+plane.width / 2)/4;
                }
                if (Point_y >= plane.y + plane.height / 2) {
                    plane.y += (Point_y-plane.y-plane.height / 2)/4;
                } else {
                    plane.y -= (-Point_y+plane.y+plane.height / 2)/4;
                }
                //边界检测
                if(plane.x<=0){//高度检测
                    plane.x=0;
                }
                if(plane.x+plane.width>=Screen_w){//宽度检测
                    plane.x=Screen_w-plane.width;
                }
            }
            //子弹移动
            for(i=0;i<=49;i++) {
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
            for(i=0;i<=49;i++) {
                for(j=1;j<=40;j++){
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
                            }
                        }
                    }
                }
            }
            //子弹打到加强版敌机
            for(i=0;i<=49;i++){
                for(j=1;j<=40;j++){
                    if(pb[i].visual==1 && strenemy[j].visual==1){
                        if(pb[i].x>=strenemy[j].x&&pb[i].x<=strenemy[j].x+strenemy[j].width){
                            if (pb[i].y <=strenemy[j].y + strenemy[j].height&&pb[i].y>=strenemy[j].y) {//子弹的纵坐标介于敌机的Y的坐标和其Y坐标加上其长度
                                strenemy[j].life-=2;
                                strenemy[j].visual = 0;
                                pb[i].visual = 0;
                                strenemy[j].boo = 3;
                                numOfDestroy++;

                            }
                        }
                    }
                }

            }
            //宝物移动
            for(i=0;i<=65;i++){
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
                            }
                            if(treasure[i].varible==3){
                                plane.life+=4;
                            }
                        }
                    }
                }
            }
            //子弹打到BOSS
            if(boss.visual!=0&&(boss.life>=4)) {
                for (i = 0; i <= 49; i++) {
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
            for(i=1;i<=145;i++){
                if(eb[i].visual==1){
                    if(eb[i].x-5>=plane.x&&eb[i].x+5<=plane.x+plane.width){
                        if (eb[i].y-5>=plane.y&&eb[i].y+5<=plane.y+plane.height){
                            plane.life-=2;
                            {//掉血后重置子弹
                                strBullet=0;
                                doubleBullet=0;
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
            for(i=1;i<=145;i++){
                if(bb[i].visual==1){
                    if(bb[i].x-5>=plane.x&&bb[i].x+5<=plane.x+plane.width){
                        if (bb[i].y-5>=plane.y&&bb[i].y+5<=plane.y+plane.height){
                            plane.life-=2;
                            {//死亡后重置子弹
                                strBullet=0;
                                doubleBullet=0;
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
            for(j=1;j<=40;j++){
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
            //与加强版飞机相撞
            for(j=1;j<=40;j++){
                if(strenemy[j].visual==1) {
                    if (strenemy[j].x+strenemy[j].width-5>= plane.x && strenemy[j].x+5<= plane.x + plane.width) {
                        if(strenemy[j].y+strenemy[j].height>= plane.y&&strenemy[j].y<=plane.y+plane.height) {
                            isLose = true;
                            plane.life=0;
                            strenemy[j].life = 0;
                            strenemy[j].visual = 0;
                            strenemy[j].boo = 2;
                            numOfDestroy++;
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
    Bitmap BBULLET = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.bb)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.drawable.bb)).getBitmap() :null;//利用位图转换数据
    Bitmap EBULLET = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.eb)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.drawable.eb)).getBitmap() :null;
    Bitmap PBULLET = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.pb)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.drawable.pb)).getBitmap() :null;
    Bitmap BOSS = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.boss)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.drawable.boss)).getBitmap() :null;
    Bitmap ENEMY = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.enemy)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.drawable.enemy)).getBitmap() : null;
    Bitmap STRENEMY = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.strenemy)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.drawable.strenemy)).getBitmap() : null;
    Bitmap BOO = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.boo)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.drawable.boo)).getBitmap() : null;
    Bitmap BACK1 = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.back3)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.drawable.back3)).getBitmap() : null;
    Bitmap BACK = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.back)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.drawable.back)).getBitmap() : null;
    Bitmap TREA1 = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.trea1)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.drawable.trea1)).getBitmap() : null;
    Bitmap TREA2 = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.trea2)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.drawable.trea2)).getBitmap() : null;
    Bitmap TREA3 = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.trea3)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.drawable.trea3)).getBitmap() : null;
    protected void onDraw(Canvas canvas) {//绘图函数，就是动画的绘图了
        super.onDraw(canvas);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        //画背景
        canvas.drawBitmap(BACK,null,new Rect(0,background-2100,0+Screen_w,background+2100),paint);
        canvas.drawBitmap(BACK1,null,new Rect(0,background1-2100,0+Screen_w,background1+2100),paint);
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
        //画己方飞机
        if (((BitmapDrawable) this.getResources().getDrawable(R.drawable.plane)) != null) {
            Bitmap HERO = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.plane)) != null ? ((BitmapDrawable) this.getResources().getDrawable(R.drawable.plane)).getBitmap() : null;
            canvas.drawBitmap(HERO,null,new Rect(plane.x,plane.y,plane.x+plane.width,plane.y+plane.height),paint);
        }
        //画己方子弹
        paint.setColor(Color.YELLOW);
        for(j=0;j<=49;j++) {
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
        //画宝物
        for(i=0;i<=65;i++){
            if(treasure[i].visual==1){
                if(treasure[i].varible==1){
                    canvas.drawBitmap(TREA1,null,new Rect(treasure[i].x,treasure[i].y,treasure[i].x+treasure[i].width,treasure[i].y+treasure[i].height),paint);
                }else if(treasure[i].varible==2) {
                    canvas.drawBitmap(TREA2,null,new Rect(treasure[i].x,treasure[i].y,treasure[i].x+treasure[i].width,treasure[i].y+treasure[i].height),paint);
                }else if(treasure[i].varible==3){
                    canvas.drawBitmap(TREA3,null,new Rect(treasure[i].x,treasure[i].y,treasure[i].x+treasure[i].width,treasure[i].y+treasure[i].height),paint);
                }
            }
        }
        //画敌机
        for(j=0;j<=40;j++){
            if(enemy[j].visual==1){
                canvas.drawBitmap(ENEMY,null,new Rect(enemy[j].x,enemy[j].y,enemy[j].x+enemy[j].width,enemy[j].y+enemy[j].height),paint);
            }
            if(strenemy[j].visual==1){
                canvas.drawBitmap(STRENEMY,null,new Rect(strenemy[j].x,strenemy[j].y,strenemy[j].x+strenemy[j].width,strenemy[j].y+strenemy[j].height),paint);
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
            if(strenemy[j].boo!=0){
                switch (strenemy[j].boo){//爆炸的种类
                    case 3:
                        canvas.drawBitmap(BOO,null,new Rect(strenemy[j].x+20,strenemy[j].y+20,strenemy[j].x+strenemy[j].width-20,strenemy[j].y+strenemy[j].height-20),paint);
                        strenemy[j].boo--;
                        break;
                    case 2:
                        canvas.drawBitmap(BOO,null,new Rect(strenemy[j].x+10,strenemy[j].y+10,strenemy[j].x+strenemy[j].width-10,strenemy[j].y+strenemy[j].height-10),paint);
                        strenemy[j].boo--;
                        break;
                    case 1:
                        canvas.drawBitmap(BOO,null,new Rect(strenemy[j].x,strenemy[j].y,strenemy[j].x+strenemy[j].width,strenemy[j].y+strenemy[j].height),paint);
                        strenemy[j].boo--;
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
}