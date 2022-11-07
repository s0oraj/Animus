package blog.cosmos.home.animus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.model.ChatModel;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {

    Context context;
    List<ChatModel> list;


    private byte encryptionKey[] = {9, 115, 51, 86, 105, 4, -31, -23, -68, 88, 17, 20, 3, -105, 119, -53};
    private Cipher cipher, decipher;
    private SecretKeySpec secretKeySpec;

    public ChatAdapter(Context context, List<ChatModel> list, Cipher cipher, Cipher decipher, SecretKeySpec secretKeySpec) {
        this.context = context;
        this.list = list;

        // Variables used for AES decription
        this.cipher = cipher;
        this.decipher = decipher;
        this.secretKeySpec = secretKeySpec;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_items, parent, false);
        return new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        if(list.get(position).getSenderID().equalsIgnoreCase(user.getUid())){
            holder.leftChat.setVisibility(View.GONE);
            holder.rightChat.setVisibility(View.VISIBLE);
            try {
                holder.rightChat.setText(AESDecryptionMethod(list.get(position).getMessage()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }else{
            holder.rightChat.setVisibility(View.GONE);
            holder.leftChat.setVisibility(View.VISIBLE);
            try {
                holder.leftChat.setText(AESDecryptionMethod(list.get(position).getMessage()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ChatHolder extends  RecyclerView.ViewHolder{

        TextView leftChat, rightChat;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            leftChat = itemView.findViewById(R.id.leftChat);
            rightChat = itemView.findViewById(R.id.rightChat);

        }
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