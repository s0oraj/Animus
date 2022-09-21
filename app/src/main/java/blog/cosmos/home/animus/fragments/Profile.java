package blog.cosmos.home.animus.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import blog.cosmos.home.animus.R;
import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends Fragment {

    private TextView nameTv, toolbarNameTv, statusTv, followingCountTv, followersCountTv, postCountTv;
    private CircleImageView profileImage;
    private Button followBtn;
    private RecyclerView recyclerView;




    public Profile() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);


    }

    private void init(View view){

     nameTv = view.findViewById(R.id.nameTv);
     statusTv = view.findViewById(R.id.statusTV);
     toolbarNameTv = view.findViewById(R.id.toolbarNameTV);
     followersCountTv = view.findViewById(R.id.followersCountTv);
     followingCountTv = view.findViewById(R.id.followingCountTv);
     postCountTv = view.findViewById(R.id.postCountTv);
     profileImage = view.findViewById(R.id.profileImage);
     followBtn = view.findViewById(R.id.followBtn);
     recyclerView = view.findViewById(R.id.recyclerView);


    }


}