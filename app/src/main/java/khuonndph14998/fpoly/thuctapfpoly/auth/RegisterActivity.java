package khuonndph14998.fpoly.thuctapfpoly.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import khuonndph14998.fpoly.thuctapfpoly.MainActivity;
import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.admin.CreateAdminActivity;
import khuonndph14998.fpoly.thuctapfpoly.model.User;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword,editTextComfirmPassword, editTextName , editTextPhone;
    private Button btnRegister;
    private TextView textToLogin,textAdmin;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        anhxa();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextComfirmPassword.getText().toString().trim();
                String name = editTextName.getText().toString().trim();
                String EMAIL_REGEX = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
                String id = UUID.randomUUID().toString();
                String roles = "1" ;

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterActivity.this, "Nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name)){
                    Toast.makeText(RegisterActivity.this, "Nhập tên", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Nhập passwrod", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(RegisterActivity.this, "Nhập confirmPassword", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!email.matches(EMAIL_REGEX)){
                    Toast.makeText(RegisterActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6 ){
                    Toast.makeText(RegisterActivity.this, "Mật khẩu tối thiểu phải 6 kí tự", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser user = fAuth.getCurrentUser();
                                if (user != null) {
                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công. Vui lòng kiểm tra email để xác thực tài khoản.", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                            } else {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, "Tài khoản đã được đăng ký", Toast.LENGTH_SHORT).show();
                            }
                        });
                FirebaseAuth.getInstance().addIdTokenListener(new FirebaseAuth.IdTokenListener() {
                    @Override
                    public void onIdTokenChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            DocumentReference df = fstore.collection("account").document(user.getUid());
                            User userObj = new User();
                            userObj.setId(id);
                            userObj.setEmail(email);
                            userObj.setFullname(name);
                            userObj.setRoles(roles);
                            df.set(userObj);

//                            finish();
                        }
                    }
                });
            }
        });
        textToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    private void anhxa() {
        editTextEmail = findViewById(R.id.input_register_email);
        editTextPassword = findViewById(R.id.input_register_password);
        editTextComfirmPassword = findViewById(R.id.input_register_Confirmpassword);
        btnRegister = findViewById(R.id.btn_register);
        textToLogin = findViewById(R.id.text_toLogin);
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressbarRegister);
        textAdmin = findViewById(R.id.text_toAdmin);
        editTextName = findViewById(R.id.input_register_name);
    }

}
