package blog.cosmos.home.animus.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.ReplacerActivity;
import blog.cosmos.home.animus.adapter.HomeAdapter;
import blog.cosmos.home.animus.adapter.StoriesAdapter;
import blog.cosmos.home.animus.chat.ChatUsersActivity;
import blog.cosmos.home.animus.model.HomeModel;
import blog.cosmos.home.animus.model.StoriesModel;

public class Home extends Fragment {

    HomeAdapter adapter;
    private RecyclerView recyclerView;
    private List<HomeModel> list;
    private FirebaseUser user;
    private List<HomeModel> personalList;
    private List<HomeModel> followingUsersList;

    private final MutableLiveData<Bundle> commentCount = new MutableLiveData<>();
    Bundle commentsCountBundle;

    private ImageView searchButton;
    private ImageView sendButton;

    RecyclerView storiesRecyclerView;
    StoriesAdapter storiesAdapter;
    List<StoriesModel> storiesModelList;

    Activity activity;


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

        activity = getActivity();

        init(view);
        clickListener();

        //  reference = FirebaseFirestore.getInstance().collection("Posts").document(user.getUid());

        commentsCountBundle = new Bundle();

        list = new ArrayList<>();
        personalList = new ArrayList<>();
        followingUsersList = new ArrayList<>();
        adapter = new HomeAdapter(list, getActivity());
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
                //adapter.notifyItemChanged(position);




            }



            @Override
            public void setCommentCount(TextView textView, String id) {
                Activity activity = getActivity();
                commentCount.observe((LifecycleOwner) activity, new Observer<Bundle>() {
                    @Override
                    public void onChanged(Bundle bundle) {

                            assert commentCount.getValue() != null;
                            if(commentCount.getValue().getInt(id)==0){
                                textView.setVisibility(View.GONE);
                            } else {
                                textView.setVisibility(View.VISIBLE);
                                StringBuilder builder = new StringBuilder();
                                builder.append("See all ")
                                        .append(commentCount.getValue().getInt(id))
                                        .append(" comments");

                                textView.setText(builder); }

                            //textView.setText("See all "+ commentCount.getValue()+ " comments");


                    }
                });

            }

            @Override
            public void onCommentBtnPressed(String id, String uid, boolean isComment) {

                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("uid", uid);

                /*
                Intent intent = new Intent(getActivity(),ReplacerActivity.class);
                intent.putExtra("commentBundle",bundle);
                intent.putExtra("isComment",isComment);
                startActivity(intent);

                 */

                /*DialogFragment dialogFragment=new DialogFragment();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getActivity().getSupportFragmentManager(),"My  Fragment");

                 */


                BottomSheetDialog bottomSheet = new BottomSheetDialog();
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getActivity().getSupportFragmentManager(),
                        "ModalBottomSheet");

            }


        });

        adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        //concatAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);


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

        storiesRecyclerView = view.findViewById(R.id.storiesRecyclerView);
        storiesRecyclerView.setHasFixedSize(true);
        storiesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        storiesModelList = new ArrayList<>();
       // storiesModelList.add(new StoriesModel("","","","",""));
        storiesAdapter = new StoriesAdapter(storiesModelList, getActivity());
        storiesRecyclerView.setAdapter(storiesAdapter);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        searchButton = view.findViewById(R.id.search_bar_button);
        sendButton = view.findViewById(R.id.sendButton);

    }

    private void clickListener(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // viewPager.setCurrentItem(1);

                Intent intent = new Intent(getActivity(), ReplacerActivity.class);
                intent.putExtra("DesiredFragment","search");
                startActivity(intent);



            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*GoogleSignInOptions gso;
                GoogleSignInClient gsc;

                gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                gsc= GoogleSignIn.getClient(getActivity(),gso);

                gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        getActivity().finish();
                        startActivity(new Intent(getActivity(),ReplacerActivity.class));
                    }
                });

                */

                Intent intent = new Intent(getActivity(), ChatUsersActivity.class);
                startActivity(intent);

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

        final DocumentReference storiesReference = FirebaseFirestore.getInstance().collection("Users")
                .document(user.getUid());


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

                personalList.clear();

                for (QueryDocumentSnapshot snapshot : value) {
                    if(!snapshot.exists()){
                        return;
                    }

                    HomeModel model = snapshot.toObject(HomeModel.class);

                    System.out.println(model.getName());

                    personalList.add(new HomeModel(
                            model.getName(),
                            model.getProfileImage(),
                            model.getImageUrl(),
                            model.getUid(),
                            model.getDescription(),
                            model.getId(),
                            model.getTimestamp(),
                            model.getLikes()
                    ));

                    snapshot.getReference().collection("Comments").get()
                            .addOnCompleteListener(task -> {

                                if (task.isSuccessful()) {

                                    Map<Integer, Map<String, Object>> map = new HashMap<>();
                                    int i=0;
                                    for (QueryDocumentSnapshot commentSnapshot : task.getResult()) {
                                      //  map = commentSnapshot.getData();
                                        map.put(i,commentSnapshot.getData());
                                        i++;
                                    }


                                    commentsCountBundle.putInt(model.getId(),map.size());
                                    commentCount.setValue(commentsCountBundle);

                                }

                            });


                    //Deleting repetitive posts (refer to homemodel @override equals for details)

                    List<HomeModel> tempList = new ArrayList<>(personalList);
                    Set<HomeModel> s= new HashSet<HomeModel>();
                    s.addAll(tempList);
                    tempList = new ArrayList<HomeModel>();
                    tempList.addAll(s);

                    personalList.clear();
                    personalList.addAll(tempList);

                    list.clear();
                    tempList= new ArrayList<HomeModel>(personalList);
                    tempList.addAll(followingUsersList);
                    list.addAll(tempList);


                    //Sorting posts from latest to oldest
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
                    adapter.addAll(list); //Not using notifySetDataChange method call here because this line list=templist makes list point to a different instance,
                    // therefore custom addAll() method of adapter fixes this


                }






            }
        });


        reference.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.d("Error: ", error.getMessage());
                return;
            }
            if (value == null)
                return;
            List<String> uidList = (List<String>) value.get("following");

            if (uidList == null || uidList.isEmpty())
                return;
            // uidList.add(user.getUid());

            for(int i=0;i<uidList.toArray().length;i++){
                Log.d("TAG","uidList index: "+i+"" +uidList.get(i));
            }

            collectionReference.whereIn("uid", uidList)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.d("Error: ", error.getMessage());
                            }
                            if (value == null)
                                return;
                            //  list.clear();
                            followingUsersList.clear();

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
                                                for (QueryDocumentSnapshot snapshot : value) {


                                                    if (!snapshot.exists()) {
                                                        return;
                                                    }
                                                    HomeModel model = snapshot.toObject(HomeModel.class);

                                                    System.out.println(model.getName());
                                                    Log.d("TAG",model.getDescription());

                                                    followingUsersList.add(new HomeModel(
                                                            model.getName(),
                                                            model.getProfileImage(),
                                                            model.getImageUrl(),
                                                            model.getUid(),
                                                            model.getDescription(),
                                                            model.getId(),
                                                            model.getTimestamp(),
                                                            model.getLikes()
                                                    ));

                                                    snapshot.getReference().collection("Comments").get()
                                                            .addOnCompleteListener(task -> {

                                                                if (task.isSuccessful()) {
                                                                    Map<Integer, Map<String, Object>> map = new HashMap<>();
                                                                    int i=0;
                                                                    for (QueryDocumentSnapshot commentSnapshot : task.getResult()) {
                                                                        //  map = commentSnapshot.getData();
                                                                        map.put(i,commentSnapshot.getData());
                                                                        i++;
                                                                    }


                                                                    commentsCountBundle.putInt(model.getId(),map.size());
                                                                    commentCount.setValue(commentsCountBundle);

                                                                }

                                                            });

                                                    /*
                                                    List<HomeModel> tempList= new ArrayList<HomeModel>(personalList);;
                                                    tempList.addAll(followingUsersList);
                                                    list = tempList; */

                                                    //Deleting repetitive posts (refer to homemodel @override equals for details)

                                                    List<HomeModel> tempList = new ArrayList<>(followingUsersList);
                                                    Set<HomeModel> s= new HashSet<HomeModel>();
                                                    s.addAll(tempList);
                                                    tempList = new ArrayList<HomeModel>();
                                                    tempList.addAll(s);

                                                    followingUsersList.clear();
                                                    followingUsersList.addAll(tempList);

                                                    list.clear();
                                                    tempList= new ArrayList<HomeModel>(followingUsersList);
                                                    tempList.addAll(personalList);
                                                    list.addAll(tempList);


                                                    //Sorting posts from latest to oldest
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
                                                    adapter.addAll(list); //Not using notifySetDataChange method call here because this line list=templist makes list point to a different instance,
                                                    // therefore custom addAll() method of adapter fixes this


                                                }

                                            }
                                        });

                                snapshot.getReference().collection("Stories")
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                                if (error != null) {
                                                    Log.d("Error: ", error.getMessage());
                                                }

                                                if (value == null)
                                                    return;

                                                for(QueryDocumentSnapshot snapshot : value){

                                                    if(!value.isEmpty()){
                                                        StoriesModel model = snapshot.toObject(StoriesModel.class);
                                                        storiesModelList.add(model);
                                                    }

                                                }
                                                storiesAdapter.notifyDataSetChanged();

                                            }
                                        });

                            }

                        }


                    });




        });


        storiesReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
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

                loadStories(uidList);
            }
        });

    }


    void loadStories(List<String> followingList) {

        Query query = FirebaseFirestore.getInstance().collection("Stories");
        query.whereIn("uid", followingList).addSnapshotListener((value, error) -> {

            if (error != null) {
                Log.d("Error: ", error.getMessage());
            }

            if (value == null)
                return;

            for (QueryDocumentSnapshot snapshot : value) {

                if (!value.isEmpty()) {
                    StoriesModel model = snapshot.toObject(StoriesModel.class);
                    storiesModelList.add(model);
                }

            }
            storiesAdapter.notifyDataSetChanged();

        });

    }



}