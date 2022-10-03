package blog.cosmos.home.animus.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import blog.cosmos.home.animus.model.Users;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    private List<Users> list;



    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class UserHolder extends RecyclerView.ViewHolder{

        public UserHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
