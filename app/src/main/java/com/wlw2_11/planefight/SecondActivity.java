package com.wlw2_11.planefight;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class SecondActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SoundPool soundPool =new SoundPool(3 , AudioManager.STREAM_MUSIC
                ,5);
        final HashMap musicId = new HashMap();
        musicId.put(1,soundPool.load(this,R.raw.beffect,1));
        final Intent intent = new Intent(this,mainMusic.class);
        startService(intent);
        Button button=(Button)findViewById(R.id.button);//选择不同的按钮
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    soundPool.play((Integer) musicId.get(1),1,1, 0, 0, 1);
                    Intent intent=new Intent(SecondActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            });
        }

        Button button1=(Button)findViewById(R.id.button1);
        if(button1 != null){
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent =new Intent(this,mainMusic.class);
        stopService(intent);
    }
}
