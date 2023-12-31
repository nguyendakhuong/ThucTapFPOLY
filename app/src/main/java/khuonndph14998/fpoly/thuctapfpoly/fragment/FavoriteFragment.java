package khuonndph14998.fpoly.thuctapfpoly.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.adapter.FavAdapter;
import khuonndph14998.fpoly.thuctapfpoly.detail.UserProductDetailActivity;
import khuonndph14998.fpoly.thuctapfpoly.listener.ItemFavListener;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;


public class FavoriteFragment extends Fragment implements ItemFavListener {
    private  RecyclerView recyclerView;
    private ArrayList<Product> productArrayList = new ArrayList<>();
    private FavAdapter adapter;
    private FirebaseFirestore db;
    private Product product;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView = view.findViewById(R.id.recv_fav);
        recyclerView.setHasFixedSize(true);
        db = FirebaseFirestore.getInstance();
        productArrayList = new ArrayList<Product>();
        adapter = new FavAdapter(getContext(),productArrayList,this, new FavAdapter.IClickListener() {
            @Override
            public void onClickDeleteItemFav(Product p) {
                onClickDeleteItemFavorite(p);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getCode();

        return view;
    }
    private String getCurrentUserEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getEmail();
        } else {
            return null;
        }
    }

    private void getCode() {
        String userEmail = getCurrentUserEmail();
        if (userEmail != null) {
            String emailPath = userEmail.replace("@gmail.com", "");
            String databasePath = "/Users/" + emailPath + "/FavProduct";
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> productCodeList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String productCode = snapshot.getKey();
                        productCodeList.add(productCode);
                    }
                    for (String code : productCodeList) {
                        loadProducts(code);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi
                }
            });
        }
    }

    private void loadProducts(String productCode) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");
        productsRef.orderByChild("code").equalTo(productCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Product> productList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }
                productArrayList.addAll(productList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi
            }
        });
    }


    private void onClickDeleteItemFavorite (Product p) {
        String userEmail = getCurrentUserEmail();
        new AlertDialog.Builder(getContext())
                .setTitle("Thông báo")
                .setMessage("Bạn có chắc chắn muốn xóa sản phẩm "+p.getName()+" này không?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {String emailPath = userEmail.replace("@gmail.com", "");
                    String databasePath = "/Users/" + emailPath + "/FavProduct";
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);
                    databaseReference.child(String.valueOf(p.getCode())).removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            productArrayList.remove(p);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();

                        }
                    });

                    }
                })
                .setNegativeButton("Hủy",null)
                .show();
    }

    @Override
    public void onItemClickFav(Product product) {
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