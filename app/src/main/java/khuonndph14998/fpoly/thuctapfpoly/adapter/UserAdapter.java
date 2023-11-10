package khuonndph14998.fpoly.thuctapfpoly.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    Context mContext;
    ArrayList<User> userArrayList;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    public UserAdapter(Context mContext, ArrayList<User> userArrayList) {
        this.mContext = mContext;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.item_user,parent,false);

        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User u = userArrayList.get(position);
        holder.textView_email.setText(u.getEmail());
        holder.textView_name.setText(u.getFullname());
        if (u.getRoles().equals("1")){
            holder.textView_phone.setText("người dùng");
            holder.iconBan.setVisibility(View.VISIBLE);
            holder.iconUnlock.setVisibility(View.GONE);
        }

        if (u.getRoles().equals("2")){
            holder.textView_phone.setText("khóa tài khoản");
            holder.iconBan.setVisibility(View.GONE);
            holder.iconUnlock.setVisibility(View.VISIBLE);
        }
        holder.iconBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog(u.getId());
            }

        });
        holder.iconUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unlockAccount(u.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        TextView textView_email,textView_name,textView_phone;
        ImageView iconBan,iconUnlock;
        View mView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            textView_email  = itemView.findViewById(R.id.textView_user_email);
            textView_name  = itemView.findViewById(R.id.textView_user_name);
            textView_phone  = itemView.findViewById(R.id.textView_user_status);
            iconBan  = itemView.findViewById(R.id.icon_ban_user);
            iconUnlock = itemView.findViewById(R.id.icon_unlock);

        }
    }
    private void showConfirmationDialog(String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn có chắc chắn muốn khóa tài khoản này?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lockAccount(id);
            }
        }).setNegativeButton("Không",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void lockAccount(String id) {
        fStore.collection("account")
                .document(id)
                .update("roles", "2")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mContext, "Bạn đã khóa tài khoản người dùng", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi
                    }
                });
    }
    private void unlockAccount(String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn có muốn mở khóa tài khoản này?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fStore.collection("account")
                        .document(id)
                        .update("roles", "1")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(mContext, "Bạn đã mở khóa tài khoản người dùng", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Xử lý lỗi
                            }
                        });
            }
        }).setNegativeButton("Không",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
