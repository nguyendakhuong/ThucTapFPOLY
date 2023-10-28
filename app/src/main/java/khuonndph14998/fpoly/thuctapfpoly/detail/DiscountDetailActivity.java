package khuonndph14998.fpoly.thuctapfpoly.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import khuonndph14998.fpoly.thuctapfpoly.R;

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
}