package com.shariff.mealsordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ViewFoodActivity extends AppCompatActivity implements ProductAdapterAdmin.OnItemClickListener {
    private RecyclerView mRecylerView;
    private ProductAdapterAdmin mAdapter;
    private FirebaseStorage mStorageRef;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBlistener;

    private List<Upload> mUploads;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getActionBar();
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#5e9c00")));


        mRecylerView = findViewById(R.id.recycler_view);
        mRecylerView.setHasFixedSize(true);
        mRecylerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads = new ArrayList<>();
        mAdapter = new ProductAdapterAdmin(ViewFoodActivity.this,mUploads);
        mAdapter.setOnItemClickListener(ViewFoodActivity.this);
        mRecylerView.setAdapter(mAdapter);


        progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait..");

        mStorageRef = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("foods");
        progressDialog.show();

        mDBlistener = mDatabaseRef.addValueEventListener(new ValueEventListener() {

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
                Toast.makeText(ViewFoodActivity.this, "E"+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Item Clicked"+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this, "Item "+position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem = mUploads.get(position);
        String selectedKey=selectedItem.getKey();

        StorageReference imageRef=mStorageRef.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(ViewFoodActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBlistener);
    }
}