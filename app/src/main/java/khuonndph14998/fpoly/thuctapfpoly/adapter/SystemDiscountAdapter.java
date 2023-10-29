package khuonndph14998.fpoly.thuctapfpoly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.model.Discount;

public class SystemDiscountAdapter extends RecyclerView.Adapter<SystemDiscountAdapter.SystemDiscountViewHolder>{

    ArrayList<Discount> arrayList;
    Context mContext;
    private String getCurrentUserEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getEmail();
        } else {
            return null;
        }
    }

    public SystemDiscountAdapter(ArrayList<Discount> arrayList, Context mContext) {
        this.arrayList = arrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SystemDiscountAdapter.SystemDiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_system_discount_code,parent,false);
        return new SystemDiscountViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SystemDiscountAdapter.SystemDiscountViewHolder holder, int position) {
        Discount dc = arrayList.get(position);
        holder.tv_name.setText(dc.getDc_name());
        holder.tv_code.setText(dc.getDc_code());
        holder.tv_time.setText(dc.getDc_time());
        holder.tv_quantity.setText(String.valueOf(dc.getDc_quantity()));
        holder.tv_price.setText(String.valueOf(dc.getDc_price()));
        holder.btnReceiveNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDiscountCode();
            }
        });
    }

    private void saveDiscountCode() {
        String userEmail = getCurrentUserEmail();
        if (userEmail != null) {
            String emailPath = userEmail.replace("@gmail.com", "");
            String databasePath = "/Users/" + emailPath + "/MyDiscountCode";
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class SystemDiscountViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name,tv_code,tv_quantity,tv_time,tv_price;
        private Button btnReceiveNow;
        public SystemDiscountViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_systemDiscount_name);
            tv_code = itemView.findViewById(R.id.tv_systemDiscount_code);
            tv_quantity = itemView.findViewById(R.id.tv_systemDiscount_quantity);
            tv_time = itemView.findViewById(R.id.tv_systemDiscount_time);
            tv_price = itemView.findViewById(R.id.tv_systemDiscount_price);
            btnReceiveNow = itemView.findViewById(R.id.btn_systemDiscountCode);
        }
    }
}
