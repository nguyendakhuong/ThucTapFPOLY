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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.adapter.DiscoundAdapter;
import khuonndph14998.fpoly.thuctapfpoly.admin.CreateDiscountCodeActivity;
import khuonndph14998.fpoly.thuctapfpoly.detail.DiscountDetailActivity;
import khuonndph14998.fpoly.thuctapfpoly.fragmentadmin.listener.ItemDiscountListener;
import khuonndph14998.fpoly.thuctapfpoly.model.Discount;

public class CodeFragment extends Fragment implements ItemDiscountListener {
    private ImageView imageViewCreateDiscount;
    private RecyclerView rcv_discount;

    private ArrayList<Discount> discountArrayList;
    DiscoundAdapter discoundAdapter;
    private FirebaseFirestore db;
    private Discount dc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_code, container, false);
        imageViewCreateDiscount = view.findViewById(R.id.icon_addDiscount);
        rcv_discount = view.findViewById(R.id.rcv_discount);

        rcv_discount.setHasFixedSize(true);
        rcv_discount.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        discountArrayList = new ArrayList<Discount>();
        discoundAdapter = new DiscoundAdapter(getContext(),discountArrayList,this);
        rcv_discount.setAdapter(discoundAdapter);

        loadDiscount();


        imageViewCreateDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CreateDiscountCodeActivity.class);
                startActivity(i);
            }
        });
        return view;
    }
    private void loadDiscount() {
        DatabaseReference discountsRef = FirebaseDatabase.getInstance().getReference("Discount");
        discountsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                discountArrayList.clear();
                for (DataSnapshot sp : snapshot.getChildren()) {
                    Discount discount = sp.getValue(Discount.class);
                    discountArrayList.add(discount);
                }
                discoundAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi khi tải dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClickDiscount(Discount discount) {
        Bundle bundle = new Bundle();
        Intent i = new Intent(getContext(), DiscountDetailActivity.class);
        bundle.putString("discountName",discount.getDc_name());
        bundle.putString("discountCode",discount.getDc_code());
        bundle.putString("discountNote",discount.getDc_note());
        bundle.putString("discountImage",discount.getDc_img());
        bundle.putString("discountTime",discount.getDc_time());
        bundle.putInt("discountQuantity",discount.getDc_quantity());
        bundle.putInt("discountPrice",discount.getDc_price());
        i.putExtras(bundle);
        startActivity(i);
    }
}