package com.shariff.mealsordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Intent.*;

public class BookingActivity extends AppCompatActivity {

    private EditText mAddress,mPhone,mQuantity;
    private FirebaseStorage mStorageRef;
    private DatabaseReference mDatabaseRef;
    private List<Booking> mUploads;
    private Button btnSave;
    private String productID;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        mAddress = findViewById(R.id.address);
        mPhone = findViewById(R.id.phone);
        mQuantity = findViewById(R.id.quantity);
        btnSave = findViewById(R.id.btn_book);


        productID= getIntent().getStringExtra("pr_ID");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        String e_mail=firebaseUser.getEmail();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());//date


        mUploads = new ArrayList<>();
        mStorageRef=FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Booking");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(mPhone.getText().toString().equals("")){
                        mPhone.setError("Please FIll");
                    }else if(mAddress.getText().toString().equals("")){
                        mAddress.setError("Please FIll");
                    }else if(mQuantity.getText().toString().equals("")){
                        mQuantity.setError("Please FIll");
                    }else {
                        //info.put(System.currentTimeMillis() + "", new Booking(e_mail, productID, date, mPhone.getText().toString(), mAddress.getText().toString(), mQuantity.getText().toString()));
                        Booking book= new Booking(e_mail, productID, date, mPhone.getText().toString(), mAddress.getText().toString(), mQuantity.getText().toString(),"Pending");
                        String uploadId = mDatabaseRef.push().getKey();
                        //mDatabaseRef.setValue(info).addOnSuccessListener(new OnSuccessListener<Void>() {
                        mDatabaseRef.child(System.currentTimeMillis()+"").setValue(book).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                cleafField();
                                Toast.makeText(BookingActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                sendEmail(e_mail);
                                addNotification();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(BookingActivity.this, "error:"+e, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }catch(Exception e){
                    Toast.makeText(BookingActivity.this, "err: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void sendEmail(String to){

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(EXTRA_EMAIL, new String[]{ to});
        email.putExtra(EXTRA_SUBJECT,"FOOD ORDERING");
        email.putExtra(EXTRA_TEXT, "Hello," +
                "This mail was Sent Because You have Order a Food in OFO (Online Order Food) Application," +
                "\n Your Food will be delivery after complete Payment!." +
                "\n Thank You.");
        //need this to prompts email client only
        email.setType("message/rfc822");
        startActivity(createChooser(email, "Dear Customer,"));
    }

    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_shop)
                        .setContentTitle("Order Info..")
                        .setAutoCancel(true)
                        .setContentText("This is Notify you that! You have Succesdfull Make an Order!");

        Intent notificationIntent = new Intent(this, ViewMyOrder.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private void cleafField(){
        mAddress.setText("");
        mPhone.setText("");
        mQuantity.setText("");
    }
}