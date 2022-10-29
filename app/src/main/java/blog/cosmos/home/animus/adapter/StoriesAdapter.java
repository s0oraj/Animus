package blog.cosmos.home.animus.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.StoryAddActivity;
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
    public void onBindViewHolder(@NonNull StoriesHolder holder, @SuppressLint("RecyclerView") int position) {

        if(position == 0){
            Glide.with(activity)
                    .load(activity.getResources().getDrawable(R.drawable.ic_add))
                    .into(holder.imageView);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    activity.startActivity(new Intent(activity, StoryAddActivity.class));
                }
            });

        } else {


            Glide.with(activity)
                    .load(list.get(position).getVideoUrl())
                    .timeout(6500)
                    .into(holder.imageView);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (position == 0) {

                        Dexter.withContext(activity)
                                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .withListener(new MultiplePermissionsListener() {
                                    @Override
                                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                                        if (multiplePermissionsReport.areAllPermissionsGranted()) {

                                            activity.startActivity(new Intent(activity, StoryAddActivity.class));
                                        } else {
                                            Toast.makeText(activity, "Please allow permission from settings.", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                                        permissionToken.continuePermissionRequest();
                                    }
                                }).check();


                    } else {
                        //open story

                    }

                }
            });
        }

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
