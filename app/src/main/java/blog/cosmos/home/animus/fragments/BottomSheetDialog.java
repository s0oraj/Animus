package blog.cosmos.home.animus.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
import blog.cosmos.home.animus.adapter.CommentAdapter;
import blog.cosmos.home.animus.model.CommentModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    RelativeLayout rootLayout;
    View viewReference;
    float rootLayoutY=0;
    private float oldY = 0;
    private float baseLayoutPosition = 0;
    private float defaultViewHeight = 0;
    private boolean isScrollingUp = false;
    private boolean isScrollingDown = false;



    RecyclerView recyclerView;
    Activity activity;
    EditText commentEt;
    ImageButton sendBtn;
    ImageView dialogBackButton;
    FirebaseUser user;
    String id,uid;
    CollectionReference reference;

    CircleImageView userProfileImage;
    LinearLayout commentLayout;


    private EditText userMsgEdt;
    private  boolean isScrollToLastRequired = false;
    CommentAdapter commentAdapter;
    List<CommentModel> list;

    public BottomSheetDialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.bottom_sheet_layout_2,
                container, false);
        userMsgEdt = view.findViewById(R.id.commentET);

     /*   getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                
      */
        view.setBackgroundResource(R.drawable.rounded_dialog);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

    }

    @Override
    public void onViewCreated(@NonNull View view1, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view1, savedInstanceState);

        commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCommentDialog bottomSheet = new AddCommentDialog();

                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("uid", uid);

                bottomSheet.setArguments(bundle);
                bottomSheet.show(getActivity().getSupportFragmentManager(),
                        "ModalBottomSheet");
            }
        });

        init(view1);
        clickListener(view1);

        reference = FirebaseFirestore.getInstance().collection("Users")
                .document(uid)
                .collection("Post Images")
                .document(id)
                .collection("Comments");

        loadCommentData();


    }

    private void init(View view) {


        commentLayout = view.findViewById(R.id.comment_section);
        activity= getActivity();

        commentEt = view.findViewById(R.id.commentET);
        sendBtn = view.findViewById(R.id.sendBtn);
        dialogBackButton = view.findViewById(R.id.dialogBackBtn);

        user= FirebaseAuth.getInstance().getCurrentUser();


        userProfileImage =  view.findViewById(R.id.user_image);


        Glide.with(getActivity())
                .load(user.getPhotoUrl())
                .placeholder(R.drawable.ic_person)
                .into(userProfileImage);

        viewReference= view;
        rootLayout = view.findViewById(R.id.linearDialogLayout);
        recyclerView = view.findViewById(R.id.commentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list= new ArrayList<>();
        commentAdapter = new CommentAdapter(getContext(), list);
        recyclerView.setAdapter(commentAdapter);
        userMsgEdt = view.findViewById(R.id.commentET);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                v.onTouchEvent(event);
                return true;
            }
        });


        if(getArguments() == null){
            return;
        }

        id = getArguments().getString("id");
        uid = getArguments().getString("uid");




    }

    private void clickListener(View view1) {
        dialogBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });




        setUpKeyboard();

       /* userMsgEdt.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(userMsgEdt, 0);
            }
        }, 300);
        userMsgEdt.requestFocus();

        */

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                String comment = commentEt.getText().toString();
                commentEt.setText("");

                if (comment.isEmpty() || comment.equals(" ")) {
                    Toast.makeText(getContext(), "Can not send empty comment", Toast.LENGTH_SHORT).show();
                    return;
                }


                String commentID = reference.document().getId();

                Map<String, Object> map = new HashMap<>();
                map.put("uid", user.getUid());
                map.put("comment", comment);
                map.put("commentID", commentID);
                map.put("postID", id);
                map.put("name", user.getDisplayName());
                map.put("profileImageUrl", user.getPhotoUrl().toString());
                map.put("timestamp", FieldValue.serverTimestamp());


                reference.document(commentID)
                        .set(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    commentEt.setText("");




                                } else {
                                    commentEt.setText(comment);
                                    Toast.makeText(getContext(), "Failed to comment: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        });





            }
        });



    }

    private void loadCommentData() {
        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    return;
                }

                if(value == null){
                    return;
                }

                list.clear();

                for(DocumentSnapshot snapshot: value){
                    CommentModel model = snapshot.toObject(CommentModel.class);

                    list.add(model);
                    Set<CommentModel> s= new HashSet<CommentModel>();
                    s.addAll(list);
                    list = new ArrayList<CommentModel>();
                    list.addAll(s);

                    boolean areAllTimeStampsNotNull= true;
                    for(int i =0; i<list.size(); i++){
                        if(list.get(i).getTimestamp() == null){
                            areAllTimeStampsNotNull = false;
                            break;
                        }
                    }

                    if(areAllTimeStampsNotNull){
                        //Sorting comments from latest to oldest
                        Collections.sort(list, new Comparator<CommentModel>() {
                            @Override
                            public int compare(CommentModel commentModel, CommentModel t1) {

                                if( t1== null || commentModel == null ||
                                        t1.getTimestamp() == null ||
                                        commentModel.getTimestamp() == null
                                ){
                                    return 0;
                                } else {
                                    return t1.getTimestamp().compareTo(commentModel.getTimestamp());
                                }
                            }
                        });
                    }


                }
                commentAdapter.addAll(list);
                //Not using notifySetDataChange method call here because this line list=templist makes list point to a different instance,
                // therefore custom addAll() method of adapter fixes this










            }
        });
    }

    private void setUpKeyboard(){
      /*  //Setup editText behavior for opening soft keyboard
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

        userMsgEdt.setOnTouchListener((view, motionEvent) -> {
            InputMethodManager keyboard = (InputMethodManager) getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (keyboard != null) {
                isScrollToLastRequired = linearLayoutManager.findLastVisibleItemPosition() == commentAdapter.getItemCount() - 1;
                keyboard.showSoftInput(viewReference.findViewById(R.id.sendBtn), InputMethodManager.SHOW_FORCED);
            }
            return false;
        });
        //Executes recycler view scroll if required.
        recyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom && isScrollToLastRequired) {
                recyclerView.postDelayed(() -> recyclerView.scrollToPosition(
                        recyclerView.getAdapter().getItemCount() - 1), 100);
            }
        });

       */
    }

    @Override
    public int getTheme() {
        return R.style.NoBackgroundDialogTheme;

    }

    @Override
    public void onResume() {
        super.onResume();
        setUpKeyboard();
    }
}