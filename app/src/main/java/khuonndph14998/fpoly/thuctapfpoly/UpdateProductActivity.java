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
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import khuonndph14998.fpoly.thuctapfpoly.detail.AdminProductDetailActivity;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;

public class UpdateProductActivity extends AppCompatActivity {
    private String[] itemsUpdate = {"Bộ quần áo","Áo thun","Quần bò","Giày dép","Áo sơmi","Thắt lưng"};
    private TextInputEditText inputName,inputQuantity,inputNote,inputDescribe,inputPrice;
    private TextView tvCode;
    private ImageView imageProduct;
    private Button btnUpdateProduct;
    private ProgressBar progressBar;
    private ArrayAdapter<String> adapterItemUpdate;
    AutoCompleteTextView autoCompleteTextViewUpdate;
    private String selectedItem;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        anhxa();
        receiveData();

        adapterItemUpdate = new ArrayAdapter<String>(this,R.layout.list_select_item,itemsUpdate);
        autoCompleteTextViewUpdate.setAdapter(adapterItemUpdate);
        autoCompleteTextViewUpdate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                selectedItem = parent.getItemAtPosition(i).toString();
            }
        });
        imageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions();
            }
        });
        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString().trim();
                String code = tvCode.getText().toString();
                String quantityText = inputQuantity.getText().toString().trim();
                String note = inputNote.getText().toString().trim();
                String describe = inputDescribe.getText().toString().trim();
                String priceText = inputPrice.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(quantityText) ||
                        TextUtils.isEmpty(priceText) || TextUtils.isEmpty(describe) || TextUtils.isEmpty(note)) {
                    Toast.makeText(UpdateProductActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!TextUtils.isDigitsOnly(quantityText)) {
                    Toast.makeText(UpdateProductActivity.this, "Số lượng phải là một số", Toast.LENGTH_SHORT).show();
                    return;
                }
                int quantity = Integer.parseInt(quantityText);
                int price = Integer.parseInt(priceText);

                if (quantity <= 0) {
                    Toast.makeText(UpdateProductActivity.this, "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (price <= 0) {
                    Toast.makeText(UpdateProductActivity.this, "Giá tiền phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedItem == null) {
                    Toast.makeText(UpdateProductActivity.this, "Bạn phải chọn một mục trong thể loại", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (imageUri == null) {
                    Toast.makeText(UpdateProductActivity.this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("products");
                Map<String, Object> updates = new HashMap<>();
                updates.put("name", name);
                updates.put("code", code);
                updates.put("quantity", quantity);
                updates.put("price", price);
                updates.put("describe", describe);
                updates.put("note", note);
                updates.put("selectedItem",selectedItem);
                updates.put("image",imageUri.toString());

                productsRef.child(code).updateChildren(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UpdateProductActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(UpdateProductActivity.this, AdminProductDetailActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateProductActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
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
    public void receiveData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String urlString = extras.getString("imageUpdate");
            Uri uri = Uri.parse(urlString);

            String name = extras.getString("nameUpdate");
            String describe = extras.getString("describeUpdate");
            String code = extras.getString("codeUpdate");
            String note = extras.getString("noteUpdate");
            int quantity = extras.getInt("quantityUpdate");
            int price = extras.getInt("priceUpdate");

            inputName.setText(name);
            inputDescribe.setText(describe);
            tvCode.setText(code);
            inputNote.setText(note);
            inputQuantity.setText(String.valueOf(quantity));
            inputPrice.setText(String.valueOf(price));
            imageProduct.setImageURI(uri);
        }
    }
    public void anhxa(){
        inputName = findViewById(R.id.input_update_name);
        tvCode = findViewById(R.id.tv_update_code);
        inputQuantity = findViewById(R.id.input_update_quantity);
        inputNote = findViewById(R.id.input_update_note);
        inputDescribe = findViewById(R.id.input_update_describe);
        inputPrice = findViewById(R.id.input_update_price);
        imageProduct = findViewById(R.id.imageView_update);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        progressBar = findViewById(R.id.progressbar_update);
        autoCompleteTextViewUpdate = findViewById(R.id.auto_complete_update);

    }
}