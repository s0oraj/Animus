package blog.cosmos.home.animus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.adapter.NotificationAdapter;
import blog.cosmos.home.animus.model.NotificationModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Notification extends Fragment {

    RecyclerView recyclerView;
    NotificationAdapter adapter;
    List<NotificationModel> list;
    FirebaseUser user;

    public Notification() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        loadNotification();
    }

    void init(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        adapter = new NotificationAdapter(getContext(), list);

        recyclerView.setAdapter(adapter);

        user = FirebaseAuth.getInstance().getCurrentUser();

    }

    void loadNotification() {

        CollectionReference reference = FirebaseFirestore.getInstance().collection("Notifications");

        reference.whereEqualTo("uid", user.getUid())
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {

                    if (error != null)
                        return;


                    if (value.isEmpty()) return;


                    list.clear();


                    /*list.add(new NotificationModel("18 mins ago","Suraj Singh followed you", "https://lh3.googleusercontent.com/a/ALm5wu31ZmZCly7VGX9PePlIhni7ik9rHj-nZXP7H9xYPQ=s96-c",new Date()));


                    list.add(new NotificationModel("3 hours ago","Zack liked your photo", "https://cdnb.artstation.com/p/assets/images/images/009/836/467/large/maria-bo-schatzis-stream-profilpicture.jpg?1521139318",new Date()));

                    list.add(new NotificationModel("8 hours ago","Nick followed you", "https://i.pinimg.com/564x/0e/c8/27/0ec82719173a0ed176c6e66f7056d488.jpg",new Date()));

                    list.add(new NotificationModel("Oct 29, 11:00 am","Sophia liked your photo", "https://avatarfiles.alphacoders.com/123/123888.png",new Date()));
                    list.add(new NotificationModel("Oct 28, 12:45pm","Manish followed you", "https://i.pinimg.com/736x/a6/89/f7/a689f7de53d8a66c89c460893059bd52.jpg",new Date()));

                    list.add(new NotificationModel("Oct 26, 06:00 pm","Jasmine commented on your photo", "https://www.stylevore.com/wp-content/uploads/2020/01/5a3cc6a963a50cd65b9f399452d66efb.jpg",new Date()));


                    list.add(new NotificationModel("Oct 26, 03:00 am","Jules Smith liked your comment", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRvpWY5rhiL5TBMi2BM8Xqd0VdA2smtkK3klrYB1Dc9pxD5-gbKfH8HVc7mkX6a0hu_mVM&usqp=CAU",new Date()));

                    list.add(new NotificationModel("Oct 25, 11:00 am","Priyanka Singh followed you", "https://images2.alphacoders.com/702/thumb-350-702751.png",new Date()));

                    list.add(new NotificationModel("Oct 22, 10:58 pm","Anuj Bandral liked your photo", "https://wallpapercave.com/wp/wp3875564.png",new Date()));

                    list.add(new NotificationModel("Oct 19, 05:37 am","Nidhi commented on your photo", "https://avatarfiles.alphacoders.com/323/323377.jpg",new Date()));

                    list.add(new NotificationModel("Oct 18, 8:20 pm","Manish followed you", "https://i.pinimg.com/736x/a6/89/f7/a689f7de53d8a66c89c460893059bd52.jpg",new Date()));

*/



                   for (QueryDocumentSnapshot snapshot : value) {

                        NotificationModel model = snapshot.toObject(NotificationModel.class);
                        list.add(model);

                    }






                    adapter.notifyDataSetChanged();


                });

    }


}