package blog.cosmos.home.animus.fragments;


import android.Manifest;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
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


    Uri imageUri;
    String imageURL;

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

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setHasFixedSize(true);

        list = new ArrayList<>();
        adapter = new GalleryAdapter(list);

        recyclerView.setAdapter(adapter);

        clickListener();

    }


    private void init(View view) {
        descET = view.findViewById(R.id.descriptionET);
        imageView = view.findViewById(R.id.imageView);
        recyclerView = view.findViewById(R.id.recyclerView);
        backBtn = view.findViewById(R.id.backBtn);
        nextBtn = view.findViewById(R.id.nextBtn);

    }

    private void clickListener(){

        adapter.SendImage(new GalleryAdapter.SendImage() {
            @Override
            public void onSend(Uri picUri) {

                imageUri = picUri;

                Glide.with(getContext())
                                .load(picUri)
                                        .into(imageView);

                imageView.setVisibility(View.VISIBLE);

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadData();


            }
        });

    }


    private void uploadData(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("");
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dexter.withContext(getContext())
                        .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {

                                if (report.areAllPermissionsGranted()) {
                                    File file = new File(Environment.getExternalStorageDirectory().toString() + "/Download");
                                    if (file.exists()) {
                                        File[] files = file.listFiles();

                                        assert files != null;
                                        for (File file1 : files) {
                                            if (file1.getAbsolutePath().endsWith(".jpg") || file1.getAbsolutePath().endsWith(".png")) {
                                                list.add(new GalleryImages(Uri.fromFile(file1)));
                                                adapter.notifyDataSetChanged();
                                            }
                                        }

                                    }

                                }

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                            }
                        }).check();
            }
        });


    }
}