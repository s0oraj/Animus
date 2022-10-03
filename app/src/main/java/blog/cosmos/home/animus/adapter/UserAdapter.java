package blog.cosmos.home.animus.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.model.Users;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    private List<Users> list;

    public UserAdapter(List<Users> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_items, parent, false);



        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserHolder extends RecyclerView.ViewHolder{

        public UserHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
