package com.example.swaiter;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class selectedActivity extends AppCompatActivity {

    private static final int RECORD_REQUEST_CODE = 101;

    ArrayList<MenuVO> selectedMenu;
    private RecyclerView selected_recyclerView;
    private SelectedMenuAdapter selectedMenuAdapter;
    private Context context;

    Intent voice_intent2;
    SpeechRecognizer recognizer2;

    DBManager dbManager;

    Button button_ask, button_totalprice;
    TextView textView_ask;

    int total_price=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingbag_layout);

        Intent intent = getIntent();

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (permission != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Permission to access the microphone is required for this app to record audio");
                builder.setTitle("Permission Required");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        makeRequest();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                makeRequest();
            }
        }

        voice_intent2 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voice_intent2.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        voice_intent2.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        recognizer2 = SpeechRecognizer.createSpeechRecognizer(this);
        recognizer2.setRecognitionListener(recognitionListener2);

        dbManager = DBManager.getInstance(this);

        context = selectedActivity.this;
        System.out.println(selectedMenu);
        getSelMenu();
        init();

        button_totalprice = (Button) findViewById(R.id.button_totalPrice);
        button_totalprice.setText(Integer.toString(total_price) + "원");
        button_totalprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_price=0;
                getSelMenu();
                init();
                button_totalprice.setText(Integer.toString(total_price) + "원");
            }
        });
        textView_ask = (TextView) findViewById(R.id.textView_ask);
        button_ask = (Button) findViewById(R.id.button_ask);
        button_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recognizer2.startListening(voice_intent2);
            }
        });

    }

    private void init() {
        selected_recyclerView = (RecyclerView) findViewById(R.id.rcc_select);
        RecyclerView.LayoutManager shopping_layoutManager = new GridLayoutManager(context, 1);
        selected_recyclerView.setLayoutManager(shopping_layoutManager);
        selectedMenuAdapter = new SelectedMenuAdapter(context, selectedMenu);
        selected_recyclerView.setAdapter(selectedMenuAdapter);

    }

    public void getSelMenu() {
        selectedMenu = new ArrayList<>();
        String[] colums = new String[] {"_id", "imgurl", "title", "option", "price"};
        Cursor cursor = dbManager.query(colums, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MenuVO menuVO = new MenuVO();
                menuVO.set_id(cursor.getInt(0));
                menuVO.setImgUrl(cursor.getString(1));
                menuVO.setTitle(cursor.getString(2));
                menuVO.setOption(cursor.getString(3));
                menuVO.setPrice(cursor.getString(4));

                int temp = Integer.parseInt(menuVO.getPrice());
                total_price = total_price + temp;
                selectedMenu.add(menuVO);
            }
        }
    }

    private RecognitionListener recognitionListener2 = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            textView_ask.setText("...");
        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            textView_ask.setText("덜 맵게 해주세요");
        }

        @Override
        public void onResults(Bundle results) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> result = results.getStringArrayList(key);
            String[] rs = new String[result.size()];
            result.toArray(rs);
            textView_ask.setText(rs[0]);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, RECORD_REQUEST_CODE);
    }
}
