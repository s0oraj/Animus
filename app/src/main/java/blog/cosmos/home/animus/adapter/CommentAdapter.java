package blog.cosmos.home.animus.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.model.CommentModel;
import blog.cosmos.home.animus.model.HomeModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {


    public static final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    Context context;
    List<CommentModel> list;

    public CommentAdapter(Context context, List<CommentModel> list){
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_items, parent, false);

        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {

        Glide.with(context)
                .load(list.get(position).getProfileImageUrl())
                .into(holder.profileImage);

        holder.nameTv.setText(list.get(position).getName());
        holder.commentTv.setText(list.get(position).getComment());




        Date date = list.get(position).getTimestamp();
        if(date != null){
            holder.commentTimeTv.setText(getRelativeDateTimeString(date));
        }
          else{
            holder.commentTimeTv.setText("");
        }




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

   static class CommentHolder extends RecyclerView.ViewHolder{


         CircleImageView profileImage;
         TextView nameTv, commentTv;

         TextView commentTimeTv;


        public CommentHolder(@NonNull View itemView) {
            super(itemView);


            profileImage = itemView.findViewById(R.id.profileImage);
            nameTv = itemView.findViewById(R.id.nameTV);
            commentTv = itemView.findViewById(R.id.commentTV);
            commentTimeTv = itemView.findViewById(R.id.commentTimeTV);



        }
    }

    /**
     * Updates the adapter with new data
     **/
    public void addAll(List<CommentModel> data) {
        if (data != null && !data.isEmpty()) {
            // If new data is not empty then update allPosts List
            list = data;
            //Notify the adapter for the change in dataset
            notifyDataSetChanged();
        }
    }

    public List<CommentModel> getList() {
        return list;
    }


    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy");
    private static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(" 'at' h:mm aa");
    public static String getRelativeDateTimeString(Date dateStart) {

        Calendar startDateCalendar = new GregorianCalendar();
        startDateCalendar.setTime(dateStart);

        if (startDateCalendar == null) return null;

        DateTime startDate = new DateTime(startDateCalendar.getTimeInMillis());
        DateTime today = new DateTime();
        int days = Days.daysBetween(today.withTimeAtStartOfDay(), startDate.withTimeAtStartOfDay()).getDays();


        String niceDateStr = (String) DateUtils.getRelativeTimeSpanString(dateStart.getTime() , Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS);
        String time = TIME_FORMAT.format(startDateCalendar.getTime());
        time = time.replace(" at","");
        String date;

        switch (days) {
            case -1: date = "Yesterday,";

            return date + time;

            case 0: date = "Today,";
                return niceDateStr;

            case 1: date = "Tomorrow,";
                return niceDateStr;

            default: date = DATE_FORMAT.format(startDateCalendar.getTime()); break;
        }


        String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        date = date.replace(" "+currentYear,"");


       return date + time;
    }

}
