package khuonndph14998.fpoly.thuctapfpoly.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.listener.ItemProductListener;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;

public class CardProductAdapter extends RecyclerView.Adapter<CardProductAdapter.CardProductViewHolder>{
    private Context mContext;
    private ArrayList<Product> productArrayList;
    private ItemProductListener cardProductListener;

    public CardProductAdapter(Context mContext, ArrayList<Product> productArrayList, ItemProductListener cardProductListener) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
        this.cardProductListener = cardProductListener;
    }

    @NonNull
    @Override
    public CardProductAdapter.CardProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_product,parent,false);
        return new CardProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardProductAdapter.CardProductViewHolder holder, int position) {
        Product p = productArrayList.get(position);
        String urlString = p.getImage();
        Uri uri = Uri.parse(urlString);
        holder.textView_name.setText(p.getName());
        holder.imageView_product.setImageURI(uri);
        holder.text_Price.setText(String.valueOf(p.getPrice()));
        holder.textView_describe.setText(p.getDescribe());
        holder.itemView.setOnClickListener(v -> cardProductListener.onItemClickProduct(productArrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class CardProductViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_name, textView_describe,text_Price;
        private ImageView imageView_product;
        private Button btnCardProduct,btnFavorite;
        public CardProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_name = itemView.findViewById(R.id.cardProduct_name);
            text_Price = itemView.findViewById(R.id.cardProduct_price);
            textView_describe = itemView.findViewById(R.id.cardProduct_description);
            imageView_product = itemView.findViewById(R.id.imageView_cardProduct);
            btnCardProduct = itemView.findViewById(R.id.btnCardProduct);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }
    }
}
