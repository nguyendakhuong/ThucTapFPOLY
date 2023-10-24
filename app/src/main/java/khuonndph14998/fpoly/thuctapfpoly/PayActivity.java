package khuonndph14998.fpoly.thuctapfpoly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import khuonndph14998.fpoly.thuctapfpoly.adapter.PayAdapter;
import khuonndph14998.fpoly.thuctapfpoly.model.PayProduct;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;

public class PayActivity extends AppCompatActivity {
    RecyclerView rcv_itemPay,rcv_pay;
    EditText ed_code;
    Button btn_code,btn_Pay;
    TextView tvCode_sale,tv_totalPay;
    CheckBox checkBox;

    PayAdapter adapter;
    ArrayList<PayProduct> productArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        anhxa();

//        rcv_itemPay.setHasFixedSize(true);
//        adapter = new PayAdapter(this, productArrayList);
//        rcv_itemPay.setAdapter(adapter);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        rcv_itemPay.setLayoutManager(layoutManager);

        if (productArrayList == null || productArrayList.size() == 0) {
            // Hiển thị thông báo hoặc xử lý khi không có dữ liệu
            Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            // Khởi tạo và thiết lập adapter
            adapter = new PayAdapter(this, productArrayList);
            rcv_itemPay.setAdapter(adapter);

            // Khởi tạo và thiết lập layout manager
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rcv_itemPay.setLayoutManager(layoutManager);
        }

        PayProduct p = new PayProduct();
        btn_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public void anhxa(){
        rcv_itemPay = findViewById(R.id.rcv_itemPay);
        rcv_pay = findViewById(R.id.rcv_pay);
        ed_code = findViewById(R.id.ed_code);
        btn_code = findViewById(R.id.btn_code);
        btn_Pay = findViewById(R.id.btn_Pay);
        tvCode_sale = findViewById(R.id.tvCode_sale);
        tv_totalPay = findViewById(R.id.tv_totalPay);
        checkBox = findViewById(R.id.checkboxPay);
    }
}