package com.wlw2_11.planefight.Animation;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by Administrator on 2018/7/16 0016.
 */

public abstract  class ImageAnimation {

    public abstract  void initRes(Context context);

    public abstract void Init(int x, int y, int duration);

    public abstract boolean Render(Canvas canvas);

}
