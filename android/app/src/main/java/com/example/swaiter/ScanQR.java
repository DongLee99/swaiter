package com.example.swaiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanQR extends AppCompatActivity {

    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false);
        qrScan.setBeepEnabled(false);
        qrScan.setPrompt("QR 코드를 스캔해주세요");
        qrScan.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ScanQR.this, MainActivity.class);
                startActivity(intent);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
