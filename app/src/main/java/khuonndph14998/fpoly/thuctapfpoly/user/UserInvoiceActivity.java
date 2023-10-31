package khuonndph14998.fpoly.thuctapfpoly.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.adapter.BillAdapter;
import khuonndph14998.fpoly.thuctapfpoly.model.Bill;

public class UserInvoiceActivity extends AppCompatActivity implements BillAdapter.OnItemBillClickListener {
    private RecyclerView rcv_userInvoice;
    private ArrayList<Bill> arrayList;
    private BillAdapter adapter;

    private String getCurrentUserEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getEmail();
        } else {
            return null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_invoice);
        rcv_userInvoice = findViewById(R.id.rcv_userInvoice);
        arrayList = new ArrayList<>();
        adapter = new BillAdapter(arrayList,getApplicationContext(),this);

        rcv_userInvoice.setHasFixedSize(true);
        rcv_userInvoice.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rcv_userInvoice.setAdapter(adapter);
        loadDataBill();
    }
    private void loadDataBill(){
        String userEmail = getCurrentUserEmail();
        if (userEmail != null) {
            String emailPath = userEmail.replace("@gmail.com", "");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bill");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    arrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Bill bill = snapshot.getValue(Bill.class);
                        if (bill != null && bill.getPayEr().equals(emailPath)) {
                            arrayList.add(bill);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Lỗi hệ thống", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onItemClick(Bill bill) {

    }
}