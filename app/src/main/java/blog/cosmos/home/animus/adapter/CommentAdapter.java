package blog.cosmos.home.animus.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import blog.cosmos.home.animus.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_items, parent, false);

        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class CommentHolder extends RecyclerView.ViewHolder{

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
