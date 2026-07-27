package com.example.projectcurrrency;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatPoolActivity extends AppCompatActivity {

    RecyclerView recView;
    EditText edtMessage;
    Button btnSend;

    ArrayList<Message> messages;
    ChatAdapter adapter;

    String currentUserName = "You";

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_pool);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.myToolBar);
        setSupportActionBar(myToolbar);
        db = FirebaseFirestore.getInstance();
        currentUserName = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        recView = findViewById(R.id.recView);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);
        messages = new ArrayList<>();
        adapter = new ChatAdapter(messages, currentUserName);
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setAdapter(adapter);
        db.collection("chat_pool")
                .orderBy("timestamp")
                .addSnapshotListener((value, error) -> {
                    if (value == null) return;
                    messages.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        Message m = doc.toObject(Message.class);
                        messages.add(m);
                    }
                    adapter.notifyDataSetChanged();
                    recView.scrollToPosition(messages.size() - 1);
                });
        btnSend.setOnClickListener(v -> {
            String text = edtMessage.getText().toString();
            if (!text.isEmpty()) {
                Message msg = new Message(text, currentUserName, System.currentTimeMillis());
                db.collection("chat_pool").add(msg);
                edtMessage.setText("");
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatpoolactions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_profile){
            Intent i=new Intent(this, ProfileActivity.class);
            startActivity(i);
            return true;
        }
        if(id == R.id.action_converter){
            Intent i=new Intent(this, ConverterActivity.class);
            startActivity(i);
            return true;
        }
        if(id == R.id.action_logout){
            FirebaseAuth.getInstance().signOut();

            Intent i=new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}