package khuonndph14998.fpoly.thuctapfpoly.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import khuonndph14998.fpoly.thuctapfpoly.MainActivity;
import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.auth.RegisterActivity;

public class CreateAdminActivity extends AppCompatActivity {
    private TextInputEditText emailAdmin,passwordAdmin;
    private Button btnAdmin;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_admin);

        anhxa();
        btnAdmin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = emailAdmin.getText().toString().trim();
                String password = passwordAdmin.getText().toString().trim();
                String emailPattern = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
                String id = UUID.randomUUID().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(CreateAdminActivity.this, "Nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(CreateAdminActivity.this, "Nhập passwrod", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!email.matches(emailPattern)){
                    Toast.makeText(CreateAdminActivity.this, "Chưa đúng định dạng email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6 ){
                    Toast.makeText(CreateAdminActivity.this, "Mật khẩu tối thiểu phải 6 kí tự", Toast.LENGTH_SHORT).show();
                    return;
                }
                fAuth.createUserWithEmailAndPassword(email,password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser user = fAuth.getCurrentUser();
                                if (user != null ) {
                                    Toast.makeText(CreateAdminActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                    DocumentReference df = fstore.collection("account").document(user.getUid());
                                    Map<String, Object> adminInfo = new HashMap<>();
                                    adminInfo.put("email", email);
                                    adminInfo.put("admin", "0");
                                    adminInfo.put("id",id);
                                    df.set(adminInfo);
                                    startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreateAdminActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });
    }

    private void anhxa() {
        emailAdmin = findViewById(R.id.input_admin_email);
        passwordAdmin = findViewById(R.id.input_admin_password);
        btnAdmin = findViewById(R.id.btn_admin);
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
    }
}