package khuonndph14998.fpoly.thuctapfpoly.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.adapter.SystemDiscountAdapter;
import khuonndph14998.fpoly.thuctapfpoly.model.Discount;

public class SystemDiscountCodeActivity extends AppCompatActivity {

    RecyclerView rcv_systemDiscordCode;
    SystemDiscountAdapter adapter;
    ArrayList<Discount> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_discount_code);
        rcv_systemDiscordCode = findViewById(R.id.rvc_systemDiscountCode);
        rcv_systemDiscordCode.setHasFixedSize(true);
        adapter = new SystemDiscountAdapter(arrayList,this);
        rcv_systemDiscordCode.setAdapter(adapter);
        rcv_systemDiscordCode.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        loadDiscountCode();
    }
    private void loadDiscountCode(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Discount");
        databaseReference.orderByChild("dc_quantity").startAfter(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot sp : snapshot.getChildren()){
                            Discount dc = sp.getValue(Discount.class);
                            if (dc != null && dc.getDc_quantity() > 0) {
                                arrayList.add(dc);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SystemDiscountCodeActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}