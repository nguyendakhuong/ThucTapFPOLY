package khuonndph14998.fpoly.thuctapfpoly.fragmentadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.adapter.BillAdapter;
import khuonndph14998.fpoly.thuctapfpoly.adapter.UserAdapter;
import khuonndph14998.fpoly.thuctapfpoly.detail.BillDetailActivity;
import khuonndph14998.fpoly.thuctapfpoly.model.Bill;


public class BillFragment extends Fragment implements BillAdapter.OnItemBillClickListener {
    RecyclerView rcv_billAll;
    BillAdapter adapter;
    ArrayList<Bill> arrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);
        arrayList = new ArrayList<>();
        adapter = new BillAdapter(arrayList,getContext(),this);
        rcv_billAll = view.findViewById(R.id.rcv_billAll);

        rcv_billAll.setHasFixedSize(true);
        rcv_billAll.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv_billAll.setAdapter(adapter);
        loadDataBill();
        return view;
    }
    private void loadDataBill() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bill");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Bill bill = snapshot.getValue(Bill.class);
                    arrayList.add(bill);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi hệ thống", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onItemClick(Bill bill) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("bill", bill);
        Intent i = new Intent(getContext(), BillDetailActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }
}