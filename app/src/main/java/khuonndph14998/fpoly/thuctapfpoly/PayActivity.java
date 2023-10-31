package khuonndph14998.fpoly.thuctapfpoly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import khuonndph14998.fpoly.thuctapfpoly.adapter.DetailPayAdapter;
import khuonndph14998.fpoly.thuctapfpoly.adapter.PayAdapter;
import khuonndph14998.fpoly.thuctapfpoly.detail.DiscountDetailActivity;
import khuonndph14998.fpoly.thuctapfpoly.model.Bill;
import khuonndph14998.fpoly.thuctapfpoly.model.Discount;
import khuonndph14998.fpoly.thuctapfpoly.model.InfoUser;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;
import khuonndph14998.fpoly.thuctapfpoly.user.InfoUserActivity;

public class PayActivity extends AppCompatActivity{

    Button btn_code,btn_Pay;
    TextView tvCode_sale,tv_totalPay,tv_address,tv_discount;
    CheckBox checkBox;

    RecyclerView rcv_itemPay,rcv_pay;
    PayAdapter adapter;
    DetailPayAdapter detailPayAdapter;
    String discountCodeStr;

    private int reducedMoney = 0;
    int newTotal;
    String address;
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

        String userEmail = getCurrentUserEmail();
        if (userEmail != null){
            String emailPath = userEmail.replace("@gmail.com", "");
            String databasePath ="/Users/"+ emailPath + "/InfoUser";
            DatabaseReference dataRefInfo = FirebaseDatabase.getInstance().getReference(databasePath);
            dataRefInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    InfoUser infoUser = snapshot.getValue(InfoUser.class);
                    if (infoUser != null) {
                        address = infoUser.getDeliveryAddress();
                        tv_address.setText(address);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        totalPrice(reducedMoney);

        btn_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiscountCodeDialog(Gravity.CENTER);
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

    }
    private void openDiscountCodeDialog(int gravity){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_discount_code);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams);
        dialog.setCancelable(true);

        TextInputEditText input_discountCode;
        Button btnCancel,btnOk;

        input_discountCode = dialog.findViewById(R.id.input_discount);
        btnCancel = dialog.findViewById(R.id.btn_discount_cancel);
        btnOk = dialog.findViewById(R.id.btn_discount_ok);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discountCodeStr = input_discountCode.getText().toString().trim();
                if (TextUtils.isEmpty(discountCodeStr)) {
                    Toast.makeText(PayActivity.this, "Vui lòng nhập mã giảm giá", Toast.LENGTH_SHORT).show();
                    return;
                }
                String userEmail = getCurrentUserEmail();
                if (userEmail != null){
                    String emailPath = userEmail.replace("@gmail.com", "");
                    String databasePath ="/Users/"+ emailPath + "/MyDiscount";
                    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference(databasePath);
                    dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(discountCodeStr)){
                                Toast.makeText(PayActivity.this, "Mã đã được bạn sử dụng", Toast.LENGTH_SHORT).show();
                            }else {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Discount");
                                Query query = databaseReference.orderByChild("dc_code").equalTo(discountCodeStr);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        Discount discount = dataSnapshot.getValue(Discount.class);
                                            if (discount != null) {
                                                reducedMoney = discount.getDc_price();
                                            }
                                        }
                                        tvCode_sale.setText(String.valueOf(reducedMoney));
                                        totalPrice(reducedMoney);
                                        btn_code.setEnabled(false);
                                        Toast.makeText(PayActivity.this, "Nhận code thành công", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        tv_discount.setText(discountCodeStr);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(PayActivity.this, "Mã không tồn tại", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
        dialog.show();
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

    public void UseDiscountCode(){
        if (discountCodeStr == null || discountCodeStr.isEmpty()) {
            return;
        }
        String userEmail = getCurrentUserEmail();
        if (userEmail != null) {
            String emailPath = userEmail.replace("@gmail.com", "");
            String databasePath = "/Users/" + emailPath + "/MyDiscount";
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);
            DatabaseReference discountCodeRef = databaseReference.child(discountCodeStr);
            discountCodeRef.setValue(discountCodeStr);
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
                    UseDiscountCode();
                    deleteQuantityDiscountCode();
                    createBill();
                    AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
                    builder.setTitle("Thông báo")
                            .setMessage("Đặt hàng thành công!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(PayActivity.this,MainActivity.class);
                                    startActivity(i);
                                    finish();
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
    private void createBill(){
        String userEmail = getCurrentUserEmail();
        if(userEmail != null) {
            String emailPath = userEmail.replace("@gmail.com", "");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bill");
            List<Product> productList = adapter.getProductList();
            String billId = databaseReference.push().getKey();
            Date currentTime = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm dd/MM/yyyy");
            String formattedTime = sdf.format(currentTime);

            List<String> productNameList = new ArrayList<>();
            List<String> productCode = new ArrayList<>();
            List<Integer> quantityList = new ArrayList<>();
            for (Product product : productList) {
                productCode.add(product.getCode());
                productNameList.add(product.getName());
                quantityList.add(product.getNumber());
            }
            Bill bill = new Bill(billId,newTotal,emailPath,address,formattedTime,productNameList,productCode,quantityList);
            databaseReference.child(billId).setValue(bill);
        }
    }
    public void totalPrice (int reducedMoney) {
        List<Product> listProduct = adapter.getProductList();
        int totalPay = 0;
        for (Product product : listProduct) {
            int productPrice = product.getPrice();
            int productNumber = product.getNumber();
            int productTotalPrice = productPrice * productNumber;
            totalPay += productTotalPrice;
        }

        newTotal = totalPay - reducedMoney;
        if (newTotal < 0) {
            tv_totalPay.setText(String.valueOf(0));
            return;
        }
        tv_totalPay.setText(String.valueOf(newTotal));
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
    public void deleteQuantityDiscountCode(){
        if (discountCodeStr == null || discountCodeStr.isEmpty()) {
            return;
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Discount");
        databaseReference.child(discountCodeStr).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Discount discount = currentData.getValue(Discount.class);
                if ( discount == null ){
                    return Transaction.success(currentData);
                }
                int newQuantity = discount.getDc_quantity() - 1;
                if (newQuantity < 0) {
                    newQuantity = 0;
                }

                discount.setDc_quantity(newQuantity);
                currentData.setValue(discount);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (committed) {
                    Log.d("tag", "Quantity updated successfully");
                } else {
                    Log.d("tag", "Quantity update failed");
                }
            }
        });
    }
    public void anhxa(){
        rcv_itemPay = findViewById(R.id.rcv_itemPay);
        rcv_pay = findViewById(R.id.rcv_pay);
        tv_discount = findViewById(R.id.tv_discount);
        btn_code = findViewById(R.id.btn_code);
        btn_Pay = findViewById(R.id.btn_Pay);
        tvCode_sale = findViewById(R.id.tvCode_sale);
        tv_totalPay = findViewById(R.id.tv_totalPay);
        tv_address = findViewById(R.id.tv_address);
        checkBox = findViewById(R.id.Pay_checkbox);
    }
}