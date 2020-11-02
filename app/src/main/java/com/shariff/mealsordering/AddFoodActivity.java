package com.shariff.mealsordering;



import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AddFoodActivity extends AppCompatActivity {
    private static final int PIC_REQUEST_IMG = 1;
    private Button mButtonCHooseImage;
    private Button mButtonUpload;
    private EditText mFilename;
    private EditText mPrice;
    private EditText mCategory;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        getSupportActionBar().hide();



        mButtonCHooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mFilename = findViewById(R.id.etFile_name);
        mPrice = findViewById(R.id.et_price);
        mCategory = findViewById(R.id.et_cat);

        mImageView = findViewById(R.id.imageView2);

        mProgressBar = findViewById(R.id.progressBar);
        mStorageRef = FirebaseStorage.getInstance().getReference("foods");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("foods");


        //Chosing an Image from Gallery
        mButtonCHooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    openFileChooser();
                }catch (Exception e){
                    Toast.makeText(AddFoodActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }

            }
        });
        //Uploading an Image
        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask !=null && mUploadTask.isInProgress()){
                    Toast.makeText(AddFoodActivity.this, "Upload in Progress", Toast.LENGTH_LONG).show();
                }else {
                    try{
                        uploadFile();
                    }catch (Exception e){
                        Toast.makeText(AddFoodActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PIC_REQUEST_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PIC_REQUEST_IMG &&resultCode==
                RESULT_OK && data!=null && data.getData()!=null){
            mImageUri = data.getData();
            Glide.with(this).load(mImageUri).into(mImageView);
            //You Can Also Use
            //mImageView.setImageURI(mImageUri);
        }
    }


    // Getting an extension from our file (eg. Jpeg,Png,Jpg etc)
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        if (mImageUri !=null){
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    +"."+getFileExtension(mImageUri));

            mUploadTask =fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                //Success
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    /*Thread delay = new Thread(){
                        @Override
                        public void run() {
                            try {
                                sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };*/
                    //delay.start();

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final Uri url=uri;
                            Toast.makeText(AddFoodActivity.this, "Upload Successful", Toast.LENGTH_LONG).show();
                            Upload upload = new Upload(mFilename.getText().toString()
                                    .trim(),mPrice.getText().toString(),mCategory.getText().toString(),url.toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(System.currentTimeMillis()+"").setValue(upload);
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                //Failure
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddFoodActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                //Updating the Progress Bar
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    mProgressBar.setProgress((int) progress);
                }
            });

        }else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }


}

/*
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

//import com.squareup.picasso.Picasso;

public class AddFoodActivity extends AppCompatActivity {

    private static final int PIC_REQUEST_IMG = 1;
    private Button mButtonCHooseImage;
    private Button mButtonUpload;
    private EditText mFilename;
    private EditText mPrice;
    private EditText mCategory;
    private ImageView mImageView;

    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);


        mButtonCHooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mFilename = findViewById(R.id.etFile_name);
        mPrice = findViewById(R.id.et_price);
        mCategory = findViewById(R.id.et_cat);

        mImageView = findViewById(R.id.imageView2);
        try {
            mStorageRef = FirebaseStorage.getInstance().getReference("foods");
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("foods");
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }


        mButtonCHooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 OpenFileChoose();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask!=null && mUploadTask.isInProgress()){
                    Toast.makeText(AddFoodActivity.this, "Upload in Progress", Toast.LENGTH_LONG).show();
                }else {
                    UploadData();
                }
            }
        });


    }


    private void OpenFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PIC_REQUEST_IMG);
    }

    private String getFileExtension(Uri uri) {
        try {
            ContentResolver cR = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(cR.getType(uri));
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
            return "";
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PIC_REQUEST_IMG && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Glide.with(this).load(mImageUri).into(mImageView);
            //Picasso.get().load(mImageUri).into(mImageView);
           // mImageView.setImageURI(mImageUri);
        }
    }

   private void UploadData() {
       try {
           if (mImageUri != null) {
               StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
               mUploadTask= fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       Toast.makeText(AddFoodActivity.this, "Success Upload!", Toast.LENGTH_SHORT).show();
                        Upload upload = new Upload(mFilename.getText().toString(),mPrice.getText().toString(),mCategory.getText().toString(),taskSnapshot.getStorage().getDownloadUrl().toString());
                       //Upload upload = new Upload(mFilename.getText().toString(),mPrice.getText().toString(),taskSnapshot.getStorage().getDownloadUrl().toString());
                       String uploadID = mDatabaseRef.push().getKey();
                       mDatabaseRef.child(uploadID).setValue(upload);
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(AddFoodActivity.this, "erro:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                   }
               }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                       Toast.makeText(AddFoodActivity.this, "Loading...", Toast.LENGTH_LONG).show();
                   }
               });

           } else {
               Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
           }
       }catch (Exception ex) {
           Toast.makeText(AddFoodActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
       }
   }


}

 */