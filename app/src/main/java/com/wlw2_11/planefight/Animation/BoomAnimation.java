package com.wlw2_11.planefight.Animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.wlw2_11.planefight.R;

/**
 * Created by Administrator on 2018/7/16 0016.
 */

public class BoomAnimation extends ImageAnimation {


    static int n = 17;
    int duration = 3; // 动画每一帧持续游戏多少帧
    int index;
    int count;
    int x, y;
    boolean isEnd; // 播放完毕
    static public Bitmap[] img = null;
    public void initRes(Context context){
        if (img != null)
            return;
        img = new Bitmap[n];
        img[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b1);
        img[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b2);
        img[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b3);
        img[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b4);
        img[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b5);
        img[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b6);
        img[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b7);
        img[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b8);
        img[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b9);
        img[9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b10);
        img[10] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b11);
        img[11] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b12);
        img[12] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b13);
        img[13] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b14);
        img[14] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b15);
        img[15] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b16);
        img[16] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b17);
    }

    public void Init(int x, int y, int duration){
        this.x = x;
        this.y = y;
        this.duration = duration;
        index = count = 0;
        isEnd = false;
    }

    public boolean Render(Canvas canvas){
        if(index < n){
            canvas.drawBitmap(img[index], x, y, new Paint());
            if(count++ != duration)
                return false;
            else {
                ++index;
                count = 0;
            }
        }else
            isEnd = true;

        return isEnd;
    }
}
