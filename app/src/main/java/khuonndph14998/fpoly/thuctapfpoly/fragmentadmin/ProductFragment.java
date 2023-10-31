package khuonndph14998.fpoly.thuctapfpoly.fragmentadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import khuonndph14998.fpoly.thuctapfpoly.CreateProductActivity;
import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.adapter.ProductAdapter;
import khuonndph14998.fpoly.thuctapfpoly.detail.AdminProductDetailActivity;
import khuonndph14998.fpoly.thuctapfpoly.listener.ItemProductListener;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;

public class ProductFragment extends Fragment implements ItemProductListener {
    private ImageView iconToCreateProduct;

    private RecyclerView rclView_user;
    private ArrayList<Product> productArrayList;
    ProductAdapter productAdapter;
    private FirebaseFirestore db;
    private Product product;

    private ProgressDialog progressDialog;
    ProductAdapter.ProductViewHolder productViewHolder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        iconToCreateProduct = view.findViewById(R.id.icon_addProduct);

        rclView_user = view.findViewById(R.id.product_rclView);
        rclView_user.setHasFixedSize(true);
        rclView_user.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        productArrayList = new ArrayList<Product>();
        productAdapter = new ProductAdapter(getContext(),productArrayList,this);
        rclView_user.setAdapter(productAdapter);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang tải dữ liệu");
        progressDialog.show();
        loadProducts();
        progressDialog.dismiss();



        iconToCreateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CreateProductActivity.class);
                startActivity(i);
            }
        });
        return view;

    }

    private void loadProducts() {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productArrayList.clear();
                for (DataSnapshot sp : snapshot.getChildren()) {
                    Product product = sp.getValue(Product.class);
                    productArrayList.add(product);
                }
                productAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Lỗi khi tải dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemClickProduct(Product product) {
        Bundle bundle = new Bundle();
        Intent i = new Intent(getContext(), AdminProductDetailActivity.class);
        bundle.putString("name",product.getName());
        bundle.putString("code",product.getCode());
        bundle.putString("describe",product.getDescribe());
        bundle.putString("note",product.getNote());
        bundle.putInt("quantity",product.getQuantity());
        bundle.putInt("price",product.getPrice());
        bundle.putString("image",product.getImage());
        bundle.putString("selectedItem",product.getSelectedItem());
        i.putExtras(bundle);
        startActivity(i);
    }
}