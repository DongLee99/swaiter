package com.example.swaiter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class settingActivity extends AppCompatActivity {

    Button button;
    EditText editText;
    info info = new info();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        editText = (EditText) findViewById(R.id.editText_url);
        editText.setText(info.getUrl());

        button = (Button) findViewById(R.id.button_urlchange);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setUrl(editText.getText().toString());
            }
        });
    }

}
