package khuonndph14998.fpoly.thuctapfpoly.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import khuonndph14998.fpoly.thuctapfpoly.R;

public class AdminProductDetailActivity extends AppCompatActivity {
    private TextView detailName,detailDescribe,detailCode,detailnote,detailquantity,dentailCategory;
    private ImageView detailImage;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_detail);
        anhxa();
        Bundle b = getIntent().getExtras();
        if (b != null){
            String urlString = b.getString("image");
            Uri uri = Uri.parse(urlString);
            detailName.setText(b.getString("name"));
            detailDescribe.setText(b.getString("describe"));
            detailCode.setText(b.getString("code"));
            detailnote.setText(b.getString("note"));
            detailquantity.setText(String.valueOf(b.getInt("quantity")));
            dentailCategory.setText(b.getString("selectedItem"));
            detailImage.setImageURI(uri);
        }

    }

    private void anhxa() {
        detailName = findViewById(R.id.detail_admin_name);
        detailnote = findViewById(R.id.detail_admin_note);
        detailCode = findViewById(R.id.detail_admin_code);
        detailquantity = findViewById(R.id.detail_admin_quantity);
        detailDescribe = findViewById(R.id.detail_admin_describe);
        dentailCategory = findViewById(R.id.detail_admin_category);
        detailImage = findViewById(R.id.detail_admin_image);
        btnUpdate = findViewById(R.id.detail_admin_update);
    }
}