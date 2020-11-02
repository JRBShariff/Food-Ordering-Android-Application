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
import java.util.Collections;
import java.util.List;

public class ViewMyOrder extends AppCompatActivity implements BookingAdapter.OnItemClickListener{

    private RecyclerView mRecylerView;
    private BookingAdapter mAdapter;
    private FirebaseStorage mStorageRef;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBlistener;
    private List<Booking> mUploads;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getActionBar();
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#5e9c00")));

        mRecylerView = findViewById(R.id.order_recycler_view);
        mRecylerView.setHasFixedSize(true);
        mRecylerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads = new ArrayList<>();
        mAdapter = new BookingAdapter(ViewMyOrder.this,mUploads);
        //mAdapter.setOnItemClickListener(this);
        mRecylerView.setAdapter(mAdapter);


        progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait..");

        mStorageRef = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Booking");
        progressDialog.show();

        mDBlistener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUploads.clear();
                for(DataSnapshot post : snapshot.getChildren()){
                    Booking booking = post.getValue(Booking.class);
                    mUploads.add(booking);
                    Collections.reverse(mUploads);
                }
                mAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewMyOrder.this, "E"+error.getMessage(),Toast.LENGTH_SHORT).show();
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBlistener);
    }
}