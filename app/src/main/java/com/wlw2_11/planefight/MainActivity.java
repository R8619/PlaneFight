package com.wlw2_11.planefight;

import android.os.Bundle;

import android.app.Activity;
import android.graphics.Color;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//显示全屏
        Init();
    }
    public void Init() {
        WindowManager wm = this.getWindowManager();//屏幕管理器
        Display display = wm.getDefaultDisplay();//获取默认的屏幕
        MainGame gameView = new MainGame(this, display);//创建一个游戏视图
        gameView.setOnTouchListener(new touch());//新建一个游戏触摸视图
       // gameView.setBackgroundColor(Color.RED);//设置背景颜色
        this.setContentView(gameView);
    }
}
class touch implements View.OnTouchListener{//触摸监听事件
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            MainGame.isdown=true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP){
            MainGame.isdown=false;
        }
        MainGame.Point_x = (int) event.getRawX();
        MainGame.Point_y = (int) event.getRawY();
        return true;
    }
}
