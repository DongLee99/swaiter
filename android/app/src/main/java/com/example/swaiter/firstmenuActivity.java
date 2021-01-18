package com.example.swaiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class firstmenuActivity extends AppCompatActivity {

    private Button scanQRbtn;
    private Button button_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstmenu_layout);

        scanQRbtn = (Button) findViewById(R.id.button_scanQR);
        scanQRbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(firstmenuActivity.this, ScanQR.class);
                startActivity(intent);
            }
        });

        button_setting = (Button) findViewById(R.id.button_setting);
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(firstmenuActivity.this, settingActivity.class);
                startActivity(intent);
            }
        });
    }
}
