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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.listener.CardPayListener;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;

public class CardPayAdapter extends RecyclerView.Adapter<CardPayAdapter.CardPayViewHolder>{

    private Context mContext;
    private ArrayList<Product> productArrayList;
    private CardPayListener CardPayListener;
    int number = 1;
    public clickListenerCardPay mIClickListenerDeleteItem;

    public interface clickListenerCardPay {
        void onClickDeleteItem(Product p);
    }

    public CardPayAdapter(Context mContext, ArrayList<Product> productArrayList, CardPayListener cardPayListener, clickListenerCardPay mIClickListenerDeleteItem) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
        this.CardPayListener = cardPayListener;
        this.mIClickListenerDeleteItem = mIClickListenerDeleteItem;
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
        String urlString = p.getImage();
        Uri uri = Uri.parse(urlString);
        holder.tv_cardName.setText(p.getName());
        holder.cardImage.setImageURI(uri);
        holder.tv_cardPrice.setText(String.valueOf(p.getPrice()));

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CardPayListener != null) {
                    CardPayListener.onIncrease(position);
                }
            }
        });

        holder.btnApart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CardPayListener != null) {
                    CardPayListener.onDecrease(position);
                }
            }
        });
        holder.tv_cardNumber.setText(String.valueOf(number));

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    CardPayListener.onCLickDeleteItemCardPay(p);
                    mIClickListenerDeleteItem.onClickDeleteItem(p);
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
