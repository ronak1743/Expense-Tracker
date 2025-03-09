package com.ronak.expensetracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ronak.expensetracker.Model.UserModel;

public class SingActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText pass,email;
    Button sigin;
    TextView nothave;
    FirebaseDatabase database;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();


        sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singinUser();
            }
        });

        nothave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SingActivity.this,SingInActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public  void initialize(){
        auth=FirebaseAuth.getInstance();
        pass=findViewById(R.id.password_sing);
        email=findViewById(R.id.email_sing);
        sigin=findViewById(R.id.subit_sing);
        nothave=findViewById(R.id.nothaveAccount_sing);
        database=FirebaseDatabase.getInstance();
    }

    public void singinUser(){
        String e=email.getText().toString();
        String p=pass.getText().toString();

        auth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String str=auth.getCurrentUser().getUid();

                    database.getReference().child("User").child(str).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserModel us=snapshot.getValue(UserModel.class);
                            us.setPassword(p);
                            database.getReference().child("User").child(str).setValue(us);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Intent i=new Intent(SingActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }
}