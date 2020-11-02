package com.shariff.mealsordering.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.shariff.mealsordering.HomeActivity;
import com.shariff.mealsordering.ProductAdapter;
import com.shariff.mealsordering.R;
import com.shariff.mealsordering.Upload;
import com.shariff.mealsordering.ViewProductAcivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements ProductAdapter.OnItemClickListener {

    //view product
    private RecyclerView mRecylerView;
    private ProductAdapter mAdapter;
    private FirebaseStorage mStorageRef;
    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;
    private ProgressDialog progressDialog;


    private HomeViewModel homeViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
       ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {


            }
        });

        //database

        mRecylerView = root.findViewById(R.id.recyclerViewCustomer);
        mRecylerView.setHasFixedSize(true);
        mRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUploads = new ArrayList<>();
        mAdapter = new ProductAdapter(getContext(),mUploads);
        mRecylerView.setAdapter(mAdapter);

        progressDialog= new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait..");

        mStorageRef = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("foods");
        progressDialog.show();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUploads.clear();
                for(DataSnapshot post : snapshot.getChildren()){
                    Upload upload = post.getValue(Upload.class);
                    upload.setKey(post.getKey());
                    mUploads.add(upload);
                    Collections.reverse(mUploads);
                }
                mAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "E"+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.setOnItemClickListener(this);

        return root;

        //view prodcut



    }

    @Override
    public void onItemClick(int position) {
       Upload selectedItem = mUploads.get(position);
       String selectedKey = selectedItem.getKey();
        //Toast.makeText(getContext(), "id:"+selectedKey, Toast.LENGTH_SHORT).show();
        Intent in = new Intent(getContext(), ViewProductAcivity.class);
        in.putExtra("pr_ID",selectedKey);
        startActivity(in);

    }

    @Override
    public void onWhatEverClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {

    }
}