package com.wlw2_11.planefight.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.wlw2_11.planefight.Game.MainGame2;
import com.wlw2_11.planefight.sounds.MusicServer;

public class Main2Activity extends AppCompatActivity implements View.OnTouchListener{//触摸监听事件{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = new Intent(this,MusicServer.class);
        startService(intent);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//显示全屏
        Init();
    }
    public void Init() {
        WindowManager wm = this.getWindowManager();//屏幕管理器
        Display display = wm.getDefaultDisplay();//获取默认的屏幕
        MainGame2 gameView = new MainGame2(this, display);//创建一个游戏视图
        gameView.setOnTouchListener(this);//新建一个游戏触摸视图
        this.setContentView(gameView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        final Intent intent = new Intent(this,MusicServer.class);
        stopService(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            MainGame2.isdown=true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP){
            MainGame2.isdown=false;
        }
        MainGame2.Point_x = (int) event.getRawX();
        MainGame2.Point_y = (int) event.getRawY();
        return true;
    }

}
