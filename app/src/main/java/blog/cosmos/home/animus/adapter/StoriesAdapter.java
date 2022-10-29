package blog.cosmos.home.animus.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.model.StoriesModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoriesHolder> {

    List<StoriesModel> list;
    Activity activity;

    public StoriesAdapter(List<StoriesModel> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public StoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stories_layout,parent,false);
        return new StoriesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesHolder holder, int position) {

        if(position == 0){
            Glide.with(activity)
                    .load(activity.getResources().getDrawable(R.drawable.ic_add))
                    .into(holder.imageView);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    activity.startActivity(new Intent());
                }
            });

        }

        Glide.with(activity)
                .load(list.get(position).getVideoUrl())
                .timeout(6500)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class StoriesHolder extends RecyclerView.ViewHolder{

        public CircleImageView imageView;

        public StoriesHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }

}
