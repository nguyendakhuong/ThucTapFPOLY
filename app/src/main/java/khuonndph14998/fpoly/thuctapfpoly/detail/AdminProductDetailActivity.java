package khuonndph14998.fpoly.thuctapfpoly.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.UpdateProductActivity;

public class AdminProductDetailActivity extends AppCompatActivity {
    private TextView detailName, detailDescribe, detailCode, detailNote, detailQuantity, detailCategory,detailPrice;
    private ImageView detailImage;
    private Button btnUpdate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_detail);
        anhXa();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String id = extras.getString("code");
            String name = extras.getString("name");
            String urlString = extras.getString("image");
            Uri uri = Uri.parse(urlString);

            detailName.setText(extras.getString("name"));
            detailDescribe.setText(extras.getString("describe"));
            detailCode.setText(extras.getString("code"));
            detailNote.setText(extras.getString("note"));
            detailQuantity.setText(String.valueOf(extras.getInt("quantity")));
            detailPrice.setText(String.valueOf(extras.getInt("price")));
            detailCategory.setText(extras.getString("selectedItem"));
            detailImage.setImageURI(uri);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog(id,name);
                }
            });
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    Intent i = new Intent(AdminProductDetailActivity.this, UpdateProductActivity.class);

                    bundle.putString("nameUpdate",detailName.getText().toString());
                    bundle.putString("codeUpdate",detailCode.getText().toString());
                    bundle.putString("describeUpdate",detailDescribe.getText().toString());
                    bundle.putString("noteUpdate",detailNote.getText().toString());
                    bundle.putInt("quantityUpdate", Integer.parseInt(detailQuantity.getText().toString()));
                    bundle.putInt("priceUpdate",Integer.parseInt(detailPrice.getText().toString()));
                    bundle.putString("imageUpdate", urlString);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });
        }
    }

    private void anhXa() {
        detailName = findViewById(R.id.detail_admin_name);
        detailNote = findViewById(R.id.detail_admin_note);
        detailCode = findViewById(R.id.detail_admin_code);
        detailQuantity = findViewById(R.id.detail_admin_quantity);
        detailPrice = findViewById(R.id.detail_admin_price);
        detailDescribe = findViewById(R.id.detail_admin_describe);
        detailCategory = findViewById(R.id.detail_admin_category);
        detailImage = findViewById(R.id.detail_admin_image);
        btnUpdate = findViewById(R.id.detail_admin_update);
        btnDelete = findViewById(R.id.detail_admin_delete);

    }

    private void showDeleteConfirmationDialog(String id,String name) {
        new AlertDialog.Builder(this)
                .setTitle("Thông báo")
                .setMessage("Bạn có muốn xóa sản phẩm "+name+" không?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProduct(id);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void deleteProduct(String id) {
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        DatabaseReference productsRef  = fb.getReference("products");

        productsRef.child(id).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminProductDetailActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AdminProductDetailActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}