package com.prismatica.iotInterface.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.prismatica.iotInterface.R;

public class SmsNotificationsActivity extends AppCompatActivity {
    private ActivityResultLauncher<String> smsPermLauncher;
    private boolean smsGranted;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_sms_notifications);

        Button btnRequest = findViewById(R.id.btnRequestPermission);
        Button btnSend = findViewById(R.id.btnSendTest);

        smsPermLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    smsGranted = granted;
                    Toast.makeText(this, granted ? "SMS permission granted" : "SMS permission denied", Toast.LENGTH_SHORT).show();
                });

        smsGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;

        btnRequest.setOnClickListener(v -> {
            if (smsGranted) {
                Toast.makeText(this, "Already granted", Toast.LENGTH_SHORT).show();
            } else {
                smsPermLauncher.launch(Manifest.permission.SEND_SMS);
            }
        });

        btnSend.setOnClickListener(v -> {
            if (!smsGranted) {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                String phone = "5554"; // emulator
                String msg = "Prismatica alert: test SMS";
                SmsManager.getDefault().sendTextMessage(phone, null, msg, null, null);
                Toast.makeText(this, "SMS sent", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Toast.makeText(this, "SMS failed: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
