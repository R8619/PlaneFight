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

public class UtilAnimation extends ImageAnimation {
    static int n = 3;

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
        img[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.u1);
        img[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.u2);
        img[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.u3);

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
