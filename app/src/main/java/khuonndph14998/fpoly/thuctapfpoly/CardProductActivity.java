    package khuonndph14998.fpoly.thuctapfpoly;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.fragment.app.FragmentTransaction;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.google.android.gms.tasks.OnFailureListener;
    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.database.ChildEventListener;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.database.Query;
    import com.google.gson.Gson;

    import java.util.ArrayList;
    import java.util.List;
    import khuonndph14998.fpoly.thuctapfpoly.adapter.CardPayAdapter;
    import khuonndph14998.fpoly.thuctapfpoly.fragment.SupportFragment;
    import khuonndph14998.fpoly.thuctapfpoly.listener.TotalPriceListener;
    import khuonndph14998.fpoly.thuctapfpoly.model.PayProduct;
    import khuonndph14998.fpoly.thuctapfpoly.model.Product;

    public class CardProductActivity extends AppCompatActivity implements TotalPriceListener {
        RecyclerView recyclerView;
        TextView tv_total;
        Button btnPay;
        private ArrayList<Product> productArrayList;
        CardPayAdapter cardPayAdapter;
        private FirebaseFirestore db;
        private Product product;
        private long totalPrice = 0;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_card_product);
            anhxa();

            recyclerView.setHasFixedSize(true);
            db = FirebaseFirestore.getInstance();
            productArrayList = new ArrayList<Product>();
            cardPayAdapter = new CardPayAdapter(getApplicationContext(),productArrayList,new CardPayAdapter.clickListenerCardPay() {
                @Override
                public void onClickDeleteItem(Product p) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CardProductActivity.this);
                    builder.setTitle("Xóa sản phẩm");
                    builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm "+p.getName());
                    builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteProduct(p);
                        }
                    });
                    builder.setNegativeButton("Hủy", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            cardPayAdapter.setTotalPriceListener(this);
            recyclerView.setAdapter(cardPayAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            getCode();
            btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Product> selectedProducts = cardPayAdapter.getSelectedProducts();
                    if (selectedProducts.isEmpty()) {
                        Toast.makeText(CardProductActivity.this, "Vui lòng chọn sản phẩm để thanh toán", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Gson gson = new Gson();
                    String productListJson = gson.toJson(selectedProducts);

                    SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("productList", productListJson);
                    editor.apply();
                    Intent intent = new Intent(CardProductActivity.this, PayActivity.class);
                    startActivity(intent);
                }
            });
        }

        private void deleteProduct(Product p){
            String userEmail = getCurrentUserEmail();
            String emailPath = userEmail.replace("@gmail.com", "");
            String databasePath ="/Users/"+ emailPath + "/productCodes";
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);
            databaseReference.child(String.valueOf(p.getCode()))
                    .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            productArrayList.remove(p);
                            cardPayAdapter.notifyDataSetChanged();
                            Toast.makeText(CardProductActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CardProductActivity.this, "Lỗi hệ thống", Toast.LENGTH_SHORT).show();
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
                String databasePath = "/Users/" + emailPath + "/productCodes";
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý lỗi
                    }
                });
            }
        }

        private void loadProducts(String productCode) {
            DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");
            productsRef.orderByChild("code").equalTo(productCode).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi
                }
            });
        }
        private void anhxa() {
            recyclerView = findViewById(R.id.rcv_card);
            tv_total = findViewById(R.id.tv_total);
            btnPay = findViewById(R.id.btnPay);
        }

        @Override
        public void onTotalPriceChanged(long totalPrice) {
            String totalPriceString = String.valueOf(totalPrice);
            tv_total.setText(totalPriceString);
        }
    }