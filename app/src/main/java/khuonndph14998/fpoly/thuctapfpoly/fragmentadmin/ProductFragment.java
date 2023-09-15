package khuonndph14998.fpoly.thuctapfpoly.fragmentadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import khuonndph14998.fpoly.thuctapfpoly.CreateProductActivity;
import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.auth.LoginActivity;

public class ProductFragment extends Fragment {
    ImageView iconToCreateProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        iconToCreateProduct = view.findViewById(R.id.icon_addProduct);
        iconToCreateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CreateProductActivity.class);
                startActivity(i);
            }
        });
        return view;

    }


}