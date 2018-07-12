package com.wlw2_11.planefight.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wlw2_11.planefight.MainActivity;
import com.wlw2_11.planefight.R;

public class SelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Button button=(Button)findViewById(R.id.button);//选择不同的按钮
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent=new Intent(SelectActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }

        Button button1=(Button)findViewById(R.id.button1);//选择不同的按钮
        if (button1 != null) {
            button1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent=new Intent(SelectActivity.this,Main1Activity.class);
                    startActivity(intent);
                }
            });
        }

        Button button2=(Button)findViewById(R.id.button2);//选择不同的按钮
        if (button2 != null) {
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent=new Intent(SelectActivity.this,Main2Activity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
