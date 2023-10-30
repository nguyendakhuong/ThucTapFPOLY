package khuonndph14998.fpoly.thuctapfpoly.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import khuonndph14998.fpoly.thuctapfpoly.CreateProductActivity;
import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.model.Discount;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;

public class CreateDiscountCodeActivity extends AppCompatActivity {
    private TextInputEditText input_DiscountName,input_DiscountQuantity,input_Discount,input_DiscountNote,input_DiscountPrice;
    private TextView tv_datePicker;
    private ImageView imageViewDiscount;
    private Button btnDatePicker,btnDiscount;
    private Calendar calendar;
    private String selectedDate = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_discount_code);
        anhxa();
        calendar = Calendar.getInstance();

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        btnDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDiscount();
            }
        });
    }

    private void anhxa(){
        input_DiscountName = findViewById(R.id.input_code_name);
        input_DiscountQuantity = findViewById(R.id.input_code_quantity);
        input_DiscountNote = findViewById(R.id.input_code_note);
        input_Discount = findViewById(R.id.input_discount);
        input_DiscountPrice = findViewById(R.id.input_price);
        tv_datePicker = findViewById(R.id.tv_datePicker);
        btnDatePicker = findViewById(R.id.btnDatePicker);
        btnDiscount = findViewById(R.id.btn_CreateCode);

    }
    public void openDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar selectedDateCalendar = Calendar.getInstance();
                selectedDateCalendar.set(year, month, dayOfMonth);
                if (selectedDateCalendar.after(calendar)) {
                    calendar = selectedDateCalendar;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    selectedDate = dateFormat.format(calendar.getTime());
                    updateSelectedDate(selectedDate);
                    processSelectedDate();
                } else {
                    Toast.makeText(CreateDiscountCodeActivity.this, "Ngày được chọn phải lớn hơn ngày hôm nay", Toast.LENGTH_SHORT).show();
                }
            }
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void updateSelectedDate(String date) {
        selectedDate = date;
        tv_datePicker.setText(selectedDate);
    }
    private void processSelectedDate() {
        Log.d("date", selectedDate);
    }
    private void createDiscount(){
        processSelectedDate();
        String name = input_DiscountName.getText().toString().trim();
        String quantity = input_DiscountQuantity.getText().toString().trim();
        String price = input_DiscountPrice.getText().toString().trim();
        String note = input_DiscountNote.getText().toString().trim();
        String discount = input_Discount.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(quantity) || TextUtils.isEmpty(note) || TextUtils.isEmpty(discount) || TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        if (discount.length() < 6 || discount.length() >12 ){
            Toast.makeText(this, "Mã code tối thiểu là 6 kí tự và tối đa là 12", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isDigitsOnly(quantity)) {
            Toast.makeText(this, "Số lượng phải là một số", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isDigitsOnly(price)) {
            Toast.makeText(this, "Giá tiền phải là số", Toast.LENGTH_SHORT).show();
            return;
        }
        int quantityInt = Integer.parseInt(quantity);
        int priceInt = Integer.parseInt(price);

        if (quantityInt <= 0) {
            Toast.makeText(this, "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            return;
        }
        if (priceInt <= 0) {
            Toast.makeText(this, "Giá tiền phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            return;
        }
        Discount dc = new Discount(name,discount,selectedDate,note,quantityInt,priceInt);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Discount");
        databaseReference.orderByChild("discount").equalTo(discount).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(CreateDiscountCodeActivity.this, "Mã giảm giá đã tồn tại", Toast.LENGTH_SHORT).show();
                }else {
                    String discountId = String.valueOf(dc.getDc_code());
                    databaseReference.child(discountId).setValue(dc)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(CreateDiscountCodeActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                    clearForm();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CreateDiscountCodeActivity.this, "Có lỗi hệ thống không thể thêm", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CreateDiscountCodeActivity.this, "Lỗi :" +error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearForm(){
        processSelectedDate();
        input_DiscountName.setText("");
        input_Discount.setText("");
        input_DiscountNote.setText("");
        input_DiscountQuantity.setText("");
        input_DiscountPrice.setText("");
        tv_datePicker.setText("");
    }
}