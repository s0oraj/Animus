package blog.cosmos.home.animus.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.model.GalleryImages;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryHolder>{

    private List<GalleryImages> list;

    public GalleryAdapter(List<GalleryImages> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public GalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_items,parent,false);
        return new GalleryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryHolder holder, int position) {

        holder.imageView.setImageURI(list.get(position).getPicUri());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class GalleryHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public GalleryHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }


}
