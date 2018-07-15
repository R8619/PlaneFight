package com.wlw2_11.planefight;

import android.content.res.Resources;
import android.graphics.BitmapFactory;



/**
 * 导弹物品
 */
public class MissileGoods extends GameGoods {
    public MissileGoods(Resources resources) {
        super(resources);
    }

    @Override
    protected void initBitmap() {
        bmp = BitmapFactory.decodeResource(resources,R.mipmap.missile_goods);
        object_width = bmp.getWidth();
        object_height = bmp.getHeight();
    }
}
