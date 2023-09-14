package khuonndph14998.fpoly.thuctapfpoly.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import khuonndph14998.fpoly.thuctapfpoly.R;


public class HomeFragment extends Fragment {
    Toolbar toolbar;
    ImageView iconCart;
    RecyclerView rclViewHome;
    ViewFlipper viewFlipper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = view.findViewById(R.id.toolbarHome);
        iconCart = view.findViewById(R.id.icon_cart);
        rclViewHome = view.findViewById(R.id.rclViewHome);
        viewFlipper = view.findViewById(R.id.viewFlipper);

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
}