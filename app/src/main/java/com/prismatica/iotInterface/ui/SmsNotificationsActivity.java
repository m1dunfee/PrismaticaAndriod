package com.prismatica.iotInterface.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.prismatica.iotInterface.R;

public class SmsNotificationsActivity extends AppCompatActivity {
    private ActivityResultLauncher<String> smsPermLauncher;
    private boolean smsGranted;

    // NEW: bind your status views
    private TextView tvPermissionStatus, tvFeedback;
    private Button btnSend;
    private CheckBox cbLowInventory;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_sms_notifications);

        Button btnRequest = findViewById(R.id.btnRequestPermission);
        btnSend           = findViewById(R.id.btnSendTest);
        tvPermissionStatus = findViewById(R.id.tvPermissionStatus);
        tvFeedback         = findViewById(R.id.tvFeedback);
        cbLowInventory     = findViewById(R.id.cbLowInventory);

        smsPermLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    smsGranted = granted;
                    updateUi();
                    Toast.makeText(this, granted ? "SMS permission granted" : "SMS permission denied", Toast.LENGTH_SHORT).show();
                });

        smsGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;
        updateUi(); // reflect initial state

        btnRequest.setOnClickListener(v -> {
            if (smsGranted) {
                Toast.makeText(this, "Already granted", Toast.LENGTH_SHORT).show();
            } else {
                smsPermLauncher.launch(Manifest.permission.SEND_SMS);
            }
        });

        btnSend.setOnClickListener(v -> {
            if (!smsGranted) {
                tvFeedback.setText("Permission not granted; skipping SMS.");
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                // Emulator tip: send to a second emulator, e.g., 5556 if this one is 5554.
                String phone = "5556";
                String msg = "Prismatica alert (test): Low inventory=" + cbLowInventory.isChecked();
                SmsManager.getDefault().sendTextMessage(phone, null, msg, null, null);
                tvFeedback.setText("SMS sent to " + phone + ": " + msg);
                Toast.makeText(this, "SMS sent", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                tvFeedback.setText("SMS failed: " + ex.getMessage());
                Toast.makeText(this, "SMS failed: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // NEW: simple UI state helper
    private void updateUi() {
        tvPermissionStatus.setText(smsGranted ? "Permission: GRANTED" : "Permission: DENIED");
        btnSend.setEnabled(smsGranted);
    }
}
