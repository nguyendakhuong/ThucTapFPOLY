package khuonndph14998.fpoly.thuctapfpoly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.model.Bill;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder>{

    private ArrayList<Bill> billList;
    private Context mContext;
    private OnItemBillClickListener listener;

    public interface OnItemBillClickListener {
        void onItemClick(Bill bill);
    }

    public BillAdapter(ArrayList<Bill> billList, Context mContext, OnItemBillClickListener listener) {
        this.billList = billList;
        this.mContext = mContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BillAdapter.BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BillAdapter.BillViewHolder holder, int position) {
        Bill bill = billList.get(position);
        holder.textBillId.setText(bill.getBillId());
        holder.textEmail.setText(bill.getPayEr());
        holder.textAddress.setText(bill.getCustomerAddress());
        holder.textTotal.setText(String.valueOf(bill.getTotalPay()));
        holder.textDate.setText(bill.getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(billList.get(position));
            }

        });
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    public class BillViewHolder extends RecyclerView.ViewHolder {
        TextView textBillId,textEmail,textAddress,textTotal,textDate;
        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            textBillId = itemView.findViewById(R.id.text_bill_id);
            textEmail = itemView.findViewById(R.id.text_email);
            textAddress = itemView.findViewById(R.id.text_address);
            textTotal = itemView.findViewById(R.id.text_total);
            textDate = itemView.findViewById(R.id.text_time);

        }

    }
}
