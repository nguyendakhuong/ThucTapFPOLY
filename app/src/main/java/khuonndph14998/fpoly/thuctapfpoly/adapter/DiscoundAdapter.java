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
import khuonndph14998.fpoly.thuctapfpoly.fragmentadmin.listener.ItemDiscountListener;
import khuonndph14998.fpoly.thuctapfpoly.model.Discount;

public class DiscoundAdapter extends RecyclerView.Adapter<DiscoundAdapter.DiscoundViewHolder>{
    private Context mContext;
    private ArrayList<Discount> productArrayList;
    private ItemDiscountListener discountListener;

    public DiscoundAdapter(Context mContext, ArrayList<Discount> productArrayList, ItemDiscountListener discountListener) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
        this.discountListener = discountListener;
    }

    @NonNull
    @Override
    public DiscoundAdapter.DiscoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_discount,parent,false);
        return new DiscoundAdapter.DiscoundViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoundAdapter.DiscoundViewHolder holder, int position) {
        Discount dc = productArrayList.get(position);
        String urlString = dc.getDc_img();
        Uri uri = Uri.parse(urlString);
        holder.dc_image.setImageURI(uri);

        holder.tv_discountName.setText(dc.getDc_name());
        holder.tv_discountQuantity.setText(String.valueOf(dc.getDc_quantity()));
        holder.tv_discountCode.setText(dc.getDc_code());
        holder.tv_discountTime.setText(dc.getDc_time());

        holder.itemView.setOnClickListener(v -> discountListener.onItemClickDiscount(productArrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class DiscoundViewHolder extends RecyclerView.ViewHolder {
        private ImageView dc_image;
        private TextView tv_discountName,tv_discountQuantity,tv_discountCode,tv_discountTime;
        public DiscoundViewHolder(@NonNull View itemView) {
            super(itemView);
            dc_image = itemView.findViewById(R.id.item_discountImage);
            tv_discountName = itemView.findViewById(R.id.item_discountName);
            tv_discountQuantity = itemView.findViewById(R.id.item_discountQuantity);
            tv_discountCode = itemView.findViewById(R.id.item_discountCode);
            tv_discountTime = itemView.findViewById(R.id.item_discountTime);
        }
    }
}
