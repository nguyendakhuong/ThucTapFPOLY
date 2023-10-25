package khuonndph14998.fpoly.thuctapfpoly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;

public class DetailPayAdapter extends RecyclerView.Adapter<DetailPayAdapter.DetailPayViewHoder>{

    private Context mContext;
    private List<Product> productArrayList;

    public DetailPayAdapter(Context mContext, List<Product> productArrayList) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
    }

    @NonNull
    @Override
    public DetailPayAdapter.DetailPayViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_name_price,parent,false);
        return new DetailPayAdapter.DetailPayViewHoder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailPayAdapter.DetailPayViewHoder holder, int position) {
        Product p = productArrayList.get(position);
        holder.tvDetail_name.setText(p.getName());
        holder.tvDetail_price.setText(String.valueOf(p.getPrice()));
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class DetailPayViewHoder extends RecyclerView.ViewHolder {
        TextView tvDetail_name,tvDetail_price;
        public DetailPayViewHoder(@NonNull View itemView) {
            super(itemView);
            tvDetail_name = itemView.findViewById(R.id.item_name);
            tvDetail_price = itemView.findViewById(R.id.item_price);
        }
    }
}
