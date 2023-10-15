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

    private String getCurrentUserEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getEmail();
        } else {
            return null;
        }
    }


    public FavAdapter(Context mContext, ArrayList<Product> productArrayList) {
        this.mContext = mContext;
        this.productArrayList = productArrayList;
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
        String userEmail = getCurrentUserEmail();
        String urlString = p.getImage();
        Uri uri = Uri.parse(urlString);
        holder.nameFav.setText(p.getName());
        holder.imageFav.setImageURI(uri);
        holder.priceFav.setText(String.valueOf(p.getPrice()));
        holder.describeFav.setText(p.getDescribe());

        holder.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deletedPosition = holder.getAdapterPosition();
                if (deletedPosition != RecyclerView.NO_POSITION) {
                    String emailPath = userEmail.replace("@gmail.com", "");
                    String databasePath = "/Users/" + emailPath + "/FavProduct";
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(databasePath)
                            .child(p.getCode());
                    databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            productArrayList.remove(deletedPosition);
                            notifyItemRemoved(deletedPosition);
                            Toast.makeText(mContext, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mContext, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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
