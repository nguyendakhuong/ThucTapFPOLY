package khuonndph14998.fpoly.thuctapfpoly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import khuonndph14998.fpoly.thuctapfpoly.adapter.PayAdapter;
import khuonndph14998.fpoly.thuctapfpoly.adapter.ProductAdapter;
import khuonndph14998.fpoly.thuctapfpoly.model.PayProduct;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;

public class PayActivity extends AppCompatActivity {

    EditText ed_code;
    Button btn_code,btn_Pay;
    TextView tvCode_sale,tv_totalPay;
    CheckBox checkBox;

    RecyclerView rcv_itemPay,rcv_pay;
    PayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        anhxa();
        rcv_itemPay.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        String productListJson = sharedPreferences.getString("productList", "");
        Log.d("productListJson" ,"" + productListJson);
        List<Product> productList = convertJsonToProductList(productListJson);
        adapter = new PayAdapter(getApplicationContext(),productList);
        rcv_itemPay.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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
    private List<Product> convertJsonToProductList(String json) {
        List<Product> productList = new ArrayList<>();
        Log.d("JSON3", json);
        Log.d("JSON4", String.valueOf(productList));
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.d("jsonObject", String.valueOf(jsonObject));

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