package blog.cosmos.home.animus.fragments;

import static blog.cosmos.home.animus.MainActivity.viewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import blog.cosmos.home.animus.MainActivity;
import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.adapter.HomeAdapter;
import blog.cosmos.home.animus.model.HomeModel;

public class Home extends Fragment {

    HomeAdapter adapter;
    private RecyclerView recyclerView;
    private List<HomeModel> list;
    private FirebaseUser user;

    private ImageView searchButton;


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        init(view);
        clickListener();

        //  reference = FirebaseFirestore.getInstance().collection("Posts").document(user.getUid());


        list = new ArrayList<>();
        adapter = new HomeAdapter(list, getContext());
        recyclerView.setAdapter(adapter);

        loadDataFromFireStore();

        adapter.OnPressed(new HomeAdapter.OnPressed() {
            @Override
            public void onLiked(int position, String id, String uid, List<String> likeList, boolean isChecked) {
                DocumentReference reference = FirebaseFirestore.getInstance().collection("Users")
                        .document(uid)
                        .collection("Post Images")
                        .document(id);

                if (likeList.contains(user.getUid())) {
                    likeList.remove(user.getUid()); //unlike
                } else {
                    likeList.add(user.getUid()); // like
                }

                Map<String, Object> map = new HashMap<>();
                map.put("likes", likeList);

                reference.update(map);


            }

            @Override
            public void onComment(int position, String id, String uid, String comment, LinearLayout commentLayout, EditText commentET) {

                if (comment.isEmpty() || comment.equals(" ")) {
                    Toast.makeText(getContext(), "Can not send empty comment", Toast.LENGTH_SHORT).show();
                    return;
                }
                CollectionReference reference = FirebaseFirestore.getInstance().collection("Users")
                        .document(uid)
                        .collection("Post Images")
                        .document(id)
                        .collection("Comments");

                String commentID = reference.document().getId();

                Map<String, Object> map = new HashMap<>();
                map.put("uid", user.getUid());
                map.put("comment", comment);
                map.put("commentID", commentID);
                map.put("postID", id);

                reference.document(commentID)
                        .set(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    commentET.setText("");
                                    commentLayout.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(getContext(), "Failed to comment: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

            }
        });

    }

    public void init(View view) {

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        if (getActivity() != null)
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        if(getActivity().getWindow().getStatusBarColor()==getResources().getColor(R.color.white)){
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent2Dark));
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        searchButton = view.findViewById(R.id.search_bar_button);

    }

    private void clickListener(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewPager.setCurrentItem(1);
            }
        });
    }


    private void loadDataFromFireStore() {

        final DocumentReference reference = FirebaseFirestore.getInstance().collection("Users")
                .document(user.getUid());

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Users");

        CollectionReference personalPostReference= FirebaseFirestore.getInstance().collection("Users")
                        .document(user.getUid())
                        .collection("Post Images");


        personalPostReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
              if(error != null){
                  Log.d("Error: ", error.getMessage());
                  return;
              }
                if (value == null) {
                    return;
                }
                list.clear();
                for (QueryDocumentSnapshot snapshot : value) {
                    if(!snapshot.exists()){
                        return;
                    }

                    HomeModel model = snapshot.toObject(HomeModel.class);

                    System.out.println(model.getName());

                    list.add(new HomeModel(
                            model.getName(),
                            model.getProfileImage(),
                            model.getImageUrl(),
                            model.getUid(),
                            model.getComments(),
                            model.getDescription(),
                            model.getId(),
                            model.getTimestamp(),
                            model.getLikes()
                    ));

                }

                reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d("Error: ", error.getMessage());
                            return;
                        }
                        if (value == null)
                            return;
                        List<String> uidList = (List<String>) value.get("following");

                        if (uidList == null || uidList.isEmpty())
                            return;

                        collectionReference.whereIn("uid", uidList)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                        if (error != null) {
                                            Log.d("Error: ", error.getMessage());
                                        }
                                        if (value == null)
                                            return;
                                        for (QueryDocumentSnapshot snapshot : value) {


                                            snapshot.getReference().collection("Post Images")
                                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                            if (error != null) {
                                                                Log.d("Error: ", error.getMessage());
                                                            }

                                                            if (value == null)
                                                                return;

                                                            // we receive post data here
                                                            //list.clear();

                                                            for (QueryDocumentSnapshot snapshot : value) {


                                                                if (!snapshot.exists()) {
                                                                    return;
                                                                }
                                                                HomeModel model = snapshot.toObject(HomeModel.class);

                                                                System.out.println(model.getName());
                                                                list.add(new HomeModel(
                                                                        model.getName(),
                                                                        model.getProfileImage(),
                                                                        model.getImageUrl(),
                                                                        model.getUid(),
                                                                        model.getComments(),
                                                                        model.getDescription(),
                                                                        model.getId(),
                                                                        model.getTimestamp(),
                                                                        model.getLikes()
                                                                ));


                                                            }
                                                            Collections.sort(list, new Comparator<HomeModel>() {
                                                                @Override
                                                                public int compare(HomeModel homeModel, HomeModel t1) {


                                                                    if( t1== null || homeModel == null ||
                                                                            t1.getTimestamp() == null ||
                                                                            homeModel.getTimestamp() == null
                                                                    ){
                                                                        return 0;
                                                                    } else {
                                                                        return t1.getTimestamp().compareTo(homeModel.getTimestamp());
                                                                    }
                                                                }
                                                            });
                                                            adapter.notifyDataSetChanged();
                                                        }
                                                    });

                                        }
                                    }
                                });


                    }
                });

                Collections.sort(list, new Comparator<HomeModel>() {
                    @Override
                    public int compare(HomeModel homeModel, HomeModel t1) {

                        if( t1== null || homeModel == null ||
                            t1.getTimestamp() == null ||
                                homeModel.getTimestamp() == null
                        ){
                            return 0;
                        } else {
                            return t1.getTimestamp().compareTo(homeModel.getTimestamp());
                        }
                    }
                });
                adapter.notifyDataSetChanged();

            }
        });







    }


}