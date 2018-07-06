package com.wlw2_11.planefight;

/**
 * Created by 10716 on 2018/7/6.
 */

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
public class MusicServer extends Service {
    private MediaPlayer mediaPlayer;
    @Override
    public IBinder onBind(Intent intent) {
// TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onStart(Intent intent,int startId){
        super.onStart(intent, startId);
        if(mediaPlayer==null){
// R.raw.mmp是资源文件，MP3格式的
            mediaPlayer = MediaPlayer.create(this, R.raw.bg);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}
