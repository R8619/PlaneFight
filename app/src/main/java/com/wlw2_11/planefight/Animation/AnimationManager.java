package com.wlw2_11.planefight.Animation;

import android.content.Context;
import android.graphics.Canvas;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Administrator on 2018/7/16 0016.
 */

public class AnimationManager {
    public static Context context;
    public static void Init(Context c){
        context = c;
    }

    static public Queue<ImageAnimation> ani_render = new LinkedList<ImageAnimation>(); // boom

    static public void PushAnimation(ImageAnimation ani) {
        ani.initRes(context);
        ani_render.offer(ani);
    }


    static public void Render(Canvas canvas) {
        Iterator<ImageAnimation> iter = ani_render.iterator();

        while (iter.hasNext()) {
            ImageAnimation s = iter.next();
            if (s.Render(canvas)) {
                iter.remove();
            }
        }
    }
}
