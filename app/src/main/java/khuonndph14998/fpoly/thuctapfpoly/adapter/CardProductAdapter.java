package khuonndph14998.fpoly.thuctapfpoly.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.listener.ItemProductListener;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;

public class CardProductAdapter extends RecyclerView.Adapter<CardProductAdapter.CardProductViewHolder> {
    private Context mContext;
    private ArrayList<Product> productArrayList;
    private ItemProductListener cardProductListener;
    private ArrayList<Product> arrayList = new ArrayList<>();
    private List<String> productCodeList = new ArrayList<>();
    FirebaseUser user;

    public CardProductAdapter(Context mContext, ArrayList<Product> productArrayList, ItemProductListener cardProductListener) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
        this.cardProductListener = cardProductListener;
    }

    @NonNull
    @Override
    public CardProductAdapter.CardProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_product, parent, false);
        return new CardProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardProductAdapter.CardProductViewHolder holder, int position) {
        Product p = productArrayList.get(position);
        String userEmail = getCurrentUserEmail();
        String emailPath = userEmail.replace("@gmail.com", "");
        String databasePath ="/Users/"+ emailPath + "/FavProduct";
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> codeList = new ArrayList<>();
                for (DataSnapshot sp : snapshot.getChildren()) {
                    String code = sp.getKey(); // Lấy key của node con làm mã code
                    codeList.add(code);
                }

                // Kiểm tra xem sản phẩm có trong danh sách yêu thích hay không
                if (isProductInFavorites(p.getCode(), codeList)) {
                    holder.btnFavorite.setBackgroundResource(R.drawable.ic_baseline_product_favorite);
                    holder.btnFavorite.setEnabled(false);
                } else {
                    holder.btnFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_border);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        String urlString = p.getImage();
        Uri uri = Uri.parse(urlString);
        holder.textView_name.setText(p.getName());
        holder.imageView_product.setImageURI(uri);
        holder.text_Price.setText(String.valueOf(p.getPrice()));
        holder.textView_describe.setText(p.getDescribe());
        holder.itemView.setOnClickListener(v -> cardProductListener.onItemClickProduct(productArrayList.get(position)));
        holder.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productCode = p.getCode();
                saveFavProductToDatabase(productCode);
                holder.btnFavorite.setBackgroundResource(R.drawable.ic_baseline_product_favorite);
                holder.btnFavorite.setEnabled(false);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class CardProductViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_name, textView_describe, text_Price;
        private ImageView imageView_product;
        private Button btnCardProduct, btnFavorite;

        public CardProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_name = itemView.findViewById(R.id.cardProduct_name);
            text_Price = itemView.findViewById(R.id.cardProduct_price);
            textView_describe = itemView.findViewById(R.id.cardProduct_description);
            imageView_product = itemView.findViewById(R.id.imageView_cardProduct);
            btnCardProduct = itemView.findViewById(R.id.btnCardProduct);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);

            btnCardProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product product = productArrayList.get(getAdapterPosition());
                    String productCode = product.getCode();
                    saveProductCodeToDatabase(productCode);

                }
            });
        }
    }
    private String getCurrentUserEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {

            return currentUser.getEmail();
        } else {
            return null;
        }
    }

    private void saveProductCodeToDatabase(String productCode) {
        String userEmail = getCurrentUserEmail();
        if (userEmail != null) {
            String emailPath = userEmail.replace("@gmail.com", "");
            String databasePath ="/Users/"+ emailPath + "/productCodes";

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);
            databaseReference.child(productCode).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(mContext, "Sản phẩm Đã có trong giỏ hàng", Toast.LENGTH_SHORT).show();
                    } else {
                        databaseReference.child(productCode).setValue(productCode)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(mContext, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(mContext, "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(mContext, "Lỗi máy chủ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void saveFavProductToDatabase(String productCode) {
        String userEmail = getCurrentUserEmail();
        if (userEmail != null) {
            String emailPath = userEmail.replace("@gmail.com", "");
            String databasePath ="/Users/"+ emailPath + "/FavProduct";
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);
            Map<String, Object> favData = new HashMap<>();
            favData.put("productCode", productCode);
            databaseReference.child(productCode).setValue(favData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(mContext, "Đã chuyển vào danh sách yêu thích!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mContext, "Lỗi hệ thống", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private boolean isProductInFavorites(String productCode, List<String> codeList) {
        return codeList.contains(productCode);
    }
}
