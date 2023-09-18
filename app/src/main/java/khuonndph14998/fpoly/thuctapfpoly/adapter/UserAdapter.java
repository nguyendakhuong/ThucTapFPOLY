package khuonndph14998.fpoly.thuctapfpoly.adapter;

import android.content.Context;
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
        holder.textView_phone.setText(u.getPhone());

//        holder.iconDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth mAuth = FirebaseAuth.getInstance();
//                FirebaseUser user = mAuth.getCurrentUser();
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                String id = u.getId();
//
//                db.collection("User")
//                        .whereEqualTo("id",id)
//                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful() && !task.getResult().isEmpty()){
//                                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
//                                    String documentID = documentSnapshot.getId();
//                                    db.collection("User")
//                                            .document(documentID)
//                                            .delete()
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    Toast.makeText(mContext, "Xóa thành công", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }).addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    Toast.makeText(mContext, "Một số lỗi xảy ra", Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                }else {
//                                    Toast.makeText(mContext, "Xóa thất bại", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//            }
//        });
        holder.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String idUser = u.getId();

            }

        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        TextView textView_email,textView_name,textView_phone;
        ImageView iconDelete;
        View mView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            textView_email  = itemView.findViewById(R.id.textView_user_email);
            textView_name  = itemView.findViewById(R.id.textView_user_name);
            textView_phone  = itemView.findViewById(R.id.textView_user_phone);
            iconDelete  = itemView.findViewById(R.id.icon_delete_user);

        }

    }
}
