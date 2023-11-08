package khuonndph14998.fpoly.thuctapfpoly.fragmentadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.adapter.UserAdapter;
import khuonndph14998.fpoly.thuctapfpoly.admin.CreateAdminActivity;
import khuonndph14998.fpoly.thuctapfpoly.model.User;

public class UsersFragment extends Fragment {

    private TextInputEditText inputEditText_search;
    private ImageView icon_search,icon_update;

    private RecyclerView rclView_user;
    private ArrayList<User> userArrayList;
    private UserAdapter userAdapter;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);


        rclView_user = view.findViewById(R.id.rclView_user);
        rclView_user.setHasFixedSize(true);
        rclView_user.setLayoutManager(new LinearLayoutManager(getContext()));

        inputEditText_search = view.findViewById(R.id.input_user_search);
        icon_search = view.findViewById(R.id.icon_search);
        icon_update = view.findViewById(R.id.icon_update);


        db = FirebaseFirestore.getInstance();
        userArrayList = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(),userArrayList);
        rclView_user.setAdapter(userAdapter);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang tải dữ liệu");
        progressDialog.show();
        fetchDataAndUpdateArrayList();
        progressDialog.dismiss();

        icon_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Đang tải dữ liệu");
                progressDialog.show();
                fetchDataAndUpdateArrayList();
                inputEditText_search.setText("");
                progressDialog.dismiss();
            }
        });
        icon_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEditText_search.getText().toString().trim();
                if (!email.isEmpty()) {
                    searchUsers(email);
                } else {
                    Toast.makeText(getContext(), "Vui lòng nhập email để tìm kiếm", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    private void fetchDataAndUpdateArrayList() {
        db.collection("account")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Xử lý lỗi nếu có
                        return;
                    }

                    if (value != null && !value.isEmpty()) {
                        userArrayList.clear();
                        for (QueryDocumentSnapshot document : value) {
                            String userId = document.getId();
                            String fullName = document.getString("fullname");
                            String email = document.getString("email");
                            String role = document.getString("roles");
                            if (role != null && !role.isEmpty()) {
                                User user = new User(userId, fullName, email, role);
                                userArrayList.add(user);
                            }
                        }
                        userAdapter.notifyDataSetChanged();
                    }
                });
    }
    private void searchUsers(String email) {
        if (email.length() < 5) {
            Toast.makeText(getContext(), "Vui lòng nhập ít nhất 5 ký tự để tìm kiếm", Toast.LENGTH_SHORT).show();
            return;
        }
        String searchKeyword = email.substring(0, 5);
        progressDialog.setTitle("Đang tìm kiếm");
        progressDialog.show();

        db.collection("account")
                .whereGreaterThanOrEqualTo("email", searchKeyword)
                .whereLessThan("email", searchKeyword + "z") // Tìm kết quả nhỏ hơn 5 ký tự + 'z' để đảm bảo chính xác với 5 ký tự đầu
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            return;
                        }
                        userArrayList.clear();
                        for (DocumentChange document : value.getDocumentChanges()) {
                            if (document.getType() == DocumentChange.Type.ADDED) {
                                userArrayList.add(document.getDocument().toObject(User.class));
                            }
                        }
                        userAdapter.notifyDataSetChanged();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
    }

}
