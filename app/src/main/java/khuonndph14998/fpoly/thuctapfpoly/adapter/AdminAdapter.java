package khuonndph14998.fpoly.thuctapfpoly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.model.Admin;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder>{
    private Context mContext;
    private ArrayList<Admin> arrayList;

    public AdminAdapter(Context mContext, ArrayList<Admin> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AdminAdapter.AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_admin,parent,false);
        return new AdminAdapter.AdminViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAdapter.AdminViewHolder holder, int position) {
        Admin admin = arrayList.get(position);
        holder.tv_Admin.setText(admin.getEmail());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_Admin;
        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Admin = itemView.findViewById(R.id.item_tvAdmin);
        }
    }
}
