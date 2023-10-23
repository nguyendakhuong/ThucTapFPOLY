package khuonndph14998.fpoly.thuctapfpoly.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import khuonndph14998.fpoly.thuctapfpoly.CreateProductActivity;
import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.fragment.SettingFragment;
import khuonndph14998.fpoly.thuctapfpoly.model.InfoUser;

public class InfoUserActivity extends AppCompatActivity {
    private EditText ed_name,ed_phone,ed_address,ed_deliveryAddress;
    private Button btnConfirm;
    private RadioButton radioMale,radioFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);
        anhxa();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ed_name.getText().toString().trim();
                String phone = ed_phone.getText().toString().trim();
                String address = ed_address.getText().toString().trim();
                String deliveryAddress = ed_deliveryAddress.getText().toString().trim();
                String sex = "";
                if (radioMale.isChecked()) {
                    sex = "Nam";
                } else if (radioFemale.isChecked()) {
                    sex = "Nữ";
                }

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address) ||
                        TextUtils.isEmpty(deliveryAddress)) {
                    Toast.makeText(InfoUserActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isDigitsOnly(phone)) {
                    Toast.makeText(InfoUserActivity.this, "Số điện thoại phải là số", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phone.length() != 10 || !phone.startsWith("0")) {
                    Toast.makeText(InfoUserActivity.this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                int phoneNumber = Integer.parseInt(phone);
                InfoUser user = new InfoUser(name,address,deliveryAddress,sex,phoneNumber);
                String userEmail = getCurrentUserEmail();
                if (userEmail != null) {
                    String emailPath = userEmail.replace("@gmail.com", "");
                    String databasePath ="/Users/"+ emailPath + "/InfoUser";
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);
                    databaseReference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(InfoUserActivity.this, "Xác nhận thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(InfoUserActivity.this, "Lỗi hệ thống", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        getValue();
    }
    private String getCurrentUserEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getEmail();
        } else {
            return null;
        }
    }
    private void getValue(){
        String userEmail = getCurrentUserEmail();
        if (userEmail != null) {
            String emailPath = userEmail.replace("@gmail.com", "");
            String databasePath = "/Users/" + emailPath + "/InfoUser";
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        InfoUser user = dataSnapshot.getValue(InfoUser.class);
                        if (user != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ed_name.setText(String.valueOf(user.getName()));
                                    ed_address.setText(String.valueOf(user.getAddress()));
                                    ed_deliveryAddress.setText(String.valueOf(user.getDeliveryAddress()));
                                    ed_phone.setText(String.format("%010d", user.getPhone()));
                                    String gender = String.valueOf(user.getSex());
                                    if(gender.equals("Nam")){
                                        radioMale.setChecked(true);
                                    }
                                    if (gender.equals("Nữ")){
                                        radioFemale.setChecked(true);
                                    }
                                }
                            });

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    public void anhxa(){
        ed_name = findViewById(R.id.input_info_name);
        ed_phone = findViewById(R.id.input_info_numberPhone);
        ed_address = findViewById(R.id.input_info_address);
        ed_deliveryAddress = findViewById(R.id.input_info_deliveryAddress);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        btnConfirm = findViewById(R.id.btnConfirm);
    }
}