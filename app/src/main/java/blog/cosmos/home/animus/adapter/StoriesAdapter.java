package blog.cosmos.home.animus.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.model.StoriesModel;

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

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class StoriesHolder extends RecyclerView.ViewHolder{

        public StoriesHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
