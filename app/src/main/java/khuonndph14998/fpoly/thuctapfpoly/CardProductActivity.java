package khuonndph14998.fpoly.thuctapfpoly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import khuonndph14998.fpoly.thuctapfpoly.adapter.CardPayAdapter;
import khuonndph14998.fpoly.thuctapfpoly.adapter.CardProductAdapter;
import khuonndph14998.fpoly.thuctapfpoly.adapter.ProductAdapter;
import khuonndph14998.fpoly.thuctapfpoly.listener.QuantityChangeListener;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;

public class CardProductActivity extends AppCompatActivity implements QuantityChangeListener {
    RecyclerView recyclerView;
    TextView tv_total;
    Button btnPay;
    private int quantity = 1;
    private int currentPosition =-1;
    private ArrayList<Product> productArrayList;
    CardPayAdapter cardPayAdapter;
    private FirebaseFirestore db;
    private Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_product);
        anhxa();

        recyclerView.setHasFixedSize(true);
        db = FirebaseFirestore.getInstance();
        productArrayList = new ArrayList<Product>();
        cardPayAdapter = new CardPayAdapter(getApplicationContext(),productArrayList,this);
        recyclerView.setAdapter(cardPayAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        getCode();
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
    private String getCurrentUserEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getEmail();
        } else {
            return null;
        }
    }
    private void getCode() {
        String userEmail = getCurrentUserEmail();
        if (userEmail != null) {
            String emailPath = userEmail.replace("@gmail.com", "");
            String databasePath ="/Users/"+ emailPath + "/productCodes";
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> productCodeList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String productCode = snapshot.getKey();
                        productCodeList.add(productCode);
                    }
                    for (String code : productCodeList) {
                        loadProducts(code);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }
    }
    private void loadProducts(String productCode) {
        if (productCode != null) {
            DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");
            productsRef.orderByChild("code").equalTo(productCode).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Product> productList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product product = snapshot.getValue(Product.class);
                        if (product != null) {
                            productList.add(product);
                        }
                    }
                    productArrayList.addAll(productList);
                    cardPayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }
    private void anhxa() {
        recyclerView = findViewById(R.id.rcv_card);
        tv_total = findViewById(R.id.tv_total);
        btnPay = findViewById(R.id.btnPay);
    }


    @Override
    public void onIncrease(int position) {
        quantity++;
        currentPosition = position;
        updateQuantityTextView();
    }

    @Override
    public void onDecrease(int position) {
        if (quantity > 1) {
            quantity--;
            currentPosition = position;
            updateQuantityTextView();
        }
    }

    private void updateQuantityTextView() {
        if (currentPosition != -1) {
            View view = recyclerView.getLayoutManager().findViewByPosition(currentPosition);
            if (view != null) {
                TextView tvQuantity = view.findViewById(R.id.text_card);
                tvQuantity.setText(String.valueOf(quantity));
            }
        }
    }
}