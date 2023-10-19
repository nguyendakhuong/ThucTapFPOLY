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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import khuonndph14998.fpoly.thuctapfpoly.EventBus.TotalProduct;
import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.listener.ButtonClickListenerCardPay;
import khuonndph14998.fpoly.thuctapfpoly.listener.TotalPriceListener;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;

public class CardPayAdapter extends RecyclerView.Adapter<CardPayAdapter.CardPayViewHolder>{

    private Context mContext;
    private ArrayList<Product> productArrayList;
    int number = 1;
    public clickListenerCardPay mIClickListenerDeleteItem;
    long totalPrice = 0;
    private TotalPriceListener totalPriceListener;
    public interface clickListenerCardPay {
        void onClickDeleteItem(Product p);
    }
    public void setTotalPriceListener(TotalPriceListener totalPriceListener) {
        this.totalPriceListener = totalPriceListener;
    }

    public CardPayAdapter(Context mContext, ArrayList<Product> productArrayList, clickListenerCardPay mIClickListenerDeleteItem) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
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
        holder.tv_cardNumber.setText(String.valueOf(p.getNumber()));

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIClickListenerDeleteItem.onClickDeleteItem(p);
                }
            });
        holder.setListenerCardPay(new ButtonClickListenerCardPay() {
            @Override
            public void onButtonClickListener(View view, int pos, int number) {
                if(number == 1){
                    if (productArrayList.get(pos).getNumber() > 1){
                        int soluong = productArrayList.get(pos).getNumber() -1;
                        productArrayList.get(pos).setNumber(soluong);
                    }
                }
                if (number == 2){
                    if (productArrayList.get(pos).getNumber() < p.getQuantity()){
                        int soluong = productArrayList.get(pos).getNumber() +1;
                        productArrayList.get(pos).setNumber(soluong);
                    }
                }
                holder.tv_cardNumber.setText(productArrayList.get(pos).getNumber() + " ");
                updateTotalPrice();
            }
        });
        updateTotalPrice();
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class CardPayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView cardImage,imgDelete;
        TextView tv_cardName,tv_cardPrice,tv_cardNumber;
        Button btnAdd,btnApart;
        ButtonClickListenerCardPay listenerCardPay;

        public void setListenerCardPay(ButtonClickListenerCardPay listenerCardPay) {
            this.listenerCardPay = listenerCardPay;
        }

        public CardPayViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImage = itemView.findViewById(R.id.card_image);
            tv_cardName = itemView.findViewById(R.id.card_name);
            tv_cardPrice = itemView.findViewById(R.id.card_price);
            tv_cardNumber = itemView.findViewById(R.id.text_card);
            btnAdd =itemView.findViewById(R.id.btn_card_add);
            btnApart =itemView.findViewById(R.id.btn_card_apart);
            imgDelete =itemView.findViewById(R.id.card_delete);

            btnAdd.setOnClickListener(this);
            btnApart.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if(v == btnApart){
                listenerCardPay.onButtonClickListener(itemView,getAdapterPosition(),1);
            }
            if (v == btnAdd){
                listenerCardPay.onButtonClickListener(itemView,getAdapterPosition(),2);
            }
        }
    }
    private long calculateTotalPrice() {
        long totalPrice = 0;
        for (Product product : productArrayList) {
            totalPrice += product.getNumber() * product.getPrice();
        }
        return totalPrice;
    }
    private void updateTotalPrice() {
        totalPrice = calculateTotalPrice();
        String gia = String.valueOf(totalPrice);
        if (totalPriceListener != null) {
            totalPriceListener.onTotalPriceChanged(totalPrice);
        }
        Log.d("gia", gia);
    }
    public long getTotalPrice() {
        return totalPrice;
    }
}
