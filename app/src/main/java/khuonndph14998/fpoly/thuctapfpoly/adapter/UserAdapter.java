package khuonndph14998.fpoly.thuctapfpoly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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


    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
    public static class UserViewHolder extends RecyclerView.ViewHolder{

        TextView textView_email,textView_name,textView_phone;
        ImageView iconDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_email  = itemView.findViewById(R.id.textView_user_email);
            textView_name  = itemView.findViewById(R.id.textView_user_name);
            textView_phone  = itemView.findViewById(R.id.textView_user_phone);
            iconDelete  = itemView.findViewById(R.id.icon_delete_user);
        }
    }
}
