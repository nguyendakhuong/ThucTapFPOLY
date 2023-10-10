package khuonndph14998.fpoly.thuctapfpoly.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import khuonndph14998.fpoly.thuctapfpoly.CardProductActivity;
import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.fragmentCategory.CategoryBeltFragment;
import khuonndph14998.fpoly.thuctapfpoly.fragmentCategory.CategoryJeansFragment;
import khuonndph14998.fpoly.thuctapfpoly.fragmentCategory.CategoryShirtFragment;
import khuonndph14998.fpoly.thuctapfpoly.fragmentCategory.CategoryShoesFragment;
import khuonndph14998.fpoly.thuctapfpoly.fragmentCategory.CategoryTshirtFragment;
import khuonndph14998.fpoly.thuctapfpoly.fragmentCategory.CategotyAllFragment;


public class HomeFragment extends Fragment {
    private Toolbar toolbar;
    private ImageView iconToCart;
    private RecyclerView rclViewHome;
    private ViewFlipper viewFlipper;

    private FrameLayout frameLayout;
    private Button btnAll,btnbelt,btnJeans,btnShirt,btnShoes,btnTshirt;
    FragmentManager fragmentManager;

    private TextView textCard;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = view.findViewById(R.id.toolbarHome);
        textCard = view.findViewById(R.id.icon_cart);
        viewFlipper = view.findViewById(R.id.viewFlipper);

        frameLayout = view.findViewById(R.id.FrameLayoutCategory);
        fragmentManager = getActivity().getSupportFragmentManager();
        btnAll = view.findViewById(R.id.categoryALl);
        btnbelt = view.findViewById(R.id.categoryTL);
        btnJeans = view.findViewById(R.id.categoryQB);
        btnShirt = view.findViewById(R.id.categoryASM);
        btnTshirt = view.findViewById(R.id.categoryTL);
        btnShoes = view.findViewById(R.id.categoryGD);
        numberOfCards();


        CategotyAllFragment fragmentAll = new CategotyAllFragment();
        fragmentManager.beginTransaction().replace(R.id.FrameLayoutCategory,fragmentAll).commit();

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategotyAllFragment fragment = new CategotyAllFragment();
                fragmentManager.beginTransaction().replace(R.id.FrameLayoutCategory,fragment).commit();
            }
        });
        btnbelt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryBeltFragment fragment = new CategoryBeltFragment();
                fragmentManager.beginTransaction().replace(R.id.FrameLayoutCategory,fragment).commit();
            }
        });
        btnShirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryShirtFragment fragment = new CategoryShirtFragment();
                fragmentManager.beginTransaction().replace(R.id.FrameLayoutCategory,fragment).commit();
            }
        });
        btnTshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryTshirtFragment fragment = new CategoryTshirtFragment();
                fragmentManager.beginTransaction().replace(R.id.FrameLayoutCategory,fragment).commit();
            }
        });
        btnJeans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryJeansFragment fragment = new CategoryJeansFragment();
                fragmentManager.beginTransaction().replace(R.id.FrameLayoutCategory,fragment).commit();
            }
        });
        btnShoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryShoesFragment fragment = new CategoryShoesFragment();
                fragmentManager.beginTransaction().replace(R.id.FrameLayoutCategory,fragment).commit();
            }
        });
        textCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CardProductActivity.class);
                startActivity(i);
            }
        });

        List<String> arrayAdvertisement  = new ArrayList<>();
        arrayAdvertisement .add("https://vn-live-01.slatic.net/p/72a4db5f460de80f920ab2da766a6b8a.jpg");
        arrayAdvertisement .add("https://vn-live-01.slatic.net/p/4ddecccebb4f73ce3beae5845a35ad87.jpg");
        arrayAdvertisement .add("https://vn-test-11.slatic.net/p/2f93ff17fcc0d1a76f699abeeef1ccfc.jpg");
        for (int i = 0; i < arrayAdvertisement .size() ; i++) {
            ImageView imageView = new ImageView(getActivity());
            Glide.with(getActivity()).load(arrayAdvertisement.get(i)).into(imageView);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);

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
    private void numberOfCards () {
        String userEmail = getCurrentUserEmail();
        if (userEmail != null ){
            String emailPath = userEmail.replace("@gmail.com", "");
            String databasePath = emailPath + "/productCodes";
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference(databasePath);
            databaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long count = dataSnapshot.getChildrenCount();
                    String countString = String.valueOf(count);
                    textCard.setText(countString);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }
}