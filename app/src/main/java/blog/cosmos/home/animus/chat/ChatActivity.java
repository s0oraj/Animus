package blog.cosmos.home.animus.chat;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.adapter.ChatAdapter;
import blog.cosmos.home.animus.model.ChatModel;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    FirebaseUser user;
    CircleImageView imageView;
    TextView name, status;
    EditText chatET;
    ImageView sendBtn;
    RecyclerView recyclerView;

    ChatAdapter adapter;
    List<ChatModel> list;

    String chatID;


    private String stringMessage;
    private byte encryptionKey[] = {9, 115, 51, 86, 105, 4, -31, -23, -68, 88, 17, 20, 3, -105, 119, -53};
    private Cipher cipher, decipher;
    private SecretKeySpec secretKeySpec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        try{

            try {
                cipher = Cipher.getInstance("AES");
                decipher = Cipher.getInstance("AES");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            }

            secretKeySpec = new SecretKeySpec(encryptionKey, "AES");

        }
        catch (Exception e){
            e.printStackTrace();
        }
        Log.d("TAG", AESEncryptionMethod("Hello world!"));
        try {
            Log.d("TAG", AESDecryptionMethod(AESEncryptionMethod("Hello world!")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        init();

        loadUserData();

        loadMessages();

        sendBtn.setOnClickListener(v -> {

            // Encrypting of data which is taken from chat edit text as input.
            String message = AESEncryptionMethod(chatET.getText().toString().trim());

            if (message.isEmpty()) {
                return;
            }

            CollectionReference reference = FirebaseFirestore.getInstance().collection("Messages");


            Map<String, Object> map = new HashMap<>();

            map.put("lastMessage", message);
            map.put("time", FieldValue.serverTimestamp());


            reference.document(chatID).update(map);

            String messageID = reference
                    .document(chatID)
                    .collection("Messages")
                    .document()
                    .getId();

            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("id", messageID);
            messageMap.put("message", message);
            messageMap.put("senderID", user.getUid());
            messageMap.put("time", FieldValue.serverTimestamp());


            reference.document(chatID).collection("Messages").document(messageID).set(messageMap)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            chatET.setText("");
                        } else {
                            Toast.makeText(ChatActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                        }
                    });

        });

    }

    void init() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        imageView = findViewById(R.id.profileImage);
        name = findViewById(R.id.nameTV);
        status = findViewById(R.id.statusTV);
        chatET = findViewById(R.id.chatET);
        sendBtn = findViewById(R.id.sendBtn);

        recyclerView = findViewById(R.id.recyclerView);

        list = new ArrayList<>();
        adapter = new ChatAdapter(this, list, cipher, decipher,secretKeySpec);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    void loadUserData() {

        String oppositeUID = getIntent().getStringExtra("uid");

        FirebaseFirestore.getInstance().collection("Users").document(oppositeUID)
                .addSnapshotListener((value, error) -> {

                    if (error != null)
                        return;

                    if (value == null)
                        return;


                    if (!value.exists())
                        return;

                    //
                    boolean isOnline = Boolean.TRUE.equals(value.getBoolean("online"));
                    status.setText(isOnline ? "Online" : "Offline");

                    Glide.with(getApplicationContext()).load(value.getString("profileImage")).into(imageView);

                    name.setText(value.getString("name"));


                });

    }

    void loadMessages() {

        chatID = getIntent().getStringExtra("id");


        CollectionReference reference = FirebaseFirestore.getInstance()
                .collection("Messages")
                .document(chatID)
                .collection("Messages");

        reference
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {


                    if (error != null) return;

                    if (value == null || value.isEmpty()) return;

                    list.clear();

                    for (QueryDocumentSnapshot snapshot : value) {
                        ChatModel model = snapshot.toObject(ChatModel.class);
                        list.add(model);

                    }
                    adapter.notifyDataSetChanged();

                });

    }

    private String AESEncryptionMethod(String string){

        byte[] stringByte = string.getBytes();
        byte[] encryptedByte = new byte[stringByte.length];

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encryptedByte = cipher.doFinal(stringByte);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        String returnString = null;

        try {
            returnString = new String(encryptedByte, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return returnString;
    }

    private String AESDecryptionMethod(String string) throws UnsupportedEncodingException {
        byte[] EncryptedByte = string.getBytes("ISO-8859-1");
        String decryptedString = string;

        byte[] decryption;

        try {
            decipher.init(cipher.DECRYPT_MODE, secretKeySpec);
            decryption = decipher.doFinal(EncryptedByte);
            decryptedString = new String(decryption);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return decryptedString;
    }



}