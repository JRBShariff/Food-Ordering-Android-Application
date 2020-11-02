package com.shariff.mealsordering;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
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

public class ViewProductAcivity extends AppCompatActivity {

    private TextView mName,mPrice,mCategory;
    private String productID;
    private ImageView mImageView;
    private FirebaseStorage mStorageRef;
    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_acivity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getActionBar();

        mName =findViewById(R.id.tv_name);
        mCategory = findViewById(R.id.tv_cat);
        mPrice = findViewById(R.id.tv_price);
        mImageView = findViewById(R.id.imageView);

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#5e9c00")));

        //fetched id
        productID = getIntent().getStringExtra("pr_ID");
        //db

        mUploads = new ArrayList<>();
        mStorageRef=FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("foods").child(productID);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Upload post = snapshot.getValue(Upload.class);
                mName.setText(post.getName());
                mPrice.setText(post.getPrice());
                mCategory.setText(post.getCategory());
                String url=post.getImageUrl();
                Glide.with(ViewProductAcivity.this).load(url).into(mImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewProductAcivity.this, "err: "+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public void BookingForm(View v){
        Intent in = new Intent(this, BookingActivity.class);
        in.putExtra("pr_ID",productID);
        startActivity(in);
    }


}