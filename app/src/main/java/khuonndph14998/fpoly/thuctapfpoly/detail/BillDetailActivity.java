package khuonndph14998.fpoly.thuctapfpoly.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.adapter.BillProductAdapter;
import khuonndph14998.fpoly.thuctapfpoly.adapter.ProductAdapter;
import khuonndph14998.fpoly.thuctapfpoly.model.Bill;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;

public class BillDetailActivity extends AppCompatActivity {
    private Bill bill;
    private TextView tvBillId,tvBillEmail,tvBillTotal,tvBillTime,tvBillAddress;
    private RecyclerView rcv_product;
    private BillProductAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        anhxa();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bill = (Bill) bundle.getSerializable("bill");
        }
        if (bill != null) {
            tvBillId.setText(bill.getBillId());
            tvBillTotal.setText(String.valueOf(bill.getTotalPay()));
            tvBillEmail.setText(bill.getPayEr());
            tvBillAddress.setText(bill.getCustomerAddress());
            tvBillTime.setText(bill.getTime());

            productList = new ArrayList<>();
            for (int i = 0; i < bill.getProductCode().size(); i++) {
                Product product = new Product();
                product.setCode(bill.getProductCode().get(i));
                product.setName(bill.getProductNameList().get(i));
                product.setNumber(bill.getQuantityList().get(i));
                productList.add(product);
            }
            adapter = new BillProductAdapter(getApplicationContext(),productList);
            rcv_product.setLayoutManager(new LinearLayoutManager(this));
            rcv_product.setAdapter(adapter);

        }
    }
    private void anhxa() {
        tvBillId = findViewById(R.id.tv_bill_id);
        tvBillEmail = findViewById(R.id.tv_bill_email);
        tvBillTotal = findViewById(R.id.tv_bill_total);
        tvBillTime = findViewById(R.id.tv_bill_time);
        tvBillAddress = findViewById(R.id.tv_bill_address);
        rcv_product = findViewById(R.id.rcv_products);
    }
}