package com.ronak.expensetracker;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ronak.expensetracker.Adapters.ListAdapter;
import com.ronak.expensetracker.Model.UserModel;
import com.ronak.expensetracker.Model.listmodel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button singout;

    ArrayList<listmodel> list = new ArrayList<>();
    ListAdapter listAdapter;
    Button cashinbtn, cashoutbtn;
    FirebaseDatabase database;
    RecyclerView rv;
    UserModel u1;
    String UID1;
    TextView cashintxt, cashouttxt;

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

//        initilizaation
        initialize();

//        singout=findViewById(R.id.singout_main);

        checkuser();
        FirebaseUser cur = auth.getCurrentUser();
        UID1 = cur.getUid();


        listAdapter = new ListAdapter(this, list);

        getData();

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(listAdapter);


//        addUserToDB();

//        singout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                auth.signOut();
//                Intent i=new Intent(MainActivity.this,MainActivity.class);
//                startActivity(i);
//                finish();
//            }
//        });


        cashinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UserToDB.class);
                i.putExtra("type", "IN");
                startActivity(i);
            }
        });

        cashoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UserToDB.class);
                i.putExtra("type", "OUT");
                startActivity(i);
            }
        });


    }


    public  void initialize(){
        auth = FirebaseAuth.getInstance();
        cashinbtn = findViewById(R.id.cash_in_btn);
        cashoutbtn = findViewById(R.id.cash_out_btn);

        cashintxt = findViewById(R.id.total_in_main);
        cashouttxt = findViewById(R.id.total_out_main);
        rv = findViewById(R.id.recycle_main);
    }


    public  void addUserToDB(){

        FirebaseDatabase.getInstance().getReference().child("User").child(UID1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        u1 = snapshot.getValue(UserModel.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void getData(){

        FirebaseDatabase.getInstance().getReference().child("DATA").child(UID1)
                .addValueEventListener(new ValueEventListener() {
                    long a1=0;
                    long a2=0;
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot s : snapshot.getChildren()) {

                    listmodel l = s.getValue(listmodel.class);
                    list.add(l);
                    long x=Long.parseLong(l.getAmount());

                    if(l.getType().equals("IN")){
                        a1+=x;
                    }
                    else{
                        a2+=x;
                    }

                    cashintxt.setText(a1+"");
                    cashouttxt.setText(a2+"");
                }
                listAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    public  void checkuser(){

        if (auth.getCurrentUser() == null) {
            Intent i = new Intent(MainActivity.this, SingInActivity.class);
            startActivity(i);
            finish();
        }

    }
}