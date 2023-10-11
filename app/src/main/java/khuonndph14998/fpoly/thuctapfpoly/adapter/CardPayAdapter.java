package khuonndph14998.fpoly.thuctapfpoly.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.listener.QuantityChangeListener;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;

public class CardPayAdapter extends RecyclerView.Adapter<CardPayAdapter.CardPayViewHolder>{

    private Context mContext;
    private ArrayList<Product> productArrayList;
    private QuantityChangeListener quantityChangeListener;
    int number = 1;

    private String getCurrentUserEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getEmail();
        } else {
            return null;
        }
    }


    public CardPayAdapter(Context mContext, ArrayList<Product> productArrayList, QuantityChangeListener quantityChangeListener) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
        this.quantityChangeListener = quantityChangeListener;
    }

    @NonNull
    @Override
    public CardPayAdapter.CardPayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_pay_product,parent,false);
        return new CardPayViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardPayAdapter.CardPayViewHolder holder, int position) {
        Product p = productArrayList.get(position);
        double total = 0;
        for (Product product : productArrayList) {
            total += product.getPrice();
        }


        String userEmail = getCurrentUserEmail();
        String urlString = p.getImage();
        Uri uri = Uri.parse(urlString);
        holder.tv_cardName.setText(p.getName());
        holder.cardImage.setImageURI(uri);
        holder.tv_cardPrice.setText(String.valueOf(p.getPrice()));

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityChangeListener != null) {
                    quantityChangeListener.onIncrease(position);
                }
            }
        });

        holder.btnApart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityChangeListener != null) {
                    quantityChangeListener.onDecrease(position);
                }
            }
        });
        holder.tv_cardNumber.setText(String.valueOf(number));

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String emailPath = userEmail.replace("@gmail.com", "");
                    String databasePath = emailPath + "/productCodes";
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(databasePath)
                            .child(p.getCode());
                    databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            int deletedPosition = holder.getAdapterPosition();
                            productArrayList.remove(deletedPosition);
                            notifyItemRemoved(deletedPosition);
                            Toast.makeText(mContext, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mContext, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class CardPayViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage,imgDelete;
        TextView tv_cardName,tv_cardPrice,tv_cardNumber;
        Button btnAdd,btnApart;
        public CardPayViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImage = itemView.findViewById(R.id.card_image);
            tv_cardName = itemView.findViewById(R.id.card_name);
            tv_cardPrice = itemView.findViewById(R.id.card_price);
            tv_cardNumber = itemView.findViewById(R.id.text_card);
            btnAdd =itemView.findViewById(R.id.btn_card_add);
            btnApart =itemView.findViewById(R.id.btn_card_apart);
            imgDelete =itemView.findViewById(R.id.card_delete);
        }
    }
}
