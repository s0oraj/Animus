package blog.cosmos.home.animus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.model.CommentModel;
import blog.cosmos.home.animus.model.HomeModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {


    Context context;
    List<CommentModel> list;

    public CommentAdapter(Context context, List<CommentModel> list){
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_items, parent, false);

        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {

        Glide.with(context)
                .load(list.get(position).getProfileImageUrl())
                .into(holder.profileImage);

        holder.nameTv.setText(list.get(position).getName());
        holder.commentTv.setText(list.get(position).getComment());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

   static class CommentHolder extends RecyclerView.ViewHolder{


         CircleImageView profileImage;
         TextView nameTv, commentTv;



        public CommentHolder(@NonNull View itemView) {
            super(itemView);


            profileImage = itemView.findViewById(R.id.profileImage);
            nameTv = itemView.findViewById(R.id.nameTV);
            commentTv = itemView.findViewById(R.id.commentTV);



        }
    }

    /**
     * Updates the adapter with new data
     **/
    public void addAll(List<CommentModel> data) {
        if (data != null && !data.isEmpty()) {
            // If new data is not empty then update allPosts List
            list = data;
            //Notify the adapter for the change in dataset
            notifyDataSetChanged();
        }
    }

    public List<CommentModel> getList() {
        return list;
    }

}
