package blog.cosmos.home.animus.adapter;

import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.model.NotificationModel;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {

    Context context;
    List<NotificationModel> list;

    public NotificationAdapter(Context context, List<NotificationModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_items, parent, false);
        return new NotificationHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {

        holder.notification.setText(list.get(position).getNotification());
        holder.time.setText(calculateTime(list.get(position).getTime()));

        Glide.with(context)
                .load(list.get(position).getUserPhotoUrl())
                .into(holder.notifUserImage);



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    String calculateTime(Date date) {
        long millis = date.toInstant().toEpochMilli();
        return DateUtils.getRelativeTimeSpanString(millis, System.currentTimeMillis(), 60000, DateUtils.FORMAT_ABBREV_TIME).toString();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class NotificationHolder extends RecyclerView.ViewHolder {

        TextView time, notification;
        CircleImageView notifUserImage;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.timeTv);
            notification = itemView.findViewById(R.id.notification);
            notifUserImage = itemView.findViewById(R.id.notifUserImage);


        }
    }

}