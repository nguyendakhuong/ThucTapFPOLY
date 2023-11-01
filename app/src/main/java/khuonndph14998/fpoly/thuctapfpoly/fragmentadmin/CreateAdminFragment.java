package khuonndph14998.fpoly.thuctapfpoly.fragmentadmin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.adapter.AdminAdapter;
import khuonndph14998.fpoly.thuctapfpoly.adapter.UserAdapter;
import khuonndph14998.fpoly.thuctapfpoly.admin.AdminActivity;
import khuonndph14998.fpoly.thuctapfpoly.admin.CreateAdminActivity;
import khuonndph14998.fpoly.thuctapfpoly.model.Admin;
import khuonndph14998.fpoly.thuctapfpoly.model.User;

public class CreateAdminFragment extends Fragment {
    private TextInputEditText inputAdmin_search;
    private ImageView icon_admin_search,icon_admin_add;
    private RecyclerView rclView_admin;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fstore;
    private FirebaseFirestore db;
    private ArrayList<Admin> adminArrayList;
    private AdminAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_create_admin, container, false);
        inputAdmin_search = view.findViewById(R.id.input_admin_search);
        icon_admin_search = view.findViewById(R.id.icon_admin_search);
        icon_admin_add = view.findViewById(R.id.icon_admin_add);
        rclView_admin = view.findViewById(R.id.rclView_admin);

        rclView_admin.setHasFixedSize(true);
        rclView_admin.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        adminArrayList = new ArrayList<Admin>();
        adapter = new AdminAdapter(getContext(),adminArrayList);
        rclView_admin.setAdapter(adapter);

        fetchDataArrayList();


        fAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = fAuth.getCurrentUser();

        icon_admin_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null && currentUser.getEmail().equals("admin@gmail.com")) {
                    openCreateAdminDialog(Gravity.CENTER);
                } else {
                    Toast.makeText(getContext(), "Bạn không có quyền truy cập", Toast.LENGTH_SHORT).show();
                }
            }
        });
        icon_admin_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputAdmin_search.getText().toString().trim();
                if (!email.isEmpty()){
                    searchEmailAdmin(email);
                }else {
                    Toast.makeText(getContext(), "Vui lòng nhập email để tìm kiếm", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    private void openCreateAdminDialog(int gravity){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_create_admin);
        Window window = dialog.getWindow();
        if (window == null ){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams);
        dialog.setCancelable(true);

        TextInputEditText input_dialog_email,input_dialog_password;
        Button btnAdd,btnCancel;
        input_dialog_email = dialog.findViewById(R.id.input_dialog_email);
        input_dialog_password = dialog.findViewById(R.id.input_dialog_password);
        btnAdd = dialog.findViewById(R.id.btn_dialog_admin);
        btnCancel = dialog.findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = input_dialog_email.getText().toString().trim();
                String password = input_dialog_password.getText().toString().trim();
                String emailPattern = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
                String id = UUID.randomUUID().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getContext(), "Nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(getContext(), "Nhập passwrod", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!email.matches(emailPattern)){
                    Toast.makeText(getContext(), "Chưa đúng định dạng email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6 ){
                    Toast.makeText(getContext(), "Mật khẩu tối thiểu phải 6 kí tự", Toast.LENGTH_SHORT).show();
                    return;
                }
                fAuth.createUserWithEmailAndPassword(email,password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser user = fAuth.getCurrentUser();
                                if (user != null ) {
                                    Toast.makeText(getContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                    DocumentReference df = fstore.collection("account").document(user.getUid());
                                    Map<String, Object> adminInfo = new HashMap<>();
                                    adminInfo.put("email", email);
                                    adminInfo.put("admin", "0");
                                    adminInfo.put("id",id);
                                    df.set(adminInfo);
                                    dialog.dismiss();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });
        dialog.show();
    }
    private void fetchDataArrayList(){
        db.collection("account").orderBy("admin", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            return;
                        }
                        adminArrayList.clear();
                        for (DocumentChange document  : value.getDocumentChanges()){
                            if (document.getType() == DocumentChange.Type.ADDED){
                                adminArrayList.add(document.getDocument().toObject(Admin.class));
                            }
                            adapter.notifyDataSetChanged();

                        }
                    }
                });
    }
    private void searchEmailAdmin(String email) {
        if (email.length() < 5) {
            Toast.makeText(getContext(), "Vui lòng nhập ít nhất 5 ký tự để tìm kiếm", Toast.LENGTH_SHORT).show();
            return;
        }
        String searchKeyword = email.substring(0, 5);
        db.collection("account")
                .whereGreaterThanOrEqualTo("email",searchKeyword)
                .whereLessThan("email",searchKeyword + "z")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        adminArrayList.clear();
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                adminArrayList.add(documentChange.getDocument().toObject(Admin.class));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                });
    }
}