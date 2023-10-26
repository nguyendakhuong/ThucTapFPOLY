package khuonndph14998.fpoly.thuctapfpoly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import khuonndph14998.fpoly.thuctapfpoly.adapter.CardPayAdapter;
import khuonndph14998.fpoly.thuctapfpoly.adapter.CardProductAdapter;
import khuonndph14998.fpoly.thuctapfpoly.adapter.DetailPayAdapter;
import khuonndph14998.fpoly.thuctapfpoly.adapter.PayAdapter;
import khuonndph14998.fpoly.thuctapfpoly.adapter.ProductAdapter;
import khuonndph14998.fpoly.thuctapfpoly.listener.ItemProductListener;
import khuonndph14998.fpoly.thuctapfpoly.listener.TotalPriceListener;
import khuonndph14998.fpoly.thuctapfpoly.model.PayProduct;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;
import khuonndph14998.fpoly.thuctapfpoly.user.InfoUserActivity;

public class PayActivity extends AppCompatActivity{

    EditText ed_code;
    Button btn_code,btn_Pay;
    TextView tvCode_sale,tv_totalPay;
    CheckBox checkBox;

    RecyclerView rcv_itemPay,rcv_pay;
    PayAdapter adapter;
    DetailPayAdapter detailPayAdapter;

    int totalPay = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        anhxa();
        rcv_itemPay.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rcv_pay.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        String productListJson = sharedPreferences.getString("productList", "");
        List<Product> productList = convertJsonToProductList(productListJson);

        adapter = new PayAdapter(getApplicationContext(),productList);
        rcv_itemPay.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        detailPayAdapter = new DetailPayAdapter(getApplicationContext(),productList);
        rcv_pay.setAdapter(detailPayAdapter);
        detailPayAdapter.notifyDataSetChanged();

        btn_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    checkAddress();
                } else {
                    Toast.makeText(getApplicationContext(), "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                }
            }
        });
        totalPrice();

    }
    private List<Product> convertJsonToProductList(String json) {
        List<Product> productList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String code = jsonObject.getString("code");
                String describe = jsonObject.getString("describe");
                String image = jsonObject.getString("image");
                String name = jsonObject.getString("name");
                String note = jsonObject.getString("note");
                int number = jsonObject.getInt("number");
                int price = (int) jsonObject.getDouble("price");
                int quantity = jsonObject.getInt("quantity");
                String selectedItem = jsonObject.getString("selectedItem");
                Product product = new Product(name,code,quantity,note,describe,selectedItem,image,price,number);
                productList.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return productList;
    }
    private String getCurrentUserEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getEmail();
        } else {
            return null;
        }
    }

    public void checkAddress () {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userEmail = getCurrentUserEmail();
        String emailPath = userEmail.replace("@gmail.com", "");
        String databasePath ="/Users/"+ emailPath + "/InfoUser";
        DatabaseReference reference = database.getReference(databasePath);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    deleteQuantityProduct();
                    AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
                    builder.setTitle("Thông báo")
                            .setMessage("Đặt hàng thành công!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(PayActivity.this,MainActivity.class);
                                    startActivity(i);
                                }
                            }).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
                    builder.setTitle("Thông báo")
                            .setMessage("Bạn chưa có thông tin địa chỉ nhận hàng vui lòng kiểm tra lại !")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(PayActivity.this, InfoUserActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).setNegativeButton("Hủy", null)
                            .show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void totalPrice () {
        List<Product> listProduct = adapter.getProductList();
        for (Product product : listProduct) {
            int productPrice = product.getPrice();
            int productNumber = product.getNumber();
            int productTotalPrice = productPrice * productNumber;
            totalPay += productTotalPrice;
        }
        tv_totalPay.setText(String.valueOf(totalPay));
    }
    public void deleteQuantityProduct(){
        List<Product> productList = adapter.getProductList();
        String codeProduct;
        for (Product product : productList) {
            String userEmail = getCurrentUserEmail();
            String emailPath = userEmail.replace("@gmail.com", "");
            String databasePath ="/Users/"+ emailPath + "/productCodes";

            int quantity = product.getQuantity();
            int number = product.getNumber();
            int updatedQuantity = quantity - number;
            codeProduct = product.getCode();

            DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("products").child(product.getCode());
            productRef.child("quantity").setValue(updatedQuantity);

            DatabaseReference productCodeRef = FirebaseDatabase.getInstance().getReference(databasePath).child(codeProduct);
            productCodeRef.removeValue();
        }
    }
    public void anhxa(){
        rcv_itemPay = findViewById(R.id.rcv_itemPay);
        rcv_pay = findViewById(R.id.rcv_pay);
        ed_code = findViewById(R.id.ed_code);
        btn_code = findViewById(R.id.btn_code);
        btn_Pay = findViewById(R.id.btn_Pay);
        tvCode_sale = findViewById(R.id.tvCode_sale);
        tv_totalPay = findViewById(R.id.tv_totalPay);
        checkBox = findViewById(R.id.Pay_checkbox);
    }
}