package khuonndph14998.fpoly.thuctapfpoly.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.listener.ItemProductListener;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;
import khuonndph14998.fpoly.thuctapfpoly.model.User;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context mContext;
    private ArrayList<Product> productArrayList;
    private ItemProductListener productListener;

    public ProductAdapter(Context mContext, ArrayList<Product> productArrayList, ItemProductListener productListener) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
        this.productListener = productListener;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_product,parent,false);
        return new ProductViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        Product p = productArrayList.get(position);
        String urlString = p.getImage();
        Uri uri = Uri.parse(urlString);
        holder.textView_name.setText(p.getName());
        holder.textView_quantity.setText(String.valueOf(p.getQuantity()));
        holder.textView_selectedItem.setText(p.getSelectedItem());
        holder.textView_code.setText(p.getCode());
        holder.imageView_product.setImageURI(uri);
        holder.itemView.setOnClickListener(v -> productListener.onItemClickProduct(productArrayList.get(position)));
        holder.iconDelete.setOnClickListener( v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Thông báo")
                    .setMessage("Bản có chắc muốn xóa sản phẩm này không")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase fb = FirebaseDatabase.getInstance();
                            DatabaseReference db = fb.getReference("products");
                            db.child(String.valueOf(p.getCode())).removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(mContext, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).setNegativeButton("Cancel",null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_name, textView_code, textView_describe, textView_note, textView_quantity, textView_selectedItem;
        private ImageView iconDelete, imageView_product;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_name = itemView.findViewById(R.id.item_textView_name);
            textView_selectedItem = itemView.findViewById(R.id.item_textView_category);
            textView_quantity = itemView.findViewById(R.id.item_textView_quantity);
            iconDelete = itemView.findViewById(R.id.item_product_delete);
            imageView_product = itemView.findViewById(R.id.item_product_image);
            textView_code = itemView.findViewById(R.id.item_textView_code);

        }
    }
}
