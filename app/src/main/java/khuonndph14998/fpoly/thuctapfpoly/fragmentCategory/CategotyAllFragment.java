package khuonndph14998.fpoly.thuctapfpoly.fragmentCategory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.adapter.CardProductAdapter;
import khuonndph14998.fpoly.thuctapfpoly.detail.UserProductDetailActivity;
import khuonndph14998.fpoly.thuctapfpoly.listener.ItemProductListener;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;

public class CategotyAllFragment extends Fragment implements ItemProductListener {
    private ImageView iconToCreateProduct;

    private RecyclerView rclView_cardProductAll;
    private ArrayList<Product> cardProductArrayList;
    CardProductAdapter adapter;
    private FirebaseFirestore db;
    private Product product;

    private ProgressDialog progressDialog;

    private String getCurrentUserEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getEmail();
        } else {
            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categoty_all, container, false);

        rclView_cardProductAll = view.findViewById(R.id.cardProduct_rclView_All);
        rclView_cardProductAll.setHasFixedSize(true);

        db = FirebaseFirestore.getInstance();
        cardProductArrayList = new ArrayList<Product>();
        adapter = new CardProductAdapter(getContext(),cardProductArrayList,this);
        rclView_cardProductAll.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        rclView_cardProductAll.setLayoutManager(gridLayoutManager);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang tải dữ liệu");
        progressDialog.show();
        loadProducts();
        progressDialog.dismiss();

        return view;
    }

    private void loadProducts() {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");
        productsRef.orderByChild("quantity").startAfter(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cardProductArrayList.clear();
                for (DataSnapshot sp : snapshot.getChildren()) {
                    Product product = sp.getValue(Product.class);
                    if (product != null && product.getQuantity() > 0) {
                        cardProductArrayList.add(product);
                    }
                }
                adapter.notifyDataSetChanged();
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
        Intent i = new Intent(getContext(), UserProductDetailActivity.class);
        bundle.putString("name",product.getName());
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