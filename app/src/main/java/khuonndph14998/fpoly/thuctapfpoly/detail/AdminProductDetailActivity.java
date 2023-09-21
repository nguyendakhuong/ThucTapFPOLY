package khuonndph14998.fpoly.thuctapfpoly.detail;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import khuonndph14998.fpoly.thuctapfpoly.R;

public class AdminProductDetailActivity extends AppCompatActivity {
    private TextView detailName, detailDescribe, detailCode, detailNote, detailQuantity, detailCategory;
    private ImageView detailImage;
    private Button btnUpdate, btnDelete;

    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_detail);
        anhXa();
        productsRef = FirebaseDatabase.getInstance().getReference().child("products");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String id = extras.getString("code");
            String urlString = extras.getString("image");
            Uri uri = Uri.parse(urlString);
            detailName.setText(extras.getString("name"));
            detailDescribe.setText(extras.getString("describe"));
            detailCode.setText(extras.getString("code"));
            detailNote.setText(extras.getString("note"));
            detailQuantity.setText(String.valueOf(extras.getInt("quantity")));
            detailCategory.setText(extras.getString("selectedItem"));
            detailImage.setImageURI(uri);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog(id);
                }
            });
        }
    }

    private void anhXa() {
        detailName = findViewById(R.id.detail_admin_name);
        detailNote = findViewById(R.id.detail_admin_note);
        detailCode = findViewById(R.id.detail_admin_code);
        detailQuantity = findViewById(R.id.detail_admin_quantity);
        detailDescribe = findViewById(R.id.detail_admin_describe);
        detailCategory = findViewById(R.id.detail_admin_category);
        detailImage = findViewById(R.id.detail_admin_image);
        btnUpdate = findViewById(R.id.detail_admin_update);
        btnDelete = findViewById(R.id.detail_admin_delete);
    }

    private void showDeleteConfirmationDialog(String id) {
        new AlertDialog.Builder(this)
                .setTitle("Thông báo")
                .setMessage("Bạn có muốn xóa sản phẩm này không?")
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
        productsRef.child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AdminProductDetailActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AdminProductDetailActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}