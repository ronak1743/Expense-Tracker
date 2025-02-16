package com.ronak.expensetracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.ronak.expensetracker.Model.UserModel;

public class SingInActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText username,email,password;
    Button create;
    TextView haveaccount;

    FirebaseDatabase database;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sing_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth=FirebaseAuth.getInstance();

        username=findViewById(R.id.username_singin);
        email=findViewById(R.id.email_singin);
        password=findViewById(R.id.password_singin);
        haveaccount=findViewById(R.id.haveAccount_singin);
        create=findViewById(R.id.subit_singin);
        database=FirebaseDatabase.getInstance();
            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String e = email.getText().toString();
                    String p = password.getText().toString();
                    String un=username.getText().toString();
                    auth.createUserWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                UserModel u=new UserModel(un,e,p);
                                String UID=task.getResult().getUser().getUid();
                                database.getReference().child("User").child(UID).setValue(u);

                                auth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()) {

                                            Intent i=new Intent(SingInActivity.this,MainActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                        else{
                                            Toast.makeText(SingInActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            }
                            else{
                                Toast.makeText(SingInActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });


            haveaccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(SingInActivity.this,SingActivity.class);
                    startActivity(i);
                    finish();
                }
            });
    }
}