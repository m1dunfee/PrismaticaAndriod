package com.prismatica.iotInterface.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.prismatica.iotInterface.R;
import com.prismatica.iotInterface.data.UserRepository;

public class LoginSignup extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button btnSubmit;
    private Button btnToggleMode;
    private TextView tvMode;
    private boolean signupMode;
    private UserRepository users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        users = new UserRepository(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnToggleMode = findViewById(R.id.btnToggleMode);
        tvMode = findViewById(R.id.tvMode);

        signupMode = false;
        updateModeLabel();

        btnToggleMode.setOnClickListener(v -> {
            signupMode = !signupMode;
            updateModeLabel();
        });

        btnSubmit.setOnClickListener(v -> onSubmit());
    }

    private void updateModeLabel() {
        tvMode.setText(signupMode ? "Sign Up" : "Log In");
        btnSubmit.setText(signupMode ? "Create Account" : "Log In");
        btnToggleMode.setText(signupMode ? "Switch to Log In" : "Switch to Sign Up");
    }

    private void onSubmit() {
        String u = etUsername.getText().toString().trim();
        String p = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(u) || TextUtils.isEmpty(p)) {
            Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean ok;
        if (signupMode) {
            ok = users.createUser(u, p);
            if (!ok) {
                Toast.makeText(this, "User exists or invalid", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show();
        } else {
            ok = users.checkLogin(u, p);
            if (!ok) {
                Toast.makeText(this, "Invalid login", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Intent i = new Intent(this, DeviceList.class);
        i.putExtra("username", u);
        startActivity(i);
        finish();
    }
}
