package com.wlw2_11.planefight.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.wlw2_11.planefight.R;


public class SecondActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=(Button)findViewById(R.id.button);//选择不同的按钮
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent=new Intent(SecondActivity.this,SelectActivity.class);
                    startActivity(intent);
                }
            });
        }

        Button button2=(Button)findViewById(R.id.button2);//选择不同的按钮
        if (button2 != null) {
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent=new Intent(SecondActivity.this,SettingsActivity.class);
                    startActivity(intent);
                }
            });
        }

        Button button3=(Button)findViewById(R.id.button3);//选择不同的按钮
        if (button3 != null) {
            button3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent=new Intent(SecondActivity.this,HelpActivity.class);
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
}
