package khuonndph14998.fpoly.thuctapfpoly.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.adapter.MessageAdapter;
import khuonndph14998.fpoly.thuctapfpoly.model.Message;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView rcv_message;
    private MessageAdapter adapter;
    private ArrayList<Message> arrayList;
    private EditText input_message;
    private ImageButton btn_message;

    private String getCurrentUserEmail() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getEmail();
        } else {
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        anhxa();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv_message.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        adapter = new MessageAdapter();
        adapter.setData(arrayList);
        rcv_message.setAdapter(adapter);
        getMessage();

        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }
    private void anhxa(){
        input_message = findViewById(R.id.input_message);
        btn_message = findViewById(R.id.btnImage_message);
        rcv_message = findViewById(R.id.rcv_message);
    }
    private void sendMessage(){
        String messageStr = input_message.getText().toString().trim();
        String userEmail = getCurrentUserEmail();
        if ( TextUtils.isEmpty(messageStr)){
            return;
        }
        if (userEmail != null) {
            String emailPath = userEmail.replace("@gmail.com", "");
            String databasePath = "/Chats/" + emailPath;
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);
            Message newMessage = new Message(messageStr);
            databaseReference.push().setValue(newMessage);
            input_message.setText("");
        }
    }
    private void getMessage () {
        String userEmail = getCurrentUserEmail();
        if (userEmail != null ){
            String emailPath = userEmail.replace("@gmail.com", "");
            String databasePath = "/Chats/" + emailPath;
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(databasePath);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    arrayList.clear();
                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Message message = messageSnapshot.getValue(Message.class);
                        if (message != null) {
                            arrayList.add(message);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    rcv_message.scrollToPosition(arrayList.size() - 1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}