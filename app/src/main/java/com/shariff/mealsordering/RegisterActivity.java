package com.shariff.mealsordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {


    private EditText user,pass,name,email;
    private Button btnReg;
    FirebaseAuth firebaseAuth;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        pass=findViewById(R.id.password);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        btnReg=findViewById(R.id.btn_reg);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Saving");
        dialog.setMessage("Please Wait");

        firebaseAuth = FirebaseAuth.getInstance();

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u_email=email.getText().toString().trim();
                String u_pass=pass.getText().toString().trim();
                String u_name=name.getText().toString().trim();

                if(u_email.equals("")){
                    email.setError("Please Enter Email!");
                }else if(u_pass.equals("")){
                    pass.setError("Please Enter Password!");
                }else if(u_name.equals("")){
                    name.setError("Please Enter Name!");
                }else{
                    try{
                    dialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(u_email,u_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if(task.isSuccessful()){
                                sendEmailVarification();
                            }else{
                                message("Info","Registration Failed!");
                            }
                        }
                    });
                    }catch (Exception e){
                        message("Info","Wait Something Went Wrong! We will Notify Soon");
                    }
                }
            }
        });

    }

    private  void sendEmailVarification(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        message("Info","Successefully Registered and Varification mail Sent!");
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegisterActivity.this,LoginAcitivty.class));
                    }else{
                        message("Warning","Opps!, Varification Mail Not Sent.");
                    }
                }
            });
        }
    }

    public void loginPage(View v){
        startActivity(new Intent(RegisterActivity.this,LoginAcitivty.class));
    }
    private  void message(String title,String msg){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}