package khuonndph14998.fpoly.thuctapfpoly.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.account.ChangePasswordActivity;
import khuonndph14998.fpoly.thuctapfpoly.auth.LoginActivity;
import khuonndph14998.fpoly.thuctapfpoly.user.InfoUserActivity;
import khuonndph14998.fpoly.thuctapfpoly.user.UserInvoiceActivity;

public class SettingFragment extends Fragment {
    private Button logoutButton,btnChangePassword,btnUserInfo,btnBill,btnDeleteAccount;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        logoutButton = view.findViewById(R.id.fragment_button_logout);
        btnChangePassword = view.findViewById(R.id.btn_setting_account);
        btnUserInfo = view.findViewById(R.id.btnUserInfo);
        btnBill = view.findViewById(R.id.btn_setting_Bill);
        btnDeleteAccount = view.findViewById(R.id.btn_setting_deleteAccount);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null ){
            Intent i = new Intent(getContext(), LoginActivity.class);
            startActivity(i);
        }
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);

            }
        });
        btnBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), UserInvoiceActivity.class);
                startActivity(i);
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(i);
            }
        });

        btnUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), InfoUserActivity.class);
                startActivity(i);
            }
        });

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Xác nhận xóa tài khoản");
                builder.setMessage("Bạn có chắc chắn muốn xóa tài khoản?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount();
                    }
                }).setNegativeButton("Hủy", null);
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        return view;
    }
    private void deleteAccount(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Bạn đã xóa tài khoản", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Lỗi hệ thống", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}