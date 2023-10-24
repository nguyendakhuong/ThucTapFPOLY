package khuonndph14998.fpoly.thuctapfpoly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import khuonndph14998.fpoly.thuctapfpoly.databinding.ActivityMainBinding;
import khuonndph14998.fpoly.thuctapfpoly.fragment.FavoriteFragment;
import khuonndph14998.fpoly.thuctapfpoly.fragment.HomeFragment;
import khuonndph14998.fpoly.thuctapfpoly.fragment.SettingFragment;
import khuonndph14998.fpoly.thuctapfpoly.fragment.SupportFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhxa();
        setContentView(mainBinding.getRoot());
        replaceFragment(new HomeFragment());
        mainBinding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.favorite:
                    replaceFragment(new FavoriteFragment());
                    break;
                case R.id.support:
                    replaceFragment(new SupportFragment());
                    break;
                case R.id.settings:
                    replaceFragment(new SettingFragment());
                    break;
            }


            return true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    private void anhxa() {
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());

    }
}