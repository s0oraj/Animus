package blog.cosmos.home.animus.fragments;

import static blog.cosmos.home.animus.utils.ImageContent.loadSavedImages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import blog.cosmos.home.animus.R;
import blog.cosmos.home.animus.adapter.GalleryAdapter;
import blog.cosmos.home.animus.model.GalleryImages;


public class Add extends Fragment {

    private EditText descET;
    private ImageView imageView;
    private RecyclerView recyclerView;

    private ImageButton backBtn, nextBtn;

    private List<GalleryImages> list;
    private GalleryAdapter adapter;
    public Add() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setHasFixedSize(true);

        list = new ArrayList<>();
        adapter = new GalleryAdapter(list);

        recyclerView.setAdapter(adapter);

    }


    private void init(View view){
        descET = view.findViewById(R.id.descriptionET);
        imageView = view.findViewById(R.id.imageView);
        recyclerView = view.findViewById(R.id.recyclerView);
        backBtn = view.findViewById(R.id.backBtn);
        nextBtn = view.findViewById(R.id.nextBtn);

    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadSavedImages(getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));

                adapter.notifyDataSetChanged();
            }
        });


    }
}