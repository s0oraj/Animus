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
import blog.cosmos.home.animus.model.HomeModel;

public class Home extends Fragment {

    HomeAdapter adapter,personalAdapter;
    ConcatAdapter concatAdapter;
    private RecyclerView recyclerView;
    private List<HomeModel> list;
    private FirebaseUser user;
    private List<HomeModel> personalList;
    private List<HomeModel> followingUsersList;
    private final MutableLiveData<Integer> commentCount = new MutableLiveData<>();


    private final MutableLiveData<Bundle> personalPostsCommentCount = new MutableLiveData<>();
    private final MutableLiveData<Bundle> followingPostsCommentCount = new MutableLiveData<>();


    Bundle followingPostsBundle;
    Bundle personalPostsBundle;



    private ImageView searchButton;

    private ImageView sendButton;


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

        followingPostsBundle = new Bundle();
        personalPostsBundle = new Bundle();

        list = new ArrayList<>();
        personalList = new ArrayList<>();
        followingUsersList = new ArrayList<>();


        adapter = new HomeAdapter(followingUsersList, getActivity());
        personalAdapter = new HomeAdapter(personalList, getActivity());
         concatAdapter = new ConcatAdapter(adapter,personalAdapter);

        recyclerView.setAdapter(concatAdapter);

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
                followingPostsCommentCount.observe((LifecycleOwner) activity, new Observer<Bundle>() {
                    @Override
                    public void onChanged(Bundle bundle) {

                            assert followingPostsCommentCount.getValue() != null;
                            if(followingPostsCommentCount.getValue().getInt(id)==0){
                                textView.setVisibility(View.GONE);
                            } else {
                                textView.setVisibility(View.VISIBLE);
                                StringBuilder builder = new StringBuilder();
                                builder.append("See all ")
                                        .append(followingPostsCommentCount.getValue().getInt(id))
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
        personalAdapter.OnPressed(new HomeAdapter.OnPressed() {
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
                personalPostsCommentCount.observe((LifecycleOwner) activity, new Observer<Bundle>() {
                    @Override
                    public void onChanged(Bundle bundle) {

                        assert personalPostsCommentCount.getValue() != null;
                        if(personalPostsCommentCount.getValue().getInt(id)==0){
                            textView.setVisibility(View.GONE);
                        } else {
                            textView.setVisibility(View.VISIBLE);
                            StringBuilder builder = new StringBuilder();
                            builder.append("See all ")
                                    .append(personalPostsCommentCount.getValue().getInt(id))
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

                DialogFragment dialogFragment=new DialogFragment();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getActivity().getSupportFragmentManager(),"My  Fragment");


            }


        });
        adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        personalAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
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
                GoogleSignInOptions gso;
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


                                    personalPostsBundle.putInt(model.getId(),map.size());
                                    personalPostsCommentCount.setValue(personalPostsBundle);

                                }

                            });


                    /*
                    List<HomeModel> tempList= new ArrayList<HomeModel>(followingUsersList);
                    tempList.addAll(personalList);
                    list = tempList;
*/
                    //Deleting repetitive posts (refer to homemodel @override equals for details)
                    Set<HomeModel> s= new HashSet<HomeModel>();
                    s.addAll(personalList);
                    personalList = new ArrayList<HomeModel>();
                    personalList.addAll(s);

                    //Sorting posts from latest to oldest
                    Collections.sort(personalList, new Comparator<HomeModel>() {
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
                    personalAdapter.addAll(personalList); //Not using notifySetDataChange method call here because this line list=templist makes list point to a different instance,
                    // therefore custom addAll() method of adapter fixes this


                }






            }
        });


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


                                                                        followingPostsBundle.putInt(model.getId(),map.size());
                                                                        followingPostsCommentCount.setValue(followingPostsBundle);

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

                                                        //Sorting posts from latest to oldest
                                                        Collections.sort(followingUsersList, new Comparator<HomeModel>() {
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
                                                        adapter.addAll(followingUsersList); //Not using notifySetDataChange method call here because this line list=templist makes list point to a different instance,
                                                        // therefore custom addAll() method of adapter fixes this


                                                    }

                                                }
                                            });

                                }

                            }


                        });

            }
        });

    }



}