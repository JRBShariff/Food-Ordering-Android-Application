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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginAcitivty extends AppCompatActivity {
    EditText email,pass;
    FirebaseAuth firebaseAuth;
    ProgressDialog dialog;
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acitivty);
        getSupportActionBar().hide();
        email = findViewById(R.id.user);
        pass = findViewById(R.id.pass);
        btn = findViewById(R.id.btn_login);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Saving");
        dialog.setMessage("Please Wait");


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user= firebaseAuth.getCurrentUser();
        if(user!=null){
            finish();
            startActivity(new Intent(LoginAcitivty.this,HomeActivity.class));
        }

    }

    public void login(View v){
        String u=email.getText().toString();
        String p=pass.getText().toString();
        if(u.equals("")){
            email.setError("Please Fill!");
        }else if(p.equals("")){
            pass.setError("Please Fill!");
        }else{
            dialog.show();
            firebaseAuth.signInWithEmailAndPassword(u,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    dialog.dismiss();
                    if(task.isSuccessful()){
                        Toast.makeText(LoginAcitivty.this, "Login Succesfull!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginAcitivty.this,HomeActivity.class));
                    }else{
                        message("Info","Login Failed! Please Enter Correct Details.");
                    }
                }
            });
        }

    }



    public void Regsitration(View v){
        startActivity(new Intent(LoginAcitivty.this,RegisterActivity.class));
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