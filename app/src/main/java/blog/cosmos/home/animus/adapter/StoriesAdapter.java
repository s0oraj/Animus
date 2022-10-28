package blog.cosmos.home.animus.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import blog.cosmos.home.animus.R;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoriesHolder> {

    @NonNull
    @Override
    public StoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stories_layout,parent,false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class StoriesHolder extends RecyclerView.ViewHolder{

        public StoriesHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
