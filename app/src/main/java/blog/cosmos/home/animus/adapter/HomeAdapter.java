package blog.cosmos.home.animus.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Random;

import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.model.HomeModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {

    Context context;
    OnPressed onPressed;
    private List<HomeModel> list;

    public HomeAdapter(List<HomeModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_items, parent, false);


        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        holder.userNameTv.setText(list.get(position).getName());
        holder.timeTv.setText("" + list.get(position).getTimestamp());

        List<String> likeList = list.get(position).getLikes();

        int count = likeList.size();

        if (count == 0) {
            holder.likeCountTv.setVisibility(View.INVISIBLE);

        } else if (count == 1) {
            holder.likeCountTv.setText(count + " like");

        } else {
            holder.likeCountTv.setText(count + " likes");
        }


        // check if already like

        if(likeList.contains(user.getUid())){
            holder.likeCheckBox.setChecked(true);
        } else{
            holder.likeCheckBox.setChecked(false);
        }




        holder.descriptionTv.setText(list.get(position).getDescription());
        Random random = new Random();

        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        Glide.with(context.getApplicationContext())
                .load(list.get(position).getProfileImage())
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(holder.profileImage);

        Glide.with(context.getApplicationContext())
                .load(list.get(position).getImageUrl())
                .placeholder(new ColorDrawable(color))
                .timeout(7000)
                .into(holder.imageView);

        holder.clickListener(position,
                list.get(position).getId(),
                list.get(position).getName(),
                list.get(position).getUid(),
                list.get(position).getLikes()

        );


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void OnPressed(OnPressed onPressed) {
        this.onPressed = this.onPressed;
    }

    public interface OnPressed {

        void onLiked(int position, String id, String uid, List<String> likeList, boolean isChecked);
        void onComment(int position, String id, String uid, String comment, LinearLayout commentLayout, EditText commentET);


    }

     class HomeHolder extends RecyclerView.ViewHolder {

        private CircleImageView profileImage;
        private TextView userNameTv, timeTv, likeCountTv, descriptionTv;
        private ImageView imageView;
        private CheckBox likeCheckBox;
        private ImageButton  commentBtn, shareBtn;
        private EditText commentET;
        private FloatingActionButton commentSendBtn;
        LinearLayout commentLayout;


        public HomeHolder(@NonNull View itemView) {
            super(itemView);


            profileImage = itemView.findViewById(R.id.profileImage);
            imageView = itemView.findViewById(R.id.imageView);
            userNameTv = itemView.findViewById(R.id.nameTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            likeCountTv = itemView.findViewById(R.id.likeCountTv);
            likeCheckBox = itemView.findViewById(R.id.likeBtn);
            commentBtn = itemView.findViewById(R.id.commentBtn);
            shareBtn = itemView.findViewById(R.id.shareBtn);
            descriptionTv = itemView.findViewById(R.id.descTv);
            commentET = itemView.findViewById(R.id.commentET);
            commentSendBtn = itemView.findViewById(R.id.commentSendBtn);
            commentLayout = itemView.findViewById(R.id.commentLayout);


        }

        public void clickListener(int position, final String id, String name, String uid, List<String> likes) {


            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(commentLayout.getVisibility() == View.GONE){
                        commentLayout.setVisibility(View.VISIBLE);
                    }
                }
            });


            likeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onPressed.onLiked(position, id, uid, likes, isChecked);
                }
            });

            commentSendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String comment = commentET.getText().toString();
                    onPressed.onComment(position,id,uid,comment, commentLayout, commentET);


                }
            });

        }
    }


    /**
     * Helper method which clears the existing dataset of the recyclerview adapter.
     */
    public void clear(){
        if(list!=null && !list.isEmpty()) {
            int size = list.size();
            list.clear();

            // Notify the adapter that items were removed so adapter can update the recyclerview accordingly.
            notifyItemRangeRemoved(0, size);
        }

    }


}
