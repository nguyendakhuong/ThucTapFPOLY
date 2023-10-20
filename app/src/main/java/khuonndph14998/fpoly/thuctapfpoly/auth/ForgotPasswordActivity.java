package khuonndph14998.fpoly.thuctapfpoly.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import khuonndph14998.fpoly.thuctapfpoly.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    private Button btnReset,btnBack;
    private EditText ed_email;
    FirebaseAuth mAuth;
    String EMAIL_REGEX = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        anhxa();
        mAuth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = ed_email.getText().toString().trim();
                if (!TextUtils.isEmpty(strEmail)){
                    ResetPassword(strEmail);
                }else {
                    Toast.makeText(ForgotPasswordActivity.this, "Vùi lòng nhập email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void anhxa(){
        ed_email = findViewById(R.id.input_confirmPassword_email);
        btnBack = findViewById(R.id.btn_confirmPassword_back);
        btnReset = findViewById(R.id.btn_confirmPassword_reset);
    }
    private void ResetPassword(String strEmail) {
        if (!strEmail.matches(EMAIL_REGEX)){
            Toast.makeText(ForgotPasswordActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.sendPasswordResetEmail(strEmail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ForgotPasswordActivity.this, "Vui lòng vào email xác thực link đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgotPasswordActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}