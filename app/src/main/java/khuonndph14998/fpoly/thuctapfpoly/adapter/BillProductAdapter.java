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

public class BillProductAdapter extends RecyclerView.Adapter<BillProductAdapter.BillProductViewHolder>{
    private Context context;
    private List<Product> productList;

    public BillProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public BillProductAdapter.BillProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill_product , parent,false);
        return new BillProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillProductAdapter.BillProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvBillProduct_name.setText(product.getName());
        holder.tvBillProduct_code.setText(product.getCode());
        holder.tvBillProduct_quantity.setText(String.valueOf(product.getQuantity()));

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class BillProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvBillProduct_name,tvBillProduct_quantity,tvBillProduct_code;
        public BillProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBillProduct_name = itemView.findViewById(R.id.item_billProduct_name);
            tvBillProduct_quantity = itemView.findViewById(R.id.item_billProduct_quantity);
            tvBillProduct_code = itemView.findViewById(R.id.item_billProduct_code);
        }
    }
}
