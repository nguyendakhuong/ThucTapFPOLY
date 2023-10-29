package khuonndph14998.fpoly.thuctapfpoly.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.UpdateProductActivity;

public class DiscountDetailActivity extends AppCompatActivity {
    private TextView tv_discountName,tv_discountCode,tv_discountQuantity,tv_discountTime,tv_discountPrice,tv_discountNote;
    private ImageView img_discount;
    private Button btn_updateDiscount, btn_deleteDiscount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_detail);
        anhxa();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String id = extras.getString("discountCode");
            String name = extras.getString("discountName");
            String quantity = String.valueOf(extras.getInt("discountQuantity"));
            String price = String.valueOf(extras.getInt("discountPrice"));
            String urlString = extras.getString("discountImage");
            Uri uri = Uri.parse(urlString);
            tv_discountName.setText(extras.getString("discountName"));
            tv_discountCode.setText(extras.getString("discountCode"));
            tv_discountNote.setText(extras.getString("discountNote"));
            tv_discountQuantity.setText(String.valueOf(extras.getInt("discountQuantity")));
            tv_discountPrice.setText(String.valueOf(extras.getInt("discountPrice")));
            tv_discountTime.setText(extras.getString("discountTime"));
            img_discount.setImageURI(uri);

            btn_deleteDiscount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteItemDialog(id,name);
                }
            });
            btn_updateDiscount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openUpdateDialog(Gravity.CENTER,id,name,quantity,price);
                }
            });

        }

    }
    private void anhxa(){
        tv_discountName = findViewById(R.id.tv_detailDiscountName);
        tv_discountCode = findViewById(R.id.tv_detailDiscountCode);
        tv_discountQuantity = findViewById(R.id.tv_detailDiscountQuantity);
        tv_discountPrice = findViewById(R.id.tv_detailDiscountPrice);
        tv_discountTime = findViewById(R.id.tv_detailDiscountTime);
        tv_discountNote = findViewById(R.id.tv_detailDiscountNote);
        img_discount = findViewById(R.id.detail_discount_image);
        btn_updateDiscount = findViewById(R.id.detail_discount_update);
        btn_deleteDiscount = findViewById(R.id.detail_discount_delete);
    }
    private void showDeleteItemDialog(String id, String name){
        new AlertDialog.Builder(this)
                .setTitle("Thông báo")
                .setMessage("Bạn có muốn xóa mã giảm giá "+name+" không?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProduct(id);
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    private void deleteProduct(String id){
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        DatabaseReference productsRef  = fb.getReference("Discount");
        productsRef.child(id).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(DiscountDetailActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(DiscountDetailActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void openUpdateDialog(int gravity,String code,String name,String quantity,String price){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_update_discount);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams);
        dialog.setCancelable(true);

        TextInputEditText input_updateDiscount_name,input_updateDiscount_quantity,input_updateDiscount_price;
        Button btnCancel,btnOk;

        input_updateDiscount_name = dialog.findViewById(R.id.input_updateDiscount_name);
        input_updateDiscount_quantity = dialog.findViewById(R.id.input_updateDiscount_quantity);
        input_updateDiscount_price = dialog.findViewById(R.id.input_updateDiscount_price);
        btnCancel = dialog.findViewById(R.id.btn_updateDiscount_cancel);
        btnOk = dialog.findViewById(R.id.btn_updateDiscount_ok);

        input_updateDiscount_name.setText(name);
        input_updateDiscount_quantity.setText(quantity);
        input_updateDiscount_price.setText(price);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updateDiscount_name = input_updateDiscount_name.getText().toString().trim();
                String updateDiscount_quantity = input_updateDiscount_quantity.getText().toString().trim();
                String updateDiscount_price = input_updateDiscount_price.getText().toString().trim();

                if (TextUtils.isEmpty(updateDiscount_name)||
                        TextUtils.isEmpty(updateDiscount_quantity) || TextUtils.isEmpty(updateDiscount_price)) {
                    Toast.makeText(DiscountDetailActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isDigitsOnly(updateDiscount_quantity)) {
                    Toast.makeText(DiscountDetailActivity.this, "Số lượng phải là một số", Toast.LENGTH_SHORT).show();
                    return;
                }
                int quantityInt = Integer.parseInt(updateDiscount_quantity);
                int priceInt = Integer.parseInt(updateDiscount_price);

                if (quantityInt <= 0) {
                    Toast.makeText(DiscountDetailActivity.this, "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (priceInt <= 0) {
                    Toast.makeText(DiscountDetailActivity.this, "Giá tiền phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                DatabaseReference discountRef = FirebaseDatabase.getInstance().getReference().child("Discount");
                Map<String,Object> updateDiscount = new HashMap<>();
                updateDiscount.put("name",updateDiscount_name);
                updateDiscount.put("quantity",updateDiscount_quantity);
                updateDiscount.put("price",updateDiscount_price);

                discountRef.child(code).updateChildren(updateDiscount)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(DiscountDetailActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DiscountDetailActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        dialog.show();
    }
}