package khuonndph14998.fpoly.thuctapfpoly.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import khuonndph14998.fpoly.thuctapfpoly.R;
import khuonndph14998.fpoly.thuctapfpoly.adapter.MessageAdapter;
import khuonndph14998.fpoly.thuctapfpoly.model.Message;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView rcv_message;
    private MessageAdapter adapter;
    private List<Message> messageList;
    private EditText input_message;
    private ImageButton btn_message;
    private DatabaseReference messagesRef;
    private ChildEventListener messageChildEventListener;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

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
        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList);
        rcv_message.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rcv_message.setLayoutManager(linearLayoutManager);

        String userEmail = getCurrentUserEmail();
        String emailPath = userEmail.replace("@gmail.com", "");
        String databasePath = "/Messages/" + emailPath;
        messagesRef = FirebaseDatabase.getInstance().getReference().child(databasePath);
        attachMessageListener();

        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageStr = input_message.getText().toString().trim();
                if (!TextUtils.isEmpty(messageStr)) {
                    addToChat(messageStr, Message.SENT_BY_ME);
                    input_message.setText("");
                    callAPI(messageStr);
                }
            }
        });
    }

    private void anhxa() {
        input_message = findViewById(R.id.input_message);
        btn_message = findViewById(R.id.btnImage_message);
        rcv_message = findViewById(R.id.rcv_message);
    }

    void addToChat(String message, String sentBy) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                messageList.add(new Message(message, sentBy));
                adapter.notifyDataSetChanged();
                rcv_message.smoothScrollToPosition(adapter.getItemCount());

                saveMessageToFirebase(message, sentBy);
            }
        });
    }

    void addResponse(String message) {
        messageList.remove(messageList.size() - 1);
        addToChat(message, Message.SENT_BY_BOT);
    }

    void callAPI(String message) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "gpt-3.5-turbo-instruct");
            jsonBody.put("prompt", message);
            jsonBody.put("max_tokens", 4000);
            jsonBody.put("temperature", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer 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")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    addResponse("Failed to load response due to " + response.body().string());
                }
            }
        });
    }

    void saveMessageToFirebase(String message, String sentBy) {
        String messageId = messagesRef.push().getKey();
        if (messageId != null) {
            Message newMessage = new Message(message, sentBy);
            messagesRef.child(messageId).setValue(newMessage);
        }
    }

    private void attachMessageListener() {
        messageChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                if (message != null) {
                    messageList.add(message);
                    adapter.notifyDataSetChanged();
                    rcv_message.smoothScrollToPosition(adapter.getItemCount());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        messagesRef.addChildEventListener(messageChildEventListener);
    }
}