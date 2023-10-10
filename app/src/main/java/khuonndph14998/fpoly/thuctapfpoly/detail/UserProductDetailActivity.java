package khuonndph14998.fpoly.thuctapfpoly.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import khuonndph14998.fpoly.thuctapfpoly.R;

public class UserProductDetailActivity extends AppCompatActivity {
    private TextView detailName, detailDescribe, detailNote, detailQuantity, detailCategory,detailPrice,like,dislike;
    private ImageView detailImage,imgLike,imgDislike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product_detail);
        anhxa();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String urlString = extras.getString("image");
            Uri uri = Uri.parse(urlString);
            detailName.setText(extras.getString("name"));
            detailDescribe.setText(extras.getString("describe"));
            detailNote.setText(extras.getString("note"));
            detailQuantity.setText(String.valueOf(extras.getInt("quantity")));
            detailPrice.setText(String.valueOf(extras.getInt("price")));
            detailCategory.setText(extras.getString("selectedItem"));
            detailImage.setImageURI(uri);
        }


    }

    private void anhxa() {
        detailName = findViewById(R.id.detail_user_name);
        detailDescribe = findViewById(R.id.detail_user_describe);
        detailNote = findViewById(R.id.detail_user_note);
        detailQuantity = findViewById(R.id.detail_user_quantity);
        detailCategory = findViewById(R.id.detail_user_category);
        detailPrice = findViewById(R.id.detail_user_price);
        like = findViewById(R.id.detail_user_like_text);
        dislike = findViewById(R.id.detail_user_dislike_text);
        detailImage = findViewById(R.id.detail_user_image);
        imgLike = findViewById(R.id.detail_user_like);
        imgDislike = findViewById(R.id.detail_user_dislike);
    }
}