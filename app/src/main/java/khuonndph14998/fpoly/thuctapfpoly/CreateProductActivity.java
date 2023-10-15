package khuonndph14998.fpoly.thuctapfpoly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import khuonndph14998.fpoly.thuctapfpoly.databinding.FragmentProductBinding;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;

public class CreateProductActivity extends AppCompatActivity {
    private String[] items = {"Bộ quần áo","Áo thun","Quần bò","Giày dép","Áo sơmi","Thắt lưng"};
    AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItem;
    private TextInputEditText inputName,inputCode,inputQuantity,inputNote,inputDescribe,inputPrice;
    private ImageView imageProduct;
    private Button btnCreateProduct;
    private ProgressBar progressBar;
    private Uri imageUri;
    private String selectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        anhxa();

        adapterItem = new ArrayAdapter<String>(this,R.layout.list_select_item,items);
        autoCompleteTextView.setAdapter(adapterItem);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                selectedItem = parent.getItemAtPosition(i).toString();
            }
        });
        imageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions ();
            }
        });
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product p = new Product();
                String name = inputName.getText().toString().trim();
                String code = inputCode.getText().toString().trim();
                String quantityText = inputQuantity.getText().toString().trim();
                String note = inputNote.getText().toString().trim();
                String describe = inputDescribe.getText().toString().trim();
                String priceText = inputPrice.getText().toString().trim();
                int favStatus = Integer.parseInt(String.valueOf(1));


                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(code) || TextUtils.isEmpty(quantityText) ||
                        TextUtils.isEmpty(priceText) || TextUtils.isEmpty(describe) || TextUtils.isEmpty(note)) {
                    Toast.makeText(CreateProductActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!TextUtils.isDigitsOnly(quantityText)) {
                    Toast.makeText(CreateProductActivity.this, "Số lượng phải là một số", Toast.LENGTH_SHORT).show();
                    return;
                }

                int quantity = Integer.parseInt(quantityText);
                int price = Integer.parseInt(priceText);

                if (quantity <= 0) {
                    Toast.makeText(CreateProductActivity.this, "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedItem == null) {
                    Toast.makeText(CreateProductActivity.this, "Bạn phải chọn một mục trong thể loại", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (imageUri == null) {
                    Toast.makeText(CreateProductActivity.this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                Product product = new Product(name, code, quantity, note, describe, selectedItem, imageUri.toString(),price,favStatus);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");
                databaseReference.orderByChild("code").equalTo(code).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Toast.makeText(CreateProductActivity.this, "Sản phẩm đã tồn tại", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            String productId = String.valueOf(product.getCode());
                            databaseReference.child(productId).setValue(product)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(CreateProductActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            clearForm();
//                                            Intent i = new Intent(CreateProductActivity.this, FragmentProductBinding.class);
//                                            startActivity(i);
//                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(CreateProductActivity.this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(CreateProductActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void requestPermissions() {
        ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = data.getData();
        imageProduct.setImageURI(imageUri);
    }
    private void clearForm () {
        inputName.setText("");
        inputCode.setText("");
        inputDescribe.setText("");
        inputNote.setText("");
        inputQuantity.setText("");
        inputPrice.setText("");
        imageProduct.setImageResource(R.drawable.ic_baseline_image);
    }

    private void anhxa() {
        inputName = findViewById(R.id.input_product_name);
        inputCode = findViewById(R.id.input_product_code);
        inputQuantity = findViewById(R.id.input_product_quantity);
        inputNote = findViewById(R.id.input_product_note);
        inputDescribe = findViewById(R.id.input_product_describe);
        inputPrice = findViewById(R.id.input_product_price);
        imageProduct = findViewById(R.id.imageView_product);
        btnCreateProduct = findViewById(R.id.btnCreateProduct);
        autoCompleteTextView = findViewById(R.id.auto_complete);
        progressBar = findViewById(R.id.progressbar);

    }
}