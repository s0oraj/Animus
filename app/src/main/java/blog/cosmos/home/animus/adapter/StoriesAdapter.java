package blog.cosmos.home.animus.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoriesHolder> {

    @NonNull
    @Override
    public StoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


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
