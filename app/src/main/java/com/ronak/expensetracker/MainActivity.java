package com.ronak.expensetracker;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ronak.expensetracker.Model.UserModel;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button singout;

    Button cashinbtn,cashoutbtn;

    RecyclerView rv;
    UserModel u1;
    String UID1;
    TextView cashintxt,cashouttxt;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        auth=FirebaseAuth.getInstance();
//        singout=findViewById(R.id.singout_main);
        if(auth.getCurrentUser()==null) {
            Intent i = new Intent(MainActivity.this, SingInActivity.class);
            startActivity(i);
            finish();
        }
        FirebaseUser cur=auth.getCurrentUser();

        UID1=cur.getUid();

       FirebaseDatabase.getInstance().getReference().child("User").child(UID1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                u1=snapshot.getValue(UserModel.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        singout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                auth.signOut();
//                Intent i=new Intent(MainActivity.this,MainActivity.class);
//                startActivity(i);
//                finish();
//            }
//        });



        cashinbtn=findViewById(R.id.cash_in_btn);
        cashoutbtn=findViewById(R.id.cash_out_btn);

        cashintxt=findViewById(R.id.total_in_main);
        cashouttxt=findViewById(R.id.total_out_main);

        RecyclerView r=findViewById(R.id.recycle_main);


    }
}