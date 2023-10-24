package khuonndph14998.fpoly.thuctapfpoly.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.model.PayProduct;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayViewHoder> {
    private Context mContext;
    private ArrayList<PayProduct> productArrayList;

    public PayAdapter(Context mContext, ArrayList<PayProduct> productArrayList) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
    }

    @NonNull
    @Override
    public PayAdapter.PayViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_pay,parent,false);
        return new PayAdapter.PayViewHoder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PayAdapter.PayViewHoder holder, int position) {
        PayProduct p = productArrayList.get(position);
        String urlString = p.getImage();
        Uri uri = Uri.parse(urlString);
        holder.imgPay.setImageURI(uri);

        holder.tvPay_name.setText(p.getName());
        holder.tvPay_quantity.setText(String.valueOf(p.getNumber()));
        holder.tvPay_category.setText(p.getCategory());
        holder.tvPay_price.setText(String.valueOf(p.getPrice()));


    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class PayViewHoder extends RecyclerView.ViewHolder {
        ImageView imgPay;
        TextView tvPay_name,tvPay_quantity,tvPay_category,tvPay_price;
        public PayViewHoder(@NonNull View itemView) {
            super(itemView);
        imgPay = itemView.findViewById(R.id.item_pay_image);
        tvPay_name = itemView.findViewById(R.id.item_pay_name);
        tvPay_quantity = itemView.findViewById(R.id.item_pay_quantity);
        tvPay_category = itemView.findViewById(R.id.item_pay_category);
        tvPay_price = itemView.findViewById(R.id.item_pay_price);
        }
    }
}
