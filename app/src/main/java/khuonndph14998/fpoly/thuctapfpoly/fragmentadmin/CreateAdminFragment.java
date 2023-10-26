package khuonndph14998.fpoly.thuctapfpoly.fragmentadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.admin.CreateAdminActivity;

public class CreateAdminFragment extends Fragment {
    private TextInputEditText inputAdmin_search;
    private ImageView icon_admin_search,icon_admin_add;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_create_admin, container, false);
        inputAdmin_search = view.findViewById(R.id.input_admin_search);
        icon_admin_search = view.findViewById(R.id.icon_admin_search);
        icon_admin_add = view.findViewById(R.id.icon_admin_add);

        icon_admin_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CreateAdminActivity.class);
                startActivity(i);
            }
        });
        return view;
    }
}