package khuonndph14998.fpoly.thuctapfpoly.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.model.Product;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.FavViewHolder>  {
    private Context mContext;
    private ArrayList<Product> productArrayList;

    private IClickListener mIClickListener;

    public interface IClickListener {
        void onClickDeleteItemFav(Product p);
    }


    public FavAdapter(Context mContext, ArrayList<Product> productArrayList, IClickListener mIClickListener) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
        this.mIClickListener = mIClickListener;
    }

    @NonNull
    @Override
    public FavAdapter.FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_fav,parent,false);
        return new FavViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.FavViewHolder holder, int position) {
        Product p = productArrayList.get(position);
        String urlString = p.getImage();
        Uri uri = Uri.parse(urlString);
        holder.nameFav.setText(p.getName());
        holder.imageFav.setImageURI(uri);
        holder.priceFav.setText(String.valueOf(p.getPrice()));
        holder.describeFav.setText(p.getDescribe());
        holder.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickListener.onClickDeleteItemFav(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class FavViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageFav;
        private TextView nameFav, priceFav, describeFav;
        private Button btnFav;

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFav = itemView.findViewById(R.id.item_fav_image);
            nameFav = itemView.findViewById(R.id.item_fav_name);
            priceFav = itemView.findViewById(R.id.item_fav_price);
            describeFav = itemView.findViewById(R.id.item_fav_describe);
            btnFav = itemView.findViewById(R.id.btnFavorite);
        }
    }
}
