package com.example.swaiter;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class SttActivity extends AppCompatActivity {
    Intent voice_intent;
    SpeechRecognizer recognizer;
    TextView textView_stt;

    private static String TAG = "0509";
    private static final int RECORD_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stt_layout);

        Intent result_intent = getIntent();

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Permission to access the microphone is required for this app to record audio");
                builder.setTitle("Permission Required");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "Clicked");
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

        voice_intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voice_intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        voice_intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recognizer.setRecognitionListener(recognitionListener);

        Button button_inputVoice = (Button) findViewById(R.id.button_inputVoice);
        textView_stt = (TextView) findViewById(R.id.textView_stt);

        button_inputVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recognizer.startListening(voice_intent);
            }
        });
    }

    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            textView_stt.setText("...");
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
            textView_stt.setText("새우 들어간 버거");
            gotoResult_layout();
        }

        @Override
        public void onResults(Bundle results) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> result = results.getStringArrayList(key);
            String[] rs = new String[result.size()];
            result.toArray(rs);
            textView_stt.setText(rs[0]);

            gotoResult_layout();
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

    private void gotoResult_layout() {
        Intent result_intent = new Intent (SttActivity.this, ResultActivity.class);
        result_intent.putExtra("text", String.valueOf(textView_stt.getText()));
        startActivity(result_intent);
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, RECORD_REQUEST_CODE);
    }
}
